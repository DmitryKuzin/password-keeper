package com.passwordkeeper.auth;

import com.passwordkeeper.user.UserDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("auth")
    public AuthDto authenticate(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            UserDetailsDto principal = (UserDetailsDto) authentication.getPrincipal();
            return AuthDto.builder().token(jwtService.generateToken(authRequest.getUsername())).userId(String.valueOf(principal.getId())).build();
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
