package com.auth.Application.controllers;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.Application.dto.RefreshTokenRequest;
import com.auth.Application.dto.TokenResponse;
import com.auth.Application.dto.UserCredential;
import com.auth.Application.dto.UserDetailsDTO;
import com.auth.Application.entities.Provider;
import com.auth.Application.entities.RefreshToken;
import com.auth.Application.entities.UserDetailsEntity;
import com.auth.Application.security.CookieService;
import com.auth.Application.security.JWTService;
import com.auth.Application.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private JWTService jwtService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CookieService cookieService;
	
	
	
//	Register User

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserDetailsDTO detailsDTO, BindingResult errorMessage) {
		System.out.println("Details in Controller "+detailsDTO);
		if (errorMessage.hasErrors()) {
			for (FieldError error : errorMessage.getFieldErrors()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getDefaultMessage());
			}
		}
		UserDetailsDTO registeredUser = authService.getUserRegister(detailsDTO);

		return ResponseEntity.status(HttpStatus.OK).body(registeredUser);
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@Valid @RequestBody UserCredential credentials, BindingResult errorMessage,
			HttpServletResponse response) {

		if (errorMessage.hasErrors()) {
			for (FieldError error : errorMessage.getFieldErrors()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getDefaultMessage());
			}
		}

		Optional<UserDetailsEntity> userDetails = authService.findByEmail(credentials.getEmail());

		if (userDetails.isPresent()) {
			UserDetailsEntity userDetailsEntity = userDetails.get();
			// log.info("Email of User {}",userDetailsEntity.getEmail());
			// log.info("Provider of User {}",userDetailsEntity.getProvider());

			if (userDetailsEntity.getProvider() == Provider.Google) {
				throw new BadCredentialsException("Please Login With Google");
			} else if (userDetailsEntity.getProvider() == Provider.Github) {
				throw new BadCredentialsException("Please Login With Github");
			}
		}

		Authentication authenticateUser = authService.authenticateUser(credentials);
		log.info("AuthenticateUser {}", authenticateUser);

		UserDetailsEntity loginUserDetails = (UserDetailsEntity) authenticateUser.getPrincipal();
		log.info("AuthenticateUser Login Details {}", loginUserDetails);

//		Generated Refresh Token

		String jti = UUID.randomUUID().toString();

		var refreshTokenDetails = RefreshToken.builder()
				.jti(jti)
				.details(loginUserDetails)
				.createdAt(Instant.now())
				.expireAt(Instant.now()
			    .plusSeconds(jwtService.getRefreshTtlSeconds()))
				.revoked(false)
				.build();

		// var refreshToken = jwtService.generateRefreshToken(loginUserDetails, jti);

		RefreshToken saveRefreshTokenDetails = authService.saveRefreshTokenDetails(refreshTokenDetails);

//		Generated Access Token 
		String accessToken = jwtService.generateToken(loginUserDetails);
		String refreshToken = null;
		if (saveRefreshTokenDetails != null) {
			refreshToken = jwtService.generateRefreshToken(loginUserDetails, refreshTokenDetails.getJti());
		}

//		Attach Refresh Token in Cookie 

		cookieService.attachRefreshCookie(response, refreshToken, (int) jwtService.getRefreshTtlSeconds());
		cookieService.addNoStoreHeaders(response);
		boolean accountStatus = loginUserDetails.isEnabled();
		if (accountStatus) {

			TokenResponse tokenResponse = TokenResponse.of(accessToken, refreshToken, jwtService.getAccessTtlSeconds(),
					modelMapper.map(loginUserDetails, UserDetailsDTO.class));
			return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
		} else {
			throw new DisabledException("User Account is Disable");
		}

	}

//     Refresh Token EndPoint
	@PostMapping("/refresh")
	public ResponseEntity<?> getRefeshToken(@RequestBody(required = false) RefreshTokenRequest tokenRequest,
			HttpServletResponse response, HttpServletRequest request) {

		Optional<String> token = authService.readRefreshToken(tokenRequest, request);
		if (token.isPresent()) {
			String refreshToken = token.get();
			if (!jwtService.isRefreshToken(refreshToken)) {
				throw new BadCredentialsException("Invalid Refresh Token");
			}
			String jti = jwtService.getJti(refreshToken);
			UUID userId = jwtService.getUserIdFromToken(refreshToken);
			Optional<RefreshToken> tokenDetails = authService.getTokenDetailFromDB(jti);
			if (tokenDetails.isPresent()) {
				RefreshToken storedRefreshToken = tokenDetails.get();
				if (storedRefreshToken.isRevoked()) {
					throw new BadCredentialsException("Token Expired");
				} else if (storedRefreshToken.getExpireAt().isBefore(Instant.now())) {
					throw new BadCredentialsException("Token Expired");
				} else if (!storedRefreshToken.getDetails().getId().equals(userId)) {
					throw new BadCredentialsException("Refresh Token not Belongs To The User");
				}

//              Revoke the Previous Token with New Token

				storedRefreshToken.setRevoked(true);
				String newJti = UUID.randomUUID().toString();
				storedRefreshToken.setReplacedByToken(newJti);
				authService.saveRefreshTokenDetails(storedRefreshToken);

//				New Refresh Token Details Created

				UserDetailsEntity details = storedRefreshToken.getDetails();
				var newRefreshTokenDetails = RefreshToken.builder().jti(newJti).details(details)
						.createdAt(Instant.now()).expireAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
						.revoked(false).build();

				authService.saveRefreshTokenDetails(newRefreshTokenDetails);

//				Creating New Access Token and Refresh Token 

				String newAccessToken = jwtService.generateToken(details);

				String newRefreshToken = jwtService.generateRefreshToken(details, newRefreshTokenDetails.getJti());

				cookieService.attachRefreshCookie(response, newRefreshToken, (int) jwtService.getRefreshTtlSeconds());
				cookieService.addNoStoreHeaders(response);

				TokenResponse tokenResponse = TokenResponse.of(newAccessToken, newRefreshToken,
						jwtService.getAccessTtlSeconds(), modelMapper.map(details, UserDetailsDTO.class));

				return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
			} else {
				throw new BadCredentialsException("Invalid Token");
			}
		}
		throw new BadCredentialsException("Invalid Token");

	}

//	Logout User 
	@PostMapping("/logout")
	public ResponseEntity<Void> userLogout(HttpServletRequest request, HttpServletResponse response) {

		Optional<String> tokenDetails = authService.readRefreshToken(null, request);
		try {
			if (tokenDetails.isPresent()) {
				String refreshToken = tokenDetails.get();

				if (jwtService.isRefreshToken(refreshToken)) {
					String jti = jwtService.getJti(refreshToken);

					Optional<RefreshToken> tokenDetailFromDB = authService.getTokenDetailFromDB(jti);
					if (tokenDetailFromDB.isPresent()) {
						RefreshToken storedRefreshToken = tokenDetailFromDB.get();
						storedRefreshToken.setRevoked(true);
						authService.saveRefreshTokenDetails(storedRefreshToken);
					}

				}

			}

		} catch (Exception e) {
			throw new BadCredentialsException("Invalid Token");
		}

		cookieService.clearRefreshCookie(response);
		cookieService.addNoStoreHeaders(response);
		SecurityContextHolder.clearContext();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}

}
