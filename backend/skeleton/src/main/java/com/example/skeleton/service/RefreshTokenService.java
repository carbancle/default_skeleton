package com.example.skeleton.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skeleton.model.RefreshToken;
import com.example.skeleton.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {
	private final long REFRESH_DATE = 1000L * 60 * 60;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	public void saveRefreshToken(RefreshToken tokenEntity) {
		refreshTokenRepository.save(tokenEntity);
	}
	
	public void updateRefreshToken(String email, String newRefreshToken) {
		// 기존 토큰 무효화 후 새 토큰 저장 (덮어씌우기 방식)
		RefreshToken tokenEntity = refreshTokenRepository.findByEmail(email);
		if (tokenEntity != null) {
			tokenEntity.setToken(newRefreshToken);
			tokenEntity.setExpireDate(new Date(new Date().getTime() + REFRESH_DATE));
			refreshTokenRepository.save(tokenEntity);
		} else {
			// 새로운 토큰 엔티티 생성
			RefreshToken newTokenEntity = new RefreshToken();
			newTokenEntity.setEmail(email);
			newTokenEntity.setToken(newRefreshToken);
			newTokenEntity.setExpireDate(new Date(new Date().getTime() + REFRESH_DATE));
			refreshTokenRepository.save(newTokenEntity);
		}
	}
}
