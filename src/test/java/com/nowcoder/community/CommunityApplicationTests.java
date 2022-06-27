package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
// 测试代码以CommunityApplication为配置， 和正式代码一样
@ContextConfiguration(classes = CommunityApplication.class)
// IoC的核心是Spring容器，Spring容器是自动创建的
// 实现ApplicationContextAware接口使当前类可以获得spring容器
// 	spring容器会检查到bean 调用set方法将自身传入
class CommunityApplicationTests implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
	}

	@Override
//	ApplicationContext Spring容器  接口  继承于HierarchicalBeanFactory
//	HierarchicalBeanFactory 继承于BeanFactory
//	BeanFactory 是Spring容器的顶级接口
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Test
	// Spring容器创建bean
	public void testApplicationContext() {
		System.out.println(applicationContext);
		AlphaDao apphoDao = applicationContext.getBean(AlphaDao.class);
		System.out.println(apphoDao.select());
		// 重命名bean，可指定创建
		apphoDao = applicationContext.getBean("alphaHibernate", AlphaDao.class);
		System.out.println(apphoDao.select());
	}

	// 管理bean的方式  单例模式
	@Test
	public void testBeanManagement() {
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);

		alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
	}

	// 容器中装载第三方bean， 编写配置类
	@Test
	public void testBeanConfig(){
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	// Spring容器将AlphoDao注入到这个属性中
	@Autowired
	// 指定获取
	@Qualifier("alphaHibernate")
	private AlphaDao alphoDao;
	@Autowired
	private AlphaService alphaService;
	@Autowired
	private SimpleDateFormat simpleDateFormat;

	@Test
	public void testDI(){
		System.out.println(alphoDao);
		System.out.println(alphaService);
		System.out.println(simpleDateFormat);
		String s;
	}

}
