package vkb.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vkb.entity.Report;
import vkb.entity.Subscription;

import java.util.Date;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, String>{


    Page<Subscription> findAllByCustomerIdLike(String customerId, Pageable pageable);

    @Transactional(readOnly = true)
    @Query(nativeQuery = true, value = "SELECT s.service_type as factor, SUM(s.paid_amount) as measure from subscription s WHERE s.subscription_date>=?1 AND s.subscription_date<=?2 GROUP BY s.service_type ORDER BY s.service_type ASC ")
    List<Report> fetchServiceTypeReport(Date dateStart, Date dateEnd);

    @Transactional(readOnly = true)
    @Query(nativeQuery = true, value = "SELECT s.status as factor, COUNT(s.status) as measure from subscription s WHERE s.subscription_date>=?1 AND s.subscription_date<=?2 GROUP BY s.status ORDER BY s.status ASC ")
    List<Report> fetchStatusReport(Date dateStart, Date dateEnd);




}