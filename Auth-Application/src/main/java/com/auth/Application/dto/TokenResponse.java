package com.auth.Application.dto;

public record TokenResponse(String accessToken, String refreshToken, long expireIn, String tokenTyp,
		UserDetailsDTO detailsDTO) {
	
	
	
	
	public static TokenResponse of (String accessToken, String refreshToken, long expireIn,UserDetailsDTO detailsDTO) {
		return new TokenResponse(accessToken,refreshToken,expireIn,"bearer",detailsDTO);
	}

}
