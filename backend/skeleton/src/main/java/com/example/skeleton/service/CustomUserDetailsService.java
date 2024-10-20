package com.example.skeleton.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.skeleton.model.Member;
import com.example.skeleton.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member= memberRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("해당 유저 없음"));
		
		UserBuilder builder = User.withUsername(email);
		builder.password(member.getPassword());
		builder.roles(member.getRole().getName());
		
		return builder.build();	// UserDetails -> User
	}
}