package vkb.dto;


import lombok.Data;


import java.util.Set;

@Data
public class SignupRequest {

    public String username, email, password;
    public Set<String> role;
}
