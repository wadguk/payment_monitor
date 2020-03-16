package io.lastwill.eventscan.repositories;

import io.lastwill.eventscan.model.Revenue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface RevenueRepository extends CrudRepository<Revenue, Long> {

    @Query("select c from Revenue c where c.rxAddress in :rxAddress and c.status = :status")
    List<Revenue> findByRxAddress(@Param("rxAddress") Collection<String> rxAddress, @Param("status") String status);

    List<Revenue> findAllByShop(@Param("shop") String shop);
    List<Revenue> findAll();
}
