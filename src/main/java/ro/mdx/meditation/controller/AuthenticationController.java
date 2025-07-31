package ro.mdx.meditation.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.mdx.meditation.http.request.LoginUserRequest;
import ro.mdx.meditation.http.response.LoginResponse;
import ro.mdx.meditation.model.AppUser;
import ro.mdx.meditation.services.AuthenticationService;
import ro.mdx.meditation.services.JwtService;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserRequest loginUserDto) {
        AppUser authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);
        long expirationTime = jwtService.getExpirationTime();
        LoginResponse loginResponse = new LoginResponse(jwtToken, expirationTime, authenticatedUser.getRole().toString());

        return ResponseEntity.ok(loginResponse);
    }
}
