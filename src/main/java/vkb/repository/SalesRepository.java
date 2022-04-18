package vkb.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vkb.entity.MonthlyIncome;
import vkb.entity.Sales;

import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales, String>{

    Page<Sales> findAllBySalesIdLike(String id, Pageable pageable);

    Page<Sales> findAllByBatchIdLike(String id, Pageable pageable);

    @Modifying
    @Transactional(readOnly = true)
    @Query(nativeQuery = true, value = "SELECT FORMAT(s.sales_date,'MM') as month, SUM(s.total_price) as total from sales s WHERE s.sales_date>=?1 AND s.sales_date<=?2 GROUP BY FORMAT(s.sales_date,'MM') ORDER BY FORMAT(s.sales_date,'MM') ASC ")
    List<MonthlyIncome> fetchMonthEarnings(String dateStart, String dateEnd);




}