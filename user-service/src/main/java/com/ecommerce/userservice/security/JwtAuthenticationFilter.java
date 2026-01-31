package com.ecommerce.userservice.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService uService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String requestToken=request.getHeader("Authorization");

        System.out.println(requestToken);

        String username=null;

        String token=null;

        if(requestToken!=null && requestToken.startsWith("Bearer")) {

            token=requestToken.substring(7);

            try {
                username=this.jwtUtil.getUsernameFromToken(token);
            }
            catch(IllegalArgumentException e) {
                System.out.println("Unable to get jwt ");
            }
            catch(ExpiredJwtException e) {
                System.out.println(e);
                System.out.println("jwt token expired");
            }catch(MalformedJwtException e) {
                System.out.println("Invalid Exception");
            }

        }else{
            System.out.println("Jwt does not begin with bearer");
        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null ) {

            UserDetails userdetails=this.uService.loadUserByUsername(username);

            if(this.jwtUtil.validateToken(token, userdetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userdetails,null, userdetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }else {
                System.out.println("Zły token JWT");
            }

        }else {
            System.out.println("Username is null or context not null");
        }

        filterChain.doFilter(request, response);
    }
}
