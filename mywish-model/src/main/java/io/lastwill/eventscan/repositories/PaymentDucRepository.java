package io.lastwill.eventscan.repositories;

import io.lastwill.eventscan.model.PaymentDetailsDUC;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface PaymentDucRepository extends CrudRepository<PaymentDetailsDUC, Long> {

    @Query("select c from PaymentDetailsDUC c where c.rxAddress in :rxAddress and c.status = :status")
    List<PaymentDetailsDUC> findByRxAddress(@Param("rxAddress") Collection<String> rxAddress, @Param("status") String status);

//    @Query("select c from PaymentDetails c where c.shop = :shop")
//    PaymentDetails findByMemo(@Param("shop") String memo);

    List<PaymentDetailsDUC> findAllByShop(@Param("shop") String shop);
    List<PaymentDetailsDUC> findAll();
}
