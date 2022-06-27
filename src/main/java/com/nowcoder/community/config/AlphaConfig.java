package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

// 普通的配置类注解
@Configuration
public class AlphaConfig {

	// bean的名字就是方法名
	@Bean
	public SimpleDateFormat simpleDateFormat(){
		// 返回的对象将会被装配的容器中
		return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	}
}
