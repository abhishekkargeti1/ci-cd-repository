package com.auth.Application.dto;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.auth.Application.entities.Provider;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDetailsDTO {

	private UUID id;
	@NotBlank(message = "Please Enter First Name")
	private String firstName;
	private String middleName;
	private String lastName;
	@NotBlank(message = "Please Enter Email Address")
	@Email(message = "Please Enter Valid Email Address")
	private String email;
	@NotBlank(message = "Please Enter Password")
	private String password;
	@NotBlank(message = "Please Enter Contact Number")
	private String contactNumber;
	private Provider provider = Provider.Local;
	private Set<RolesDTO> roles = new HashSet<>();
	private Instant createdAt =  Instant.now();
	private Instant updateAt =  Instant.now();
	private String picture;
	private String providerId;
}
