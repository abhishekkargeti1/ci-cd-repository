package com.auth.Application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserCredential {
	@NotBlank(message = "Please Enter Email Address")
	@Email(message = "Please Enter Valid Email Address")
	private String email;
	@NotBlank(message = "Please Enter Password")
	private String password;
}
