package com.example.skeleton.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.skeleton.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	public RefreshToken findByEmail(String email);
}
