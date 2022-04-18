package vkb.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vkb.entity.Goods;
import vkb.entity.Report;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, String>{

    List<Goods> findAllByNameAndDescription(String name, String description);

    Page<Goods> findAllByIdLike(String id, Pageable pageable);

    @Modifying
    @Transactional(readOnly = true)
    @Query(nativeQuery = true, value = "SELECT 'goods' as factor,  SUM(g.unit_price * g.quantity) as measure from goods g")
    List<Report> fetchGoodsValue();


}