package com.auth.Application.controllers;

import java.util.Map;

import javax.security.auth.login.CredentialExpiredException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth.Application.dto.ApiErrorResponse;
import com.auth.Application.exceptions.UserAlreadyExistsException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler({ BadCredentialsException.class, CredentialExpiredException.class,
			UsernameNotFoundException.class, DisabledException.class })
	public ResponseEntity<ApiErrorResponse> authenticationException(Exception e, HttpServletRequest request) {
		//log.info("Exception Class {} ",e.getClass().getName());
		ApiErrorResponse statusResponse = ApiErrorResponse.statusResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request",
				e.getMessage(), request.getRequestURI());

		return ResponseEntity.badRequest().body(statusResponse);
	}

	 @ExceptionHandler(UserAlreadyExistsException.class)
	    public ResponseEntity<?> handleUserAlreadyExists(UserAlreadyExistsException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
	    }
	
	
	// Generic fallback
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGeneric(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
	}
}