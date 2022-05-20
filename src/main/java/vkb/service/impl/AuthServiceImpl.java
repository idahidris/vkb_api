package vkb.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiError;
import vkb.controller.common.AppApiErrors;
import vkb.controller.common.AppApiResponse;
import vkb.dto.*;
import vkb.entity.ERole;
import vkb.entity.RefreshToken;
import vkb.entity.Role;
import vkb.entity.User;
import vkb.repository.RefreshTokenRepository;
import vkb.repository.RoleRepository;
import vkb.repository.UserRepository;
import vkb.service.AuthService;
import vkb.util.JwtUtils;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private ApiResponseUtil apiResponseUtil;

private Long refreshTokenDurationMs=1000L * 60 * 60 * 25;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;



    @Override
    public AppApiResponse signIn(LoginRequest loginRequest) {
        AppApiResponse appApiResponse = new AppApiResponse();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());


            RefreshToken refreshToken = createRefreshToken(userDetails.getId());

            appApiResponse.setResponseBody(
                    new JwtResponse(
                            jwt, refreshToken.getToken(), userDetails.getId(),
                    userDetails.getUsername(), userDetails.getEmail(), roles, jwtUtils.getJwtExpirationMs()));

            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(0);
            appApiResponse.setApiErrors(appApiErrors);
            appApiResponse.setRequestSuccessful(true);


        }
        catch (BadCredentialsException ex){
            log.error("invalid username or password {}", ex.getMessage());
           appApiResponse = buildFailureResponse("invalid username or password entered", "09");

        }

        return   apiResponseUtil.buildFromServiceLayer(appApiResponse);

    }

    public AppApiResponse buildFailureResponse(String msg, String code){
        AppApiResponse appApiResponse = new AppApiResponse();
        AppApiError appApiError = new AppApiError(code, msg);
        AppApiErrors appApiErrors = new AppApiErrors();
        List<AppApiError> listErrors = new ArrayList<>();
        listErrors.add(appApiError);
        appApiErrors.setApiErrorList(listErrors);
        appApiErrors.setErrorCount(1);
        appApiResponse.setApiErrors(appApiErrors);

        return apiResponseUtil.buildFromServiceLayer(appApiResponse);

    }

    @Override
    public AppApiResponse signUp(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return buildFailureResponse("Error: Username is already taken!", "03");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return buildFailureResponse("Error: Email is already in use!", "04");
        }
        // Create new user's account
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            return buildFailureResponse("Error: Role is not provided. ", "05");

        } else {
            for (String role :strRoles) {
                Role adminRole = roleRepository.findByName(ERole.valueOf(role)).orElse(null);
                if (adminRole == null) {
                    return buildFailureResponse(role + " not supported", "07");
                }
                roles.add(adminRole);

            }

        }

        userRepository.save(user);
        AppApiResponse appApiResponse = new AppApiResponse();
        appApiResponse.setResponseBody(new MessageResponse("User - "+user.getUsername()+" registered successfully!"));
        AppApiErrors appApiErrors = new AppApiErrors();
        List<AppApiError> listErrors = new ArrayList<>();
        appApiErrors.setApiErrorList(listErrors);
        appApiErrors.setErrorCount(0);
        appApiResponse.setApiErrors(appApiErrors);
        appApiResponse.setRequestSuccessful(true);
        user.setRoles(roles);

        return appApiResponse;
    }



    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setId(refreshToken.getUser().getId());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }



    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.deleteById(token.getUser().getId());
            return null;
             }
        return token;
    }


    @Transactional
    public int deleteByUserId(Long userId) {
         refreshTokenRepository.deleteAllByUser(userRepository.findById(userId).get());
         return 1;
    }





    public AppApiResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        RefreshToken refreshToken = findByToken(requestRefreshToken).orElse(null);
        if(refreshToken ==null)
            return buildFailureResponse(requestRefreshToken +":::Refresh token is not in database! ", "09");
        if(verifyExpiration(refreshToken)==null)
            return buildFailureResponse(requestRefreshToken +":::Refresh token has expired ", "09");
        User user = refreshToken.getUser();
        String token  = jwtUtils.generateTokenFromUsername(user.getUsername());

     if (token ==null)
       return buildFailureResponse(requestRefreshToken+" unable to generate token", "09");
    AppApiResponse appApiResponse = new AppApiResponse();
    appApiResponse.setResponseBody(new TokenRefreshResponse(token, requestRefreshToken, jwtUtils.getJwtExpirationMs()));
    AppApiErrors appApiErrors = new AppApiErrors();
    List<AppApiError> listErrors = new ArrayList<>();
    appApiErrors.setApiErrorList(listErrors);
    appApiErrors.setErrorCount(0);
    appApiResponse.setApiErrors(appApiErrors);
    appApiResponse.setRequestSuccessful(true);
    return  appApiResponse;
    }




}
