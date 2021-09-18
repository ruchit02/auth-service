package com.topnotch.demo.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTUtil {
	
	@Autowired
	private StreamBridge bridge ;
	
	private SecretKey key = Keys.secretKeyFor( SignatureAlgorithm.HS256 ) ;
	private String secret = null ;
	
	public JWTUtil() {
		super();
	}
	
	public void init() {
		
		secret = Encoders.BASE64URL.encode( key.getEncoded() );
		System.out.println( "Secret created : " + secret );
		
		System.out.println( "Sending Secret ...." );
		bridge.send( "secretExchange-out-0" ,  secret );
		System.out.println( "Secret Sent ...." );
	}

	public String getUsername(String token) {
		
		return this.getClaims(token).getSubject();
	}

	public Date getExpiration(String token) {
		
		return this.getClaims(token).getExpiration() ;
	}

	public boolean hasTokenExpired(String token) {
		
		return !this.getExpiration(token).before(new Date());
	}

	public boolean isValidToken(String token, UserDetails userDetails) {

		String username = this.getUsername(token);
		return username.equals(userDetails.getUsername()) && hasTokenExpired(token);
	}

	public Claims getClaims(String token) {
		
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody() ;
	}

	public String generateToken(String username) {

		//Map<String, Object> claims = new HashMap<>();
		return createToken(username);
	}

	private String createToken(String username) {
		
		return Jwts.builder()
				   .setIssuer("Ruchit Darji")
				   .setSubject(username)
				   .setIssuedAt(new Date(System.currentTimeMillis()))
				   .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12))
				   .signWith(key)
				   .compact() ;
	}
}
