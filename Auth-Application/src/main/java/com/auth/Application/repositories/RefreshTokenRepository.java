package com.auth.Application.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auth.Application.entities.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

	
	public Optional<RefreshToken> findByJti(String jti);
	@Query(value = "select * from refresh_token where user_id= :userid ", nativeQuery = true)
	public Optional<RefreshToken> findByUserId(@Param("userid") UUID userId);
}
