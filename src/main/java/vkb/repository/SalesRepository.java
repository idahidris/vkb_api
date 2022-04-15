package vkb.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vkb.entity.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, String>{

    Page<Sales> findAllBySalesIdLike(String id, Pageable pageable);


}