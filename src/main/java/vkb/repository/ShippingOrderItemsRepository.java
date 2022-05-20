package vkb.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vkb.entity.ShippingOrderItems;

@Repository
public interface ShippingOrderItemsRepository extends JpaRepository<ShippingOrderItems, String>{

}