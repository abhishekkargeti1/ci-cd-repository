package com.auth.Application.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refresh_token", indexes = { @Index(name = "refresh_token_jti", columnList = "jti", unique = true),
		@Index(name = "refresh_token_user_id", columnList = "user_id") })
@ToString
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	// token Id
	@Column(name = "jti", unique = true, nullable = false, updatable = false)
	private String jti;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	private UserDetailsEntity details;
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;
	@Column(name = "expired_at", nullable = false)
	private Instant expireAt;
	@Column(name = "revoked", nullable = false)
	private boolean revoked;
	private String replacedByToken;
}
