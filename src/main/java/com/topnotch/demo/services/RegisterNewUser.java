package com.topnotch.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.topnotch.demo.dtos.SignUpForm;
import com.topnotch.demo.models.Authority;
import com.topnotch.demo.models.DBUser;
import com.topnotch.demo.repositories.AuthorityRepository;
import com.topnotch.demo.repositories.UserRepository;

@Service
public class RegisterNewUser {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthorityRepository authRepository;
	
	@Autowired
	private PasswordEncoder encoder ;
	
	public void register( SignUpForm fmUser ) {
		
		System.out.println( "Mapping user details database object ...." );
		
		DBUser user = new DBUser() ;
		user.setUsername( fmUser.getEmail() );
		user.setPass_word( encoder.encode( fmUser.getPass_word() ) );
		
		System.out.println( "Database object created ...." );
		
		Authority authority = new Authority() ;
		authority.setAuthority( "USER" );
		authority.setUsername(user);
		
		List<Authority> authorities = new ArrayList<Authority>() ;
		authorities.add(authority) ;
		
		user.setAuthorities( authorities );
		
		userRepository.saveAndFlush( user );
		authRepository.saveAndFlush( authority ) ;
		
		System.out.println( "Object pushed to the database ...." );
	}
}