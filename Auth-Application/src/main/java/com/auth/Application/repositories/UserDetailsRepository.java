package com.auth.Application.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.Application.entities.UserDetailsEntity;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, UUID> {

	boolean existsByEmail(String userEmail);
	public Optional<UserDetailsEntity> findByEmail(String email);
	
	
}
