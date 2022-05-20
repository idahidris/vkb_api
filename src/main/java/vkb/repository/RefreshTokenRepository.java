package vkb.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vkb.entity.RefreshToken;
import vkb.entity.User;

import java.util.Optional;
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Override
    Optional<RefreshToken> findById(Long id);
    Optional<RefreshToken> findByToken(String token);
    void deleteAllByUser(User user);



    @Query(value = "delete from refresh_token c where c.user.id=:#{#user.id}")
    void deleteAllByUserId(@Param("user") User user);
}