package com.boot.serviceImplementation;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.boot.model.User;
import com.boot.repository.UserRepository;
import com.boot.service.UserService;

@Service
public class UserServiceImpl implements UserService, UserDetailsService
{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder pwdEncode;

	@Override
	public Integer saveUser(User user) 
	{
		user.setPassword(pwdEncode.encode(user.getPassword()));
		return userRepository.save(user).getUserId();
	}

	@Override
	public Optional<User> findByUserName(String username) {
		
		return userRepository.findByUserName(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		  Optional<User> opt = findByUserName(username);
		  if(opt.isPresent())
		  {
			  User user= opt.get();
				return new org.springframework.security.core.userdetails.User(username, user.getPassword(), 
						user.getRoles().stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toList()));

			  
		  }
		  else
		  {
			  throw new UsernameNotFoundException("User Not exist");  
		  }
		  
		 	}

}
