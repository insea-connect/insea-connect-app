package ma.insea.connect.auth.controller;

import lombok.RequiredArgsConstructor;
import ma.insea.connect.auth.dto.RegistrationResponse;
import ma.insea.connect.auth.dto.UserLoginDTO;
import ma.insea.connect.auth.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<RegistrationResponse> login(
            @RequestBody UserLoginDTO request
            )
     {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
