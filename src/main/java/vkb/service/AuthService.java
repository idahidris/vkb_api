package vkb.service;

import vkb.controller.common.AppApiResponse;
import vkb.dto.LoginRequest;
import vkb.dto.SignupRequest;
import vkb.dto.TokenRefreshRequest;

public interface AuthService {

     AppApiResponse signIn(LoginRequest loginRequest);
     AppApiResponse logout(LoginRequest loginRequest);
     AppApiResponse signUp(SignupRequest signupRequest);
     AppApiResponse refreshToken(TokenRefreshRequest tokenRefreshRequest);
}
