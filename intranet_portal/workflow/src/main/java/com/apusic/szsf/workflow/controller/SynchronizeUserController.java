package com.apusic.szsf.workflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.apusic.szsf.sync.domain.SyncUser;

@Controller
@RequestMapping("/sync")
public class SynchronizeUserController {
	
	@RequestMapping("/syncuser.do")
	public ModelAndView  SyncUser(SyncUser user){
		//to-do
		System.out.println("开始同步:"+user.getUserName());
		ModelAndView mav = new ModelAndView("form"); 
	    mav.addObject("user",user);  
		return mav;
	}

}
