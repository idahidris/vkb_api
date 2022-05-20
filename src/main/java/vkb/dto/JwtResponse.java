package vkb.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private long expiresInMillis;
    public JwtResponse(String accessToken, String refreshToken, Long id, String username, String email, List<String> roles, long expiresInMillis) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.expiresInMillis = expiresInMillis;
    }

}
