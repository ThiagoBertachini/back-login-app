package com.tbemerencio.login.controllers;

import com.tbemerencio.login.domain.user.User;
import com.tbemerencio.login.dto.LoginRequestDto;
import com.tbemerencio.login.dto.LoginResponseDto;
import com.tbemerencio.login.dto.RegisterRequestDto;
import com.tbemerencio.login.infra.security.TokenService;
import com.tbemerencio.login.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController @RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        User user = userRepository.findByEmail(loginRequestDto.email()).orElseThrow(()
                -> new RuntimeException("User Not Found"));
        if(passwordEncoder.matches(loginRequestDto.password(), user.getPassword())){
            String token = this.tokenService.generetaToken(user);
            return ResponseEntity.ok(new LoginResponseDto(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto){
        Optional<User> user = userRepository.findByEmail(registerRequestDto.email());
        if (user.isPresent()){
           return ResponseEntity.badRequest().build();
        }
        User newUser = new User();
        newUser.setName(registerRequestDto.name());
        newUser.setEmail(registerRequestDto.email());
        newUser.setPassword(passwordEncoder.encode(registerRequestDto.password()));

        User userSaved = userRepository.save(newUser);
        String token = tokenService.generetaToken(userSaved);
        return ResponseEntity.ok(new LoginResponseDto(userSaved.getName(), token));
    }
}
