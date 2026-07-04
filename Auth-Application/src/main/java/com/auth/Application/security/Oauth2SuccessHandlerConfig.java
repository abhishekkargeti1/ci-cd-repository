package com.auth.Application.security;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.auth.Application.entities.Provider;
import com.auth.Application.entities.RefreshToken;
import com.auth.Application.entities.UserDetailsEntity;
import com.auth.Application.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Oauth2SuccessHandlerConfig implements AuthenticationSuccessHandler {

	@Autowired
	private AuthService authService;
	@Autowired
	private JWTService jwtService;
	@Autowired
	private CookieService cookieService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		log.info("Success Handler ");
		log.info("Authentication Info {}", authentication.toString());

//		Taking Info From Authentication 

		OAuth2User oath2User = (OAuth2User) authentication.getPrincipal();

		log.info("User Info {}", oath2User.getAttributes().toString());

		String registration = "unknown";
		if (authentication instanceof OAuth2AuthenticationToken token) {
			registration = token.getAuthorizedClientRegistrationId();
		}
		log.info("Registration Info {}", registration);

		UserDetailsEntity details = null;
		Optional<UserDetailsEntity> optionalUser = null;
		switch (registration) {
		case "google" -> {
			String googleId = oath2User.getAttributes().getOrDefault("sub", "").toString();
			String email = oath2User.getAttributes().getOrDefault("email", "").toString();
			String name = oath2User.getAttributes().getOrDefault("name", "").toString();
			String picture = oath2User.getAttributes().getOrDefault("picture", "").toString();
			details = UserDetailsEntity.builder().providerId(googleId).email(email).firstName(name)
					.provider(Provider.Google).accountStatus(true).picture(picture).build();
			optionalUser = authService.findByEmail(email);
		}

		case "github" -> {
			String githubId = oath2User.getAttributes().getOrDefault("id", "").toString();
			String email = (String) oath2User.getAttributes().get("email");
			String name = (String) oath2User.getAttributes().get("name");
			String userName = (String) oath2User.getAttributes().get("login");
			String picture = (String) oath2User.getAttributes().get("avatar_url");
			if (email == null) {
				email = userName + "@github.com";
			}
			details = UserDetailsEntity.builder().providerId(githubId).email(email).firstName(name)
					.provider(Provider.Github).accountStatus(true).picture(picture).build();

			// log.info("Email is {}",email);
			optionalUser = authService.findByEmail(email);

		}

		default -> {
			throw new RuntimeException("Invalid Provider");
		}
		}

		if (optionalUser.isPresent()) {
			details = optionalUser.get();
			log.info("user is there in database");
			log.info("User Details {}", details.toString());
		} else {
			authService.getUserRegister(details);
		}

//		Revoke Previous Token if Present

		Optional<RefreshToken> refreshTokenByUserID = authService.fingByUserId(details.getId());
		if (refreshTokenByUserID.isPresent()) {
			RefreshToken tokenDetailsRefreshToken = refreshTokenByUserID.get();
			tokenDetailsRefreshToken.setRevoked(true);
			authService.saveRefreshTokenDetails(tokenDetailsRefreshToken);
		}

//		Generate New Refresh Token And Access Token

		String jti = UUID.randomUUID().toString();

		RefreshToken refreshTokenDetails = RefreshToken.builder().jti(jti).details(details).revoked(false)
				.createdAt(Instant.now()).expireAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
				.build();

		RefreshToken saveRefreshTokenDetails = authService.saveRefreshTokenDetails(refreshTokenDetails);

		String accessToken = jwtService.generateToken(details);
		String refreshToken = null;
		if (saveRefreshTokenDetails != null) {
			refreshToken = jwtService.generateRefreshToken(details, refreshTokenDetails.getJti());
		}
		cookieService.attachRefreshCookie(response, refreshToken, (int) jwtService.getRefreshTtlSeconds());
		cookieService.addNoStoreHeaders(response);

		response.getWriter().write("Login Success");

	}

}
