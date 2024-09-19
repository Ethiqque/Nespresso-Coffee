package com.nespresso.security.jwt;

import com.nespresso.security.exception.AbsentBearerHeaderException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtTokenFromAuthHeaderExtractor {

	public static final int BEARER_PREFIX_LENGTH = 7;
	public static final String BEARER_PREFIX = "Bearer ";

	@Value("${jwt.header}")
	private String jwtHttpRequestHeader;

	public String extract(final HttpServletRequest request) {
		String header = request.getHeader(jwtHttpRequestHeader);
		return extract(header);
	}

	public String extract(final String header) {
		return Optional.ofNullable(header)
				.filter(authHeader -> authHeader.startsWith(BEARER_PREFIX))
				.map(authHeader -> authHeader.substring(BEARER_PREFIX_LENGTH))
				.orElseThrow(AbsentBearerHeaderException::new);
	}
}
