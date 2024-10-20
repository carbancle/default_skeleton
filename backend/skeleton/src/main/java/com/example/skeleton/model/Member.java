package com.example.skeleton.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	// email을 username으로 사용할 예정
	private String email;
	private String name;
	private String password;
	
	// 사용자 권한 (Role)
	@ManyToOne(fetch = FetchType.EAGER) // 다대일 관계로 설정
	@JoinColumn(name = "role_id", nullable = false) // 외래 키 컬럼 설정
	@JsonManagedReference
	private Role role; // Member는 하나의 Role만 가질 수 있음
}
