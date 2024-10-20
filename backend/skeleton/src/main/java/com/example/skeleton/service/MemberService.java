package com.example.skeleton.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.skeleton.dto.RegisterRequestDTO;
import com.example.skeleton.model.Member;
import com.example.skeleton.model.Role;
import com.example.skeleton.repository.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Member createMember(RegisterRequestDTO request) {
		Member member = new Member();
		member.setEmail(request.getEmail());
		member.setName(request.getName());
		member.setPassword(passwordEncoder.encode(request.getPassword()));
		
		Role memberRole = roleService.findRoleByName("ROLE_USER");
		
		member.setRole(memberRole);
		
		return memberRepository.save(member);
	}
	
	public Member findMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("유저 정보를 확인할 수 없습니다" + email));
	}
	
	public boolean existsByEmail(String email) {
		return memberRepository.existsByEmail(email);
	}
	
	public boolean checkPassword(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
}
