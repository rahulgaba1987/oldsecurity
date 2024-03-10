package com.boot.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.boot.util.JwtUtil;

@Component
public class SecurityFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserDetailsService detailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		// 1. read token from Auth Header
		   String token=request.getHeader("Authorization");
		   if(token!=null)
		   {
			 String username=  jwtUtil.getUsername(token);
			   //do validation
			 // username should not be empty and Security context must be empty
			 if(username!=null  && SecurityContextHolder.getContext().getAuthentication()==null)
			 {
				 UserDetails userDetails = detailsService.loadUserByUsername(username);
				 
				 //validate token
				 boolean isValid = jwtUtil.validateToken(token, userDetails.getUsername());
				 if(isValid)
				 {
					 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(),userDetails.getAuthorities());
					 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					 // Final object stored in security context with user details(us,psw)
					 SecurityContextHolder.getContext().setAuthentication(authToken);
				 }
				 
			 }
		   }
		   filterChain.doFilter(request, response);
		
	}

}
