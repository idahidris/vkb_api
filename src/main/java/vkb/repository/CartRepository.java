package vkb.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vkb.entity.Cart;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, String>{

    @Modifying
    @Transactional
    @Query(value = "delete from Cart c where c.userId =?1")
    void deleteAllByUserId(String userId);
    List<Cart> findAllByUserId(String userId);


}