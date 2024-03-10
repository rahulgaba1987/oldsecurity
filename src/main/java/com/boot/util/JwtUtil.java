package com.boot.util;



import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil 
{

	@Value("${secret:Default RKG}")
	private String secret;
	
	
	// 6. validate username in token and database , expDate
	public boolean validateToken(String token,String username)
	{
		String tokenUsername = getUsername(token);
		return (username.equals(tokenUsername) && !isTokenExpire(token));
	}
	
	//5. Validate Expiry date
	public boolean isTokenExpire(String token)
	{
		Date expDate = getExpiryDate(token);
		return expDate.before(new Date(System.currentTimeMillis()));
	}
	
	// 4. Read Subject/ User
	public String getUsername(String token)
	{
		return getClaims(token).getSubject();
	}
	
	// 3. Read Expiry Date
	public Date getExpiryDate(String token)
	{
		return getClaims(token).getExpiration();
	}
	
	// 2. Get claims
	public Claims getClaims(String token)
	{
		return Jwts.parser().
				   setSigningKey(secret.getBytes())
				   .parseClaimsJws(token).getBody();
	}
	
	
	// 1. Generate Token
	
	public String generateToken(String subject)
	{
		//String base64Key = Encoders.BASE64.encode(secret.getBytes());
		return Jwts.builder()
				   .setSubject(subject)
				   .setIssuer("Rahul")
				   .setIssuedAt(new Date(System.currentTimeMillis()))
				   .setExpiration(new Date(System.currentTimeMillis()+TimeUnit.MINUTES.toMillis(15)))
			        .signWith(SignatureAlgorithm.HS512, secret.getBytes())
				   .compact();
				   
	}
	
}
