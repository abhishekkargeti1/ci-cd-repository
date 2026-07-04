package com.auth.Application.service;

import java.util.List;
import java.util.Optional;

import com.auth.Application.dto.UserCredential;
import com.auth.Application.dto.UserDetailsDTO;
import com.auth.Application.entities.UserDetailsEntity;

public interface UserDetails {

	public UserDetailsDTO getUserRegister(UserDetailsDTO dto);
	public UserDetailsDTO getUserRegister(UserDetailsEntity dto);
	
	public List<UserDetailsDTO> getALLUsers();
	
	public UserDetailsDTO getUserByEmailAndPassword(UserCredential credentails);
	public boolean existByEmail(String email);
	public Optional<UserDetailsEntity> findByEmail(String email);
}
