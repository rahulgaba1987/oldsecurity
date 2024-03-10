package com.boot.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.model.User;
import com.boot.model.UserRequest;
import com.boot.model.UserResponse;
import com.boot.repository.UserRepository;
import com.boot.service.UserService;
import com.boot.util.JwtUtil;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	// Save user data in database
	@PostMapping("/save")
	public ResponseEntity<String> saveUser(@RequestBody User user) {
		Integer userId = userService.saveUser(user);
		String body = " User '" + userId + "' Saved";

		return new ResponseEntity<String>(body, HttpStatus.CREATED);
	}

	
	
	
	
	// validate user and generate token
	@PostMapping("/login")
	public ResponseEntity<UserResponse> loginUser(@RequestBody UserRequest request) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
		System.out.println(request);
		String token = jwtUtil.generateToken(request.getUserName());
		return new ResponseEntity<UserResponse>(new UserResponse(token, "Success, Token generated successfully"),
				HttpStatus.OK);
	}
	
	// 3. After login
	@PostMapping("/welcome")
	public ResponseEntity<String> acceptData(Principal p)
	{
		return new ResponseEntity<String>("Hello User! "+p.getName(),HttpStatus.OK);
	}

}
