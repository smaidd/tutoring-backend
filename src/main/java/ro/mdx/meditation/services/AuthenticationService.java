package ro.mdx.meditation.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.mdx.meditation.exceptions.auth.BadCredentialException;
import ro.mdx.meditation.http.request.LoginUserRequest;
import ro.mdx.meditation.model.AppUser;
import ro.mdx.meditation.repository.UserRepository;

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AppUser authenticate(LoginUserRequest input) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.username(),
                            input.password()
                    )
            );
        } catch (AuthenticationException authenticationException) {
            log.error(authenticationException.getMessage(), authenticationException);
            if (authenticationException.getMessage().contains("Bad credentials")) {
                throw new BadCredentialException("Credentiale gresite");
            } else {
                throw new BadCredentialException("Authentificare fara succes");
            }
        }

        return userRepository.findByUsername(input.username())
                .orElseThrow();
    }
}
