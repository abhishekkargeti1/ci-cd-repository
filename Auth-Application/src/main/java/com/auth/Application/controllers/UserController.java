package com.auth.Application.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.Application.dto.UserCredential;
import com.auth.Application.dto.UserDetailsDTO;
import com.auth.Application.service.UserDetails;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class UserController {

	@Autowired
	private UserDetails userService;

	@PostMapping("/registerUser")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserDetailsDTO detailsDto,BindingResult errorMessage){
		System.out.println("Details in Controller "+detailsDto);
		if (errorMessage.hasErrors()) {
			for (FieldError error : errorMessage.getFieldErrors()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getDefaultMessage());
			}
		}

		UserDetailsDTO userRegisterDetails = userService.getUserRegister(detailsDto);
		return ResponseEntity.status(HttpStatus.OK).body(userRegisterDetails);
	}
	
	
	
	
	
	
	
	@GetMapping("/allUser")
	public ResponseEntity<?> getAllUsers() {

		List<UserDetailsDTO> allUsersDetails = userService.getALLUsers();

		return ResponseEntity.status(HttpStatus.OK).body(allUsersDetails);
	}

	@PostMapping("/userDetails")
	public ResponseEntity<?> getUserDetails(@Valid @RequestBody UserCredential credentails,
			BindingResult errorMessage) {

		log.info("user credentails {}", credentails);
		if (errorMessage.hasErrors()) {
			for (FieldError error : errorMessage.getFieldErrors()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("error_message", error.getDefaultMessage()));
			}

		}
		UserDetailsDTO UsersDetails = userService.getUserByEmailAndPassword(credentails);

		return ResponseEntity.status(HttpStatus.OK).body(UsersDetails);
	}

}
