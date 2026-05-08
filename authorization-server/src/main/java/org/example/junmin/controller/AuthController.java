package org.example.junmin.controller;

import jakarta.annotation.Resource;
import org.example.junmin.common.dto.LoginDTO;
import org.example.junmin.common.vo.TokenVO;
import org.example.junmin.vo.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class AuthController {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private JwtEncoder jwtEncoder;

    @PostMapping("/oauth2/blog/token")
    public Result login(@RequestBody LoginDTO dto) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(), dto.getPassword()
                )
        );
        TokenVO vo = new TokenVO();
        vo.setToken(generateToken(auth));

        return Result.success(vo);
    }





    public String generateToken(Authentication auth) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:9000")
                .subject(auth.getName())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(7200))
                .claim("scope", "read")
                .claim("roles",
                        auth.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList()
                )
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(claims)
        ).getTokenValue();
    }
}
