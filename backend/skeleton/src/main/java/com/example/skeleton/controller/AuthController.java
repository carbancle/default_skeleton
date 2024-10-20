package com.example.skeleton.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.example.skeleton.dto.JwtResponseDTO;
import com.example.skeleton.dto.LoginRequsetDTO;
import com.example.skeleton.dto.RegisterRequestDTO;
import com.example.skeleton.model.Member;
import com.example.skeleton.model.RefreshToken;
import com.example.skeleton.service.MemberService;
import com.example.skeleton.service.RefreshTokenService;
import com.example.skeleton.util.TokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Log4j2
public class AuthController {
	private final TokenProvider tokenProvider;
	private final MemberService memberService;
	private final RefreshTokenService refreshTokenService;
	private final long REFRESH_DATE = 1000L * 60 * 60;
	
	@PostMapping("/join")
	public ResponseEntity<?> registerMember(@RequestBody RegisterRequestDTO request) {
		if (memberService.existsByEmail(request.getEmail())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 아이디는 이미 사용중 입니다.");
		}
		
		memberService.createMember(request);
		
		return ResponseEntity.ok("회원 가입이 성공적으로 진행되었습니다.");
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequsetDTO request) {
		try {
			Member member = memberService.findMemberByEmail(request.getEmail());
			// log.info(member);
			
			boolean checkPassword = memberService.checkPassword(request.getPassword(), member.getPassword()); 
			if (checkPassword) {
				String accessToken = tokenProvider
						.createToken(
								request.getEmail(),
								member.getRole().getName(),
								member.getId()
						);
				String refreshToken = tokenProvider
						.createRefreshToken(
								request.getEmail(),
								member.getRole().getName(),
								member.getId()
						);
				RefreshToken tokenEntity = new RefreshToken();
				tokenEntity.setToken(refreshToken);
				tokenEntity.setEmail(member.getEmail());
				tokenEntity.setExpireDate(new Date(new Date().getTime() + REFRESH_DATE));
				
				refreshTokenService.updateRefreshToken(member.getEmail(), refreshToken);
				
				return ResponseEntity.ok(new JwtResponseDTO(accessToken, refreshToken));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("증명 요청이 유효하지 않음");
			}
		} catch (UsernameNotFoundException e) {
        	log.info("유저 정보 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("증명 요청이 유효하지 않음");
        }
	}
	
	@GetMapping("/me")
	public ResponseEntity<?> getAuthenticatedUser(@RequestHeader("Authorization") String token) {
		try {
			String jwtToken = token.substring(7);
			
			if (!tokenProvider.validateToken(jwtToken)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효한 토큰이 아닙니다.");
			}
			
			String email = tokenProvider.getUsername(jwtToken);
			
			Member member = memberService.findMemberByEmail(email);
			
			return ResponseEntity.ok(member);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유저 정보 인증에 실패했습니다."); 
		}
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		String refreshToken = WebUtils.getCookie(request, "refreshToken").getValue();

		if (refreshToken != null && tokenProvider.validateToken(refreshToken)) {
			String email = tokenProvider.getUsername(refreshToken);
			Member member = memberService.findMemberByEmail(email);

			String newAccessToken = tokenProvider.createToken(member.getEmail(), member.getRole().getName(), member.getId());
			String newRefreshToken = tokenProvider
					.createRefreshToken(
							member.getEmail(),
							member.getRole().getName(),
							member.getId()
					);
			
			refreshTokenService.updateRefreshToken(member.getEmail(), newRefreshToken);

			return ResponseEntity.ok(new JwtResponseDTO(newAccessToken, newRefreshToken));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효한 갱신 토큰이 아닙니다.");
		}
	}
}
