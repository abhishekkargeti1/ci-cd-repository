package com.auth.Application.serviceImpl;

import java.util.Optional;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.auth.Application.dto.RefreshTokenRequest;
import com.auth.Application.dto.UserCredential;
import com.auth.Application.dto.UserDetailsDTO;
import com.auth.Application.entities.RefreshToken;
import com.auth.Application.entities.UserDetailsEntity;
import com.auth.Application.repositories.RefreshTokenRepository;
import com.auth.Application.security.CookieService;
import com.auth.Application.security.JWTService;
import com.auth.Application.service.AuthService;
import com.auth.Application.service.UserDetails;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;

	@Autowired
	private UserDetails userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private CookieService cookieService;

	@Autowired
	private JWTService jwtService;


    AuthServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

	

	@Override
	public UserDetailsDTO getUserRegister(UserDetailsDTO dto) {
		return userService.getUserRegister(dto);
	}

	@Override
	public UserDetailsDTO getUserRegister(UserDetailsEntity entity) {
		return userService.getUserRegister(entity);
	}

	@Override
	public Authentication authenticateUser(UserCredential credentials) {
		try {
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));
		} catch (DisabledException e) {
			throw new DisabledException("User Account is Disabled");
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid Email or Password");
		}
	}

	@Override
	public UserDetailsDTO loginUser(UserCredential credentails) {
		UserDetailsDTO userDTO = userService.getUserByEmailAndPassword(credentails);
		return userDTO;
	}

	@Override
	public RefreshToken saveRefreshTokenDetails(RefreshToken refreshTokenDetails) {
		try {
			return refreshTokenRepository.save(refreshTokenDetails);
		} catch (Exception e) {
			log.info("Token Details Does't Saved {}",e);
			throw new RuntimeException("Something Went Wrong");
		}
	}

	@Override
	public Optional<String> readRefreshToken(RefreshTokenRequest tokenRequest, HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookieService.getRefreshTokenCookieName().equals(cookie.getName())) {
					String cookieValue = cookie.getValue();
					if (!cookieValue.isBlank()) {
						return Optional.of(cookieValue);
					} else {
						throw new BadCredentialsException("Refresh Token is Empty");
					}
				}
			}

		} else if (tokenRequest != null && tokenRequest.getRefreshToken() != null
				&& !tokenRequest.getRefreshToken().isBlank()) {
			return Optional.of(tokenRequest.getRefreshToken());
		}

//		Custom Header Request 
		String refreshToken = request.getHeader("X-Refresh-Token");
		if (refreshToken != null && !refreshToken.isBlank()) {
			return Optional.of(refreshToken);
		}

//		Refresh Token Send under Authorized Header

		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader != null && authHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
			String token = authHeader.substring(7).trim();
			if (token != null && !token.isBlank()) {
				try {
					if (jwtService.isRefreshToken(token)) {
						return Optional.of(token);
					}else {
						throw new BadCredentialsException("Invalid Refresh Token");						
					}
				} catch (Exception e) {
					log.warn("Invalid or expired refresh token ",e);
					throw new BadCredentialsException("Invalid Refresh Token");
				}
			}
		}

		throw new BadCredentialsException("Missing Refresh Token");

	}

	@Override
	public Optional<RefreshToken> getTokenDetailFromDB(String jti) {
		
		return refreshTokenRepository.findByJti(jti);
	}

	@Override
	public boolean existByEmail(String email) {
		
		return userService.existByEmail(email);
	}

	@Override
	public Optional<UserDetailsEntity> findByEmail(String email) {
		return userService.findByEmail(email);
	}

	@Override
	public Optional<RefreshToken> fingByUserId(UUID userid) {
		return refreshTokenRepository.findByUserId(userid);
	}
	
	

	
}
