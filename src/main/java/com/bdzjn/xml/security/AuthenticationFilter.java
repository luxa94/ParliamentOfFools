package com.bdzjn.xml.security;

import com.bdzjn.xml.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class AuthenticationFilter extends GenericFilterBean {

    private final UserRepository userRepository;

    public static final String AUTHORIZATION = "X-AUTH-TOKEN";

    @Autowired
    public AuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final String token = httpServletRequest.getHeader(AUTHORIZATION);
        if (token != null && !token.isEmpty()) {
            authenticate(token);
        }

        chain.doFilter(request, response);
    }

    private void authenticate(String token) {
        userRepository.findByToken(token).ifPresent(user -> {
            final SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole().name());
            final ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(simpleGrantedAuthority);
            final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, "", authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        });
    }
}
