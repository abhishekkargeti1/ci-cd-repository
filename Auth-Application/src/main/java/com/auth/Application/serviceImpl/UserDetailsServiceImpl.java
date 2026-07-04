package com.auth.Application.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.Application.dto.UserCredential;
import com.auth.Application.dto.UserDetailsDTO;
import com.auth.Application.entities.UserDetailsEntity;
import com.auth.Application.exceptions.UserAlreadyExistsException;
import com.auth.Application.repositories.UserDetailsRepository;
import com.auth.Application.service.UserDetails;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetails {

	@Autowired
	private UserDetailsRepository respository;
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	@Transactional
	public UserDetailsDTO getUserRegister(UserDetailsDTO dto) {
		
			log.info("User Details in Service Layer {}", dto);

			if (respository.existsByEmail(dto.getEmail())) {
				throw new UserAlreadyExistsException("Email already Exists");
			}

			dto.setPassword(encoder.encode(dto.getPassword()));
			// UserDetailsEntity entity = UserDetailsMapper.toEntity(dto);
			UserDetailsEntity entity = mapper.map(dto, UserDetailsEntity.class);
			UserDetailsEntity savedDetails = respository.save(entity);

			UserDetailsDTO savedDetailsDto = mapper.map(savedDetails, UserDetailsDTO.class);

			return savedDetailsDto;
		

	}

	@Override
	public UserDetailsDTO getUserRegister(UserDetailsEntity entity) {
		try {
			log.info("User Details in Service Layer {}", entity);

			//dto.setPassword(encoder.encode(dto.getPassword()));
			// UserDetailsEntity entity = UserDetailsMapper.toEntity(dto);
			
			UserDetailsEntity savedDetails = respository.save(entity);

			UserDetailsDTO savedDetailsDto = mapper.map(savedDetails, UserDetailsDTO.class);

			return savedDetailsDto;
		} catch (Exception e) {
			throw new RuntimeException("Something Went Wrong in User Registration Please Try Something Later");
		}

	}

	@Override
	public List<UserDetailsDTO> getALLUsers() {
		List<UserDetailsEntity> userDetails = respository.findAll();

		List<UserDetailsDTO> userDetailsDTO = userDetails.stream()
				.map(entity -> mapper.map(entity, UserDetailsDTO.class)).toList();

		return userDetailsDTO;
	}

	@Override
	public UserDetailsDTO getUserByEmailAndPassword(UserCredential credentails) {

		Optional<UserDetailsEntity> userDetailsByEmail = respository.findByEmail(credentails.getEmail());
		if (userDetailsByEmail.isPresent()) {
			UserDetailsEntity userDetailsEntity = userDetailsByEmail.get();

			if (encoder.matches(credentails.getPassword(), userDetailsEntity.getPassword())) {

				UserDetailsDTO userDetailsDTO = mapper.map(userDetailsEntity, UserDetailsDTO.class);

				return userDetailsDTO;
			} else {
				throw new IllegalArgumentException("Please Enter Valid Credetails");
			}
		} else {
			throw new IllegalArgumentException("Please Enter Valid Credetails");
		}
	}

	@Override
	public boolean existByEmail(String email) {
		// TODO Auto-generated method stub
		return respository.existsByEmail(email);
	}

	@Override
	public Optional<UserDetailsEntity> findByEmail(String email) {
		return respository.findByEmail(email);
	}

	
}
