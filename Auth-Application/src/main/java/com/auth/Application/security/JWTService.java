package com.auth.Application.security;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth.Application.entities.Role;
import com.auth.Application.entities.UserDetailsEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class JWTService {

	private final SecretKey key;
	private final long accessTtlSeconds;
	private final long refreshTtlSeconds;
	private final String issuer;

	public JWTService(@Value("${security.jwt.secret}") String secret,
			@Value("${security.jwt.access-ttl-second}") long accessTtlSeconds,
			@Value("${security.jwt.refresh-ttl-second}") long refreshTtlSeconds,
			@Value("${security.jwt.issuer}") String issuer) {
		
		
		if(secret==null|| secret.length()<64) {
			throw new IllegalArgumentException("Invalid secret");
		}
		
		this.key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.accessTtlSeconds = accessTtlSeconds;
		this.refreshTtlSeconds = refreshTtlSeconds;
		this.issuer = issuer;
	}
	
	
//	Generate Access Token 
	
    public String generateToken(UserDetailsEntity user) {
    	Instant now =  Instant.now();
    	List<String>roles= new ArrayList();
    	if(user.getRoles()!=null) {
    		for (Role role : user.getRoles()) {
    	        roles.add(role.getName());
    	    }
    	}
    	
        return Jwts.builder()
        		.setId(UUID.randomUUID().toString())
                .setSubject(user.getId().toString())
                .setIssuer(this.issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .addClaims(Map.of(
                		"email",user.getEmail(),
                		"roles",roles,
                		"typ","access"
                		))
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }
	
    
//    Generate Refresh Token
    
    public String generateRefreshToken(UserDetailsEntity user,String tokenId) {
    	Instant now =  Instant.now();
    	List<String>roles= new ArrayList();
    	if(user.getRoles()!=null) {
    		for (Role role : user.getRoles()) {
    			roles.add(role.getName());
    		}
    	}
    	
    	return Jwts.builder()
    			.setId(tokenId)
    			.setSubject(user.getId().toString())
    			.setIssuer(this.issuer)
    			.setIssuedAt(Date.from(now))
    			.setExpiration(Date.from(now.plusSeconds(refreshTtlSeconds)))
    			.addClaims(Map.of(
    					"typ","refresh"
    					))
    			
    			.signWith(key,SignatureAlgorithm.HS512)
    			.compact();
    }
    
    
    // Token Parse Method
    public Jws<Claims> parse (String token){
    	try {
    		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
    }
    
    
    // Verify Token is Access
    public boolean  isAccessToken (String token) {
    	Claims claims=parse(token).getBody();
    	if(claims.get("typ").equals("access")) {
    		return true;
    	}
    	return false;
    }
    
    
    // Verify Token is Refresh
    public boolean  isRefreshToken (String token) {
    	Claims claims=parse(token).getBody();
    	if(claims.get("typ").equals("refresh")) {
    		return true;
    	}
    	return false;
    }
    
//    Get User User Id
    public UUID getUserIdFromToken(String token) {
    	Claims claims=parse(token).getBody();
    	return UUID.fromString(claims.getSubject());
    }
	
    
//    Get Token Id
    public String getJti(String token) {
    	Claims claims=parse(token).getBody();
    	return claims.getId();
    }
    
	

}
