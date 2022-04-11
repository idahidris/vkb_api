package vkb.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vkb.entity.Goods;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, String>{

    List<Goods> findAllByNameAndDescription(String name, String description);

}