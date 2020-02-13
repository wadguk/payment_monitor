package io.lastwill.eventscan.repositories;

import io.lastwill.eventscan.model.PaymentDetailsBTC;
import io.lastwill.eventscan.model.PaymentDetailsDUC;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface PaymentBtcRepository extends CrudRepository<PaymentDetailsBTC, Long> {

    @Query("select c from PaymentDetailsBTC c where c.rxAddress in :rxAddress")
    List<PaymentDetailsBTC> findByRxAddress(@Param("rxAddress") Collection<String> rxAddress);

    @Modifying
    @Transactional
    @Query("update PaymentDetailsBTC paymentDetailsBTC set paymentDetailsBTC.status = :status where paymentDetailsBTC.rxAddress = :rxAddress")
    void updatePaymentStatus(@Param("rxAddress") String rxAddress, @Param("status") String status);

//    @Query("select c from PaymentDetails c where c.shop = :shop")
//    PaymentDetails findByMemo(@Param("shop") String memo);

    List<PaymentDetailsBTC> findAllByShop(@Param("shop") String shop);
    List<PaymentDetailsBTC> findAll();
}
