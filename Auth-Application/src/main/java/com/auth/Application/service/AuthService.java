package com.auth.Application.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.Authentication;

import com.auth.Application.dto.RefreshTokenRequest;
import com.auth.Application.dto.UserCredential;
import com.auth.Application.dto.UserDetailsDTO;
import com.auth.Application.entities.RefreshToken;
import com.auth.Application.entities.UserDetailsEntity;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

	public UserDetailsDTO getUserRegister(UserDetailsEntity entity);

	public UserDetailsDTO getUserRegister(UserDetailsDTO dto);

	public UserDetailsDTO loginUser(UserCredential credentails);

	public Authentication authenticateUser(UserCredential credentials);

	public RefreshToken saveRefreshTokenDetails(RefreshToken refreshTokenDetails);

	public Optional<String> readRefreshToken(RefreshTokenRequest tokenRequest, HttpServletRequest request);

	public Optional<RefreshToken> getTokenDetailFromDB(String jti);

	public boolean existByEmail(String email);

	public Optional<UserDetailsEntity> findByEmail(String email);

	public Optional<RefreshToken> fingByUserId(UUID userid);

}
