package com.auth.Application.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth.Application.entities.Role;
import com.auth.Application.entities.UserDetailsEntity;
import com.auth.Application.repositories.UserDetailsRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JWTAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JWTService jwtService;

	@Autowired
	private UserDetailsRepository respository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String header = request.getHeader("Authorization");
		log.info("Header Token {} ", header);
		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			try {
				if (!jwtService.isAccessToken(token)) {
					filterChain.doFilter(request, response);
					return;
				}
				Jws<Claims> paresClaim = jwtService.parse(token);
				Claims claimsPayload = paresClaim.getBody();

				UUID userId = UUID.fromString(claimsPayload.getSubject());
				Optional<UserDetailsEntity> userDetaislByID = respository.findById(userId);
				List<GrantedAuthority> authorities = new ArrayList<>();
				if (userDetaislByID.isPresent()) {
					UserDetailsEntity userDetailsEntity = userDetaislByID.get();
					if (!userDetailsEntity.isEnabled()) {
						filterChain.doFilter(request, response);
						return;
					}
					if (userDetailsEntity.getRoles() == null) {
						if (userDetailsEntity.getRoles() != null) {
							for (Role role : userDetailsEntity.getRoles()) {
								authorities.add(new SimpleGrantedAuthority(role.getName()));
							}
						}
					}

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetailsEntity.getEmail(), null, authorities);

					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					if (SecurityContextHolder.getContext().getAuthentication() == null) {
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}

			} catch (ExpiredJwtException e) {
				request.setAttribute("error", "Token Expire");
			}  catch (Exception e) {
				request.setAttribute("error", "Invalid Token");
			}
		}
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		// TODO Auto-generated method stub
		return request.getRequestURI().startsWith("/api/v1/auth");
	}

}
