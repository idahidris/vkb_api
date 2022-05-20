package vkb.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshResponse {
  private String accessToken;
  private String refreshToken;
  private String tokenType = "Bearer";
  private long refreshTokenDurationMs;

  public TokenRefreshResponse(String accessToken, String refreshToken, long refreshTokenDurationMs) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.refreshTokenDurationMs = refreshTokenDurationMs;
  }

}