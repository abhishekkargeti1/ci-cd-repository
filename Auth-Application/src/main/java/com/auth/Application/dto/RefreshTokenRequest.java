package com.auth.Application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RefreshTokenRequest {
	@NotBlank(message="Reqiured Refresh Token")
	String refreshToken;

}
