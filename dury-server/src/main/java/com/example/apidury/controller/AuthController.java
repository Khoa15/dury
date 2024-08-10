package com.example.apidury.controller;


import com.example.apidury.exception.TokenRefreshException;
import com.example.apidury.model.*;
import com.example.apidury.payload.request.LoginRequest;
import com.example.apidury.payload.request.SignupRequest;
import com.example.apidury.payload.request.TokenRefreshRequest;
import com.example.apidury.payload.response.JwtResponse;
import com.example.apidury.payload.response.MessageResponse;
import com.example.apidury.payload.response.TokenRefreshResponse;
import com.example.apidury.repository.RoleRepository;
import com.example.apidury.repository.UserRepository;
import com.example.apidury.security.jwt.JwtUtils;
import com.example.apidury.security.services.FirebaseService;
import com.example.apidury.security.services.RefreshTokenService;
import com.example.apidury.security.services.UserDetailsImpl;
import com.example.apidury.security.services.UserDetailsServiceImpl;
import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.auth.openidconnect.IdTokenResponse;
import com.google.firebase.auth.FirebaseToken;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
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
  RefreshTokenService refreshTokenService;

  @Autowired
  FirebaseService firebaseService;
  @Autowired
  UserDetailsServiceImpl userDetailsService;

//  @PostMapping("/login")
//  public ResponseEntity<?> login(@RequestBody IdTokenRequest idToken){
//    try{
//      FirebaseToken decodedToken = firebaseService.authentication(idToken.getIdToken());
//      String uid = decodedToken.getUid();
//
//      return ResponseEntity.ok(new AuthResponse(uid, "JWT-TOKEN"));
//    }catch (Exception ex){
//      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
//    }
//  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    try{
      FirebaseToken decodedToken = firebaseService.authentication(loginRequest.getToken());
      String uid = decodedToken.getUid();


//      Authentication authentication = authenticationManager
//              .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//      SecurityContextHolder.getContext().setAuthentication(authentication);

//      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      UserDetailsImpl userDetails = userDetailsService.loadUserByUid(uid);

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities()
      );
      SecurityContextHolder.getContext().setAuthentication(authentication);

      String jwt = jwtUtils.generateJwtToken(userDetails);

      List<String> roles = userDetails.getAuthorities().stream()
              .map(item -> item.getAuthority())
              .collect(Collectors.toList());

      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

      return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
              userDetails.getUsername(), roles));
    }catch (Exception ex){
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid token!"));
    }
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    try{
      if(userRepository.existsById(signUpRequest.getToken())){
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Try again!"));
      }
      if (userRepository.existsByUsername(signUpRequest.getUsername())) {
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
      }
      FirebaseToken decodedToken = firebaseService.authentication(signUpRequest.getToken());
      String uid = decodedToken.getUid();
      // Create new user's account
      User user = new User(uid, signUpRequest.getUsername());

      Set<String> strRoles = signUpRequest.getRole();
      Set<Role> roles = new HashSet<>();

      if (strRoles == null) {
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
      } else {
        strRoles.forEach(role -> {
          switch (role) {
            case "admin":
              Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                      .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
              roles.add(adminRole);

              break;
            case "mod":
              Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                      .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
              roles.add(modRole);

              break;
            default:
              Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                      .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
              roles.add(userRole);
          }
        });
      }

      user.setRoles(roles);
      userRepository.save(user);

      return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }catch (Exception e){
      //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid token!"));
    }
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(user -> {
          String token = jwtUtils.generateTokenFromUsername(user.getUsername());
          return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        })
        .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
            "Refresh token is not in database!"));
  }
  
//  @PostMapping("/signout")
//  public ResponseEntity<?> logoutUser() {
//    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    String userId = userDetails.getId();
//    refreshTokenService.deleteByUserId(userId);
//    return ResponseEntity.ok(new MessageResponse("Log out successful!"));
//  }
@PostMapping("/signout")
public ResponseEntity<?> logoutUser() {
  try {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("User is not authenticated"));
    }

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String userId = userDetails.getId();


    refreshTokenService.deleteByUserId(userId);

    return ResponseEntity.ok(new MessageResponse("Log out successful!"));
  } catch (Exception e) {
    // Log lỗi để biết thêm chi tiết
    e.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("An error occurred during logout"));
  }
}


}
