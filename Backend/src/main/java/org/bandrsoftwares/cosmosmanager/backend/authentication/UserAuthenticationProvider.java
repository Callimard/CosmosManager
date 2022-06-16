package org.bandrsoftwares.cosmosmanager.backend.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bandrsoftwares.cosmosmanager.backend.data.user.User;
import org.bandrsoftwares.cosmosmanager.backend.data.user.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userEmail = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userRepository.findByEmail(userEmail);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), null);
            } else {
                throw new BadCredentialsException("Authentication failed for " + userEmail);
            }
        } else {
            throw new BadCredentialsException("Authentication failed for " + userEmail);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
