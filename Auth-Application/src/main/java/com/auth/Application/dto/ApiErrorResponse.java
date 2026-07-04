package com.auth.Application.dto;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record ApiErrorResponse(int status, String error, String message, String path, OffsetDateTime timeStamp) {
	
	
	public static ApiErrorResponse statusResponse(int status, String error, String message, String path) {
		return new ApiErrorResponse(status, error, message, path, OffsetDateTime.now(ZoneOffset.UTC));
	}
	public static ApiErrorResponse statusResponse(int status, String error, String message, String path,OffsetDateTime timeStamp) {
		return new ApiErrorResponse(status, error, message, path, OffsetDateTime.now(ZoneOffset.UTC));
	}

}
