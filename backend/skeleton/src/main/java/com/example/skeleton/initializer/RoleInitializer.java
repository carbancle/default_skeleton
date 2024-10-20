package com.example.skeleton.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.skeleton.model.Role;
import com.example.skeleton.repository.RoleRepository;

import jakarta.annotation.PostConstruct;

@Component
public class RoleInitializer {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@PostConstruct
	public void init() {
		if (!roleRepository.existsByName("ROLE_USER")) {
			Role roleUser = new Role();
			roleUser.setName("ROLE_USER");
			roleRepository.save(roleUser);
		}
		if (!roleRepository.existsByName("ROLE_ADMIN")) {
			Role roleUser = new Role();
			roleUser.setName("ROLE_ADMIN");
			roleRepository.save(roleUser);
		}
	}
}
