package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {
	@Autowired
	private MailClient mailClient;

	@Autowired
	private TemplateEngine templateEngine;

	@Test
	public void sendMail(){
		mailClient.sendMail("1589125551@qq.com", "test", "test");
	}

	@Test
	public void testHtmlMail(){
		Context context = new Context();
		context.setVariable("username", "sunday");
		String content = templateEngine.process("/mail/demo", context);
		System.out.println(content);
		mailClient.sendMail("1589125551@qq.com", "testHtml", content);

	}
}
