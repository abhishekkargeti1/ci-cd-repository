package com.auth.Application.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth.Application.entities.UserDetailsEntity;
import com.auth.Application.repositories.UserDetailsRepository;


@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserDetailsRepository repository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserDetailsEntity> userByEmail = repository.findByEmail(username);
		
			if(userByEmail.isPresent()) {
				UserDetailsEntity userDetailsEntity = userByEmail.get();
				return userDetailsEntity;
			}
			throw new UsernameNotFoundException("User Not Found Please Enter Valid Credetails");
	}
	
}
