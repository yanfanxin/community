package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
@Repository
// 作用范围内有一个还是多个
//@Scope("prototype")
public class AlphaService {
	/**
	 * 字段注入
	 * */
	@Autowired
//	@SuppressWarnings("all")
	private AlphaDao alphaDao;

	/**
	 * 通过set方法注入
	 * */
	private AlphaDao alphaDao1;
//	@Autowired
	public void setAlphaDao1(AlphaDao alphaDao1){
		this.alphaDao1 = alphaDao1;
	}

	/**
	 * 构造方法注入
	 * */
	private AlphaDao alphaDao2;
//	@Autowired
	public AlphaService(AlphaDao alphaDao2){
		this.alphaDao2 = alphaDao2;
	}

	public AlphaService(){
		System.out.println("实例化 AphaService");
	}

	// 在构造器之后调用
	@PostConstruct
	public void init(){
		System.out.println("init AlphaService");
	}

	// 在销毁对象之前调用
	@PreDestroy
	public void destroy(){
		System.out.println("销毁AlphaService");
	}

	public String find(){
		return alphaDao.select();
	}
}
