package com.apusic.szsf.workflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apusic.szsf.workflow.model.User;

@Controller
@RequestMapping("/login")
public class AuthenticationController {
	
	@RequestMapping("/index.do")
	public String index(){
		return "form";
	}
	
	@RequestMapping("/login.do")
	public String login(User user){
		System.out.println("login user is "+user.getUserName());
		System.out.println("password is "+user.getPassword());
		return "form";
	}

}
