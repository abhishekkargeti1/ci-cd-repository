package com.auth.Application.security;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth.Application.config.AppConstants;
import com.auth.Application.dto.ApiErrorResponse;

import tools.jackson.databind.ObjectMapper;

@Configuration
public class SecurityConfig {
	@Autowired
	private JWTAuthFilter jwtAuthFilter;

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler; // Implement class is Oauth2SuccessHandler

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> 
				auth.requestMatchers(AppConstants.Auth_Public_URL).permitAll()
				.anyRequest().authenticated())
				.oauth2Login(oauth2 ->

				oauth2.successHandler(authenticationSuccessHandler).failureHandler(null)

				).logout(AbstractHttpConfigurer::disable)

				.exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, error) -> {
					// error.printStackTrace();
					response.setStatus(401);
					response.setContentType("application/json");
					String errorOccured = (String) request.getAttribute("error");
					ApiErrorResponse statusResponse = ApiErrorResponse.statusResponse(HttpStatus.UNAUTHORIZED.value(),
							"Unauthorized Access", errorOccured, request.getRequestURI(),
							OffsetDateTime.now(ZoneOffset.UTC));
//            	Map<String ,String> errorMessage = new HashMap<>();
//             	
//            	if(errorOccured !=null) {
//             		errorMessage.put("message", errorOccured);
//             		errorMessage.put("status", String.valueOf(401));
//            	}else {
//            		errorMessage.put("message", "Unauthorized Access "+error.getMessage());
//            		errorMessage.put("status", String.valueOf(401));
//            		
//            	}

//            	String message = "Unauthorized Access "+error.getMessage();
//            	Map<String ,String> errorMessage = Map.of("message ",message,"status",String.valueOf(401),"statusCode",new Integer(401).toString());

					var objectMapper = new ObjectMapper();
//            	response.getWriter().write(objectMapper.writeValueAsString(errorMessage));
					response.getWriter().write(objectMapper.writeValueAsString(statusResponse));
				})).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
		return configuration.getAuthenticationManager();
	}

}