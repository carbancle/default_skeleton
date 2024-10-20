package com.example.skeleton.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skeleton.model.Role;
import com.example.skeleton.repository.RoleRepository;

@Service
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;
	
	public Role findRoleByName(String name) {
		return roleRepository.findByName(name)
				.orElseThrow(() -> new RuntimeException("해당 권한은 존재하지 않습니다."));
	}
}
