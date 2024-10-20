package com.example.skeleton.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.skeleton.model.CustomUserDetails;
import com.example.skeleton.service.CustomUserDetailsService;
import com.example.skeleton.util.TokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final TokenProvider tokenProvider;
	private final CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 요청에서 JWT 토큰 추출
		String token = getJwtFromRequest(request);

		// 토큰이 유효한지 검사
		if (token != null && tokenProvider.validateToken(token)) {
			// 토큰에서 사용자명을 추출
			String username = tokenProvider.getUsername(token);

			UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
			CustomUserDetails details = new CustomUserDetails(userDetails, tokenProvider.getUserId(token));
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(details, null,
					userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);

		}
		// 다음 필터로 요청 전달
		filterChain.doFilter(request, response);

	}

	private String getJwtFromRequest(HttpServletRequest request) {
		// Authorization 헤더에서 Bearer 토큰 추출
		String bearerToken = request.getHeader("Autherization");
		// Bearer 토큰이 존재하고 Bearer 로 시작하면 토큰 부분만 추출
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}