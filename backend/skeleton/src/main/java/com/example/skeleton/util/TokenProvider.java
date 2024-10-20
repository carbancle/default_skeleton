package com.example.skeleton.util;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider {
	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private final long EXPIRE_DATE = 1000L * 60 * 30;
	private final long REFRESH_DATE = 1000L * 60 * 60;
	// refresh는 웬만하면 AccessToken보다 만료가 길어야함

	public String createToken(String username, String role, Long userId) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("role", role);
		claims.put("userId", userId);

		Date now = new Date();
		Date validity = new Date(now.getTime() + EXPIRE_DATE);

		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public String getUsername(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	public String getRole(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role",
				String.class);
	}

	public Long getUserId(String token) {
		return getClaims(token).get("userId", Long.class);
	}

	public String createRefreshToken(String username, String role, Long userId) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("role", role);
		claims.put("userId", userId);

		Date now = new Date();
		Date validity = new Date(now.getTime() + REFRESH_DATE);

		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}
}
