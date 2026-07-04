package com.auth.Application.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

@Service
@Getter
public class CookieService {

	private final String refreshTokenCookieName;
	private final boolean cookieHttpOnly;
	private final boolean cookieSecure;
	private final String cookieDomain;
	private final String cookieSameSite;

	public CookieService(@Value("${security.jwt.refresh-token-cookie-name}") String refreshTokenCookieName,
			@Value("${security.jwt.cookie-http-only}") boolean cookieHttpOnly,
			@Value("${security.jwt.cookie-secure}") boolean cookieSecure,
			@Value("${security.jwt.cookie-domain}") String cookieDomain,
			@Value("${security.jwt.cookie-same-site}") String cookieSameSite) {
		super();
		this.refreshTokenCookieName = refreshTokenCookieName;
		this.cookieHttpOnly = cookieHttpOnly;
		this.cookieSecure = cookieSecure;
		this.cookieDomain = cookieDomain;
		this.cookieSameSite = cookieSameSite;
	}

//	Method to Attach Cookie to the response

	public void attachRefreshCookie(HttpServletResponse response, String value, int maxAge) {

		ResponseCookieBuilder responseCookieBuilder = ResponseCookie.from(refreshTokenCookieName, value)
				.httpOnly(cookieHttpOnly).secure(cookieSecure).path("/").maxAge(maxAge).sameSite(cookieSameSite);

		if (cookieDomain != null && !cookieDomain.isBlank()) {
			responseCookieBuilder.domain(cookieDomain);
		}

		ResponseCookie responseCookie = responseCookieBuilder.build();
		response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
	}

//  Clear the Refresh Token cookie

	public void clearRefreshCookie(HttpServletResponse response) {

		ResponseCookieBuilder responseCookieBuilder = ResponseCookie.from(refreshTokenCookieName, "").maxAge(0)
				.httpOnly(cookieHttpOnly).secure(cookieSecure).path("/").sameSite(cookieSameSite);

		if (cookieDomain != null && !cookieDomain.isBlank()) {
			responseCookieBuilder.domain(cookieDomain);
		}

		ResponseCookie responseCookie = responseCookieBuilder.build();
		response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
	}

	
	
//	Anti Caching Headers
	public void addNoStoreHeaders(HttpServletResponse response) {		
		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store");
		response.setHeader("Pragma", "no-cache");
	}
}
