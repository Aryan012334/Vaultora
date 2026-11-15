package dev.aryan.minibank.auth;

import dev.aryan.minibank.auth.AuthDtos.AuthResponse;
import dev.aryan.minibank.auth.AuthDtos.LoginRequest;
import dev.aryan.minibank.auth.AuthDtos.RegisterRequest;
import dev.aryan.minibank.common.ApiException;
import dev.aryan.minibank.common.CurrentUser;
import dev.aryan.minibank.user.UserRepository;
import dev.aryan.minibank.user.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
class AuthController {

	private final AuthService authService;

	private final UserRepository users;

	AuthController(AuthService authService, UserRepository users) {
		this.authService = authService;
		this.users = users;
	}

	@PostMapping("/api/auth/register")
	@ResponseStatus(HttpStatus.CREATED)
	AuthResponse register(@Valid @RequestBody RegisterRequest request) {
		return authService.register(request);
	}

	@PostMapping("/api/auth/login")
	AuthResponse login(@Valid @RequestBody LoginRequest request) {
		return authService.login(request);
	}

	@GetMapping("/api/me")
	UserResponse me(@AuthenticationPrincipal Jwt jwt) {
		return users.findById(CurrentUser.id(jwt))
			.map(UserResponse::from)
			.orElseThrow(() -> ApiException.unauthorized("Your session has ended. Please sign in again."));
	}

}
