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
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController @RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<User> getUser(@RequestBody LoginRequestDto loginRequestDto){
        User user = userRepository.findByEmail(loginRequestDto.email()).orElseThrow(()
                -> new RuntimeException("User Not Found"));
        return ResponseEntity.ok(user);
    }
}
