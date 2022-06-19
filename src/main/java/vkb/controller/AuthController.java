package vkb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vkb.controller.common.AppApiResponse;
import vkb.dto.LoginRequest;
import vkb.dto.SignupRequest;
import vkb.dto.TokenRefreshRequest;
import vkb.service.AuthService;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(ApiController.AUTH_BASE_PATH)
public class AuthController extends ApiController {


	@Autowired
	private AuthService authService;



	@PostMapping(ApiController.SIGN_IN)
	public AppApiResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		log.info("************************** start login request api **************************");
		 AppApiResponse app = authService.signIn(loginRequest);
		log.info("************************** end login request api **************************");
		return app;

	}


	@PostMapping(ApiController.LOGOUT)
	public AppApiResponse logout(@Valid @RequestBody LoginRequest loginRequest) {
		log.info("************************** start logout request api **************************");
		AppApiResponse app = authService.logout(loginRequest);
		log.info("************************** end logout request api **************************");
		return app;

	}


	@PostMapping(ApiController.SIGN_UP)
	public AppApiResponse registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		log.info("************************** start sign up request api **************************");
		AppApiResponse app = authService.signUp(signUpRequest);
		log.info("************************** end sign up request api **************************");
		return app;
	}

	@PostMapping(ApiController.REFRESH_TOKEN)
	public AppApiResponse refreshToken(@Valid @RequestBody TokenRefreshRequest refreshRequest) {
		log.info("************************** start refresh token request api **************************");
		AppApiResponse app = authService.refreshToken(refreshRequest);
		log.info("************************** end refresh token request api **************************");
		return app;
	}
}