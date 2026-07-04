package com.auth.Application.dto;

import java.util.UUID;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RolesDTO {
	private UUID id;
	private String name;
}
