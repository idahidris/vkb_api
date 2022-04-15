package vkb.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vkb.entity.Cart;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, String>{

    List<Cart> deleteAllByUserId(String userId);
    List<Cart> findAllByUserId(String userId);


}