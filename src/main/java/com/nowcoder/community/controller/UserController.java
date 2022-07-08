package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping(path = "/user")
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Value("${community.path.upload}")
	private String uploadPath;

	@Value("${community.path.domain}")
	private String domain;

	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Autowired
	private UserService userService;

	@Autowired
	private HostHolder hostHolder;

	// 自定义的注解，登录才能访问
	@LoginRequired
	@RequestMapping(path = "/setting", method = RequestMethod.GET)
	public String getSettingPage() {
		return "/site/setting";
	}

	@LoginRequired
	@RequestMapping(path = "/upload", method = RequestMethod.POST)
	public String uploadHeader(MultipartFile headerImage, Model model) {
		if (headerImage == null) {
			model.addAttribute("error", "您还没有选择图片!");
			return "site/setting";
		}

		String fileName = headerImage.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		if (StringUtils.isBlank(suffix)) {
			model.addAttribute("error", "文件的格式不正确!");
			return "site/setting";
		}

		// 生成随机文件名
		fileName = CommunityUtil.generateUUID() + suffix;
		// 确定文件存放的路径
		File desc = new File(uploadPath + "/" + fileName);
		try {
			// 存储文件
			headerImage.transferTo(desc);
		} catch (IOException e) {
			logger.error("上传文件失败： " + e.getMessage());
			throw new RuntimeException("上传文件失败， 服务器发生异常", e);
		}

		// 更新当前用户的头像的路径(web访问路径)
		// http://localhost:808/communtiy/user/header/xxx.png
		User user = hostHolder.getUser();
		String headerUrl = domain + contextPath + "/user/header/" + fileName;
		userService.updateHeader(user.getId(), headerUrl);
		return "redirect:/index";
	}

	@RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
	public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
		// 服务器存放的路径
		fileName = uploadPath + "/" + fileName;
		// 文件后缀
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		// 响应图片
		response.setContentType("image/" + suffix);
		try (
				// 这里声明的变量会自动添加到final里，如果有close方法会自动关闭
				OutputStream os = response.getOutputStream();
				FileInputStream fis = new FileInputStream(fileName);
		) {
			byte[] buffer = new byte[1024];
			int b = 0;
			while ((b = fis.read(buffer)) != -1) {
				os.write(buffer, 0, b);
			}
		} catch (IOException e) {
			logger.error("读取录像失败" + e.getMessage());
		}

	}


	@LoginRequired
	@RequestMapping(path = "/updatePassword", method = RequestMethod.POST)
	public String updatePassword(String oldPassword, String newPassword, String rptPassword, Model model) {
		User user = hostHolder.getUser();
		oldPassword = CommunityUtil.md5(oldPassword + user.getSalt());
		if (!oldPassword.equals(user.getPassword())) {
			model.addAttribute("oldPasswordMsg", "密码输入有误！");
			return "/site/setting";
		}

		if (StringUtils.isBlank(newPassword)) {
			model.addAttribute("newPasswordMsg", "请输入新密码");
			return "/site/setting";
		}

		if (StringUtils.isBlank(rptPassword)) {
			model.addAttribute("rptPasswordMsg", "请重复新密码");
			return "/site/setting";
		}

		if (!newPassword.equals(rptPassword)) {
			model.addAttribute("rptPasswordMsg", "两次密码输入不一致");
			return "/site/setting";
		}

		newPassword = CommunityUtil.md5(newPassword + user.getSalt());
		if (newPassword.equals(oldPassword)) {
			model.addAttribute("newPasswordMsg", "新老密码一致！");
			return "/site/setting";
		}

		userService.updatePassword(user.getId(), newPassword);
		return "redirect:/index";
	}
}
