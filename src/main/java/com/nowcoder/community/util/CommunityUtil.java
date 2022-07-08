package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {

	// 生成随机字符串
	public static String generateUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}

	// MD5加密
	public static String md5(String key){
		if (StringUtils.isBlank(key)){
			return null;
		}
		return DigestUtils.md5DigestAsHex(key.getBytes());
	}

	public static void main(String[] args) {
		String s1 =  md5("816c384b92b2d5001f1aebb20ae080d9" + "ef519");
		String s3 =  md5("qwe123" + "ef519");
		String s2 = "816c384b92b2d5001f1aebb20ae080d9";
		System.out.println(s1.equals(s2));
	}
}
