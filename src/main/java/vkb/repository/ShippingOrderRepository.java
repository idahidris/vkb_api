package vkb.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vkb.entity.ShippingOrder;

@Repository
public interface ShippingOrderRepository extends JpaRepository<ShippingOrder, String>{
 Page<ShippingOrder> findAllByBatchIdLike(String batchId, Pageable pageable);

}