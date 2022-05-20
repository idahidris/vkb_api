package vkb.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import javax.persistence.*;
@Entity(name = "refresh_token")
@Getter
@Setter
public class RefreshToken {

  @Id
  private long id;


  @OneToOne(cascade=CascadeType.PERSIST , fetch=FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id" )
  private User user;


  @Column(nullable = false, unique = true)
  private String token;
  @Column(nullable = false)
  private Instant expiryDate;

}