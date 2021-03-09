package com.safe.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.safe.dao.UserRepo;
import com.safe.entities.Contact;
import com.safe.entities.User;
import com.safe.helpper.Message;

@Controller
public class HomeController {
//	
//	@Autowired
//	private UserRepo UserRepo; 
//	
//	
//	@GetMapping("/test")
//	@ResponseBody
//	public String test() {
//		
//		User user = new User();
//		user.setName("Anwar");
//		user.setEmail("mdanwar7764@gmail.com");
//		
//		Contact contact=new Contact();
//		
//		user.getContacts().add(contact);
//		
//		
//		UserRepo.save(user);
//		return "Working";
//	}
	
	@Autowired
	private BCryptPasswordEncoder PasswordEncoder;
	
	@Autowired
	private UserRepo userRepo;
	
	
	@RequestMapping("/")
 	public String home(Model model) {
		
		model.addAttribute("title", "Home - Safecontact");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		
		model.addAttribute("title", "About - Safecontact");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		
		model.addAttribute("title", "Register - Safecontact");
		model.addAttribute("user", new User());
		return "signup";
	}


	//handler for registering user
	
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result1, @RequestParam(value = "agreement",defaultValue = "false") boolean agreement, Model model, 
			HttpSession session) {
		
	try {
		
		if(!agreement) {
			System.out.println("You have not agree the terms and conditions");
			throw new Exception("You have not agree the terms and conditions");
		
		}
		
		if(result1.hasErrors()) {
			System.out.println("ERROR "+ result1.toString());
			model.addAttribute("user", user);
			return "signup";
		}
		
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImageUrl("default.png");
		user.setPassword(PasswordEncoder.encode(user.getPassword()));
		
		System.out.println("Agreement "+agreement);
		System.out.println("USER  "+user);
		
		User result = this.userRepo.save(user);
		
		model.addAttribute("user", new User());
		session.setAttribute("message", new Message("Successfully registered !!", "alert-success"));
		return "signup";
		
	} catch (Exception e) {
		// TODO: handle exception
		
		e.printStackTrace();
		model.addAttribute("user", user);
		session.setAttribute("message", new Message("Something Went Wrong !!"+e.getMessage(), "alert-danger"));
		return "signup";
		
	}
		
	}
	
	
	//handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model) {
		model.addAttribute("title", "Login Page");
		return "login";
	}

}
