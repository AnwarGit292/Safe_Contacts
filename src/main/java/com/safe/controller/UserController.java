package com.safe.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.safe.dao.UserRepo;
import com.safe.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepo userRepo;

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		
		String userName = principal.getName();
		System.out.println("USERNAME "+userName);
		
		User user = userRepo.getUserByUserName(userName);
		System.out.println("USER "+user);
		
		model.addAttribute("user",user);
		
		return "normal/user_dashboard";
	}
	
}
