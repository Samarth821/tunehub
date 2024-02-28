package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Song;
import com.example.demo.entity.Users;
import com.example.demo.service.SongService;
import com.example.demo.service.UsersService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UsersController {
	@Autowired
	UsersService service;
	
	@Autowired
	SongService songService;

	@PostMapping("/registration")
	public String addUsers(@ModelAttribute Users user) 
	{
		boolean userStatus = service.emailExists(user.getEmail());
		if (userStatus  == false) {
			service.addUser(user);
			System.out.println("User added");
		} else {
			System.out.println("User exists");
		}
		return "login.html";
	}
	@PostMapping("/validate")
	public String validate(@RequestParam("email") String email, 
			@RequestParam("password") String password, HttpSession session, Model model) 
	{
		if(service.validateUser(email, password))
		{
			String role = service.getRole(email);
			
			session.setAttribute("email", email);
			if(role.equals("admin"))
			{
				return "adminHome.html";
			}
			else {
                Users user = service.getUser(email);
                boolean userStatus = user.isPremium();
                List<Song> songsList =  songService.fetchAllSongs();

                model.addAttribute("isPremium", userStatus);
                model.addAttribute("songs", songsList);
                return "customerHome.html";
			}
		}
		else
		{
			return "login.html";
		}
		
	}
	

	@GetMapping("/logout")
	public String logout(HttpSession session)  {
		session.invalidate();
		return "login.html";
	}
	
	
	
	

}
