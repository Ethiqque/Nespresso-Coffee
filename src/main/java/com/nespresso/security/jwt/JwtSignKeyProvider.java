package com.nespresso.security.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtSignKeyProvider {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.refresh.secret}")
	private String secretRefreshKey;

	public Key get() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public Key getRefresh() {
		byte[] keyBytes = Decoders.BASE64.decode(secretRefreshKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
