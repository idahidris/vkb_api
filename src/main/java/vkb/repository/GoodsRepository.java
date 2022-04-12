package vkb.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vkb.entity.Goods;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, String>{

    List<Goods> findAllByNameAndDescription(String name, String description);

    Page<Goods> findAllByIdLike(String id, Pageable pageable);


}