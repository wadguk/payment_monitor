package io.lastwill.eventscan.repositories;

import io.lastwill.eventscan.model.PaymentDetailsETH;
import io.lastwill.eventscan.model.PaymentDetailsLTC;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface PaymentLtcRepository extends CrudRepository<PaymentDetailsLTC, Long> {

    @Query("select c from PaymentDetailsLTC c where c.rxAddress in :rxAddress and c.status = :status")
    List<PaymentDetailsLTC> findByRxAddress(@Param("rxAddress") Collection<String> rxAddress,@Param("status") String status);

    @Modifying
    @Transactional
    @Query("update PaymentDetailsLTC paymentDetailsLTC set paymentDetailsLTC.status = :status where paymentDetailsLTC.rxAddress = :rxAddress")
    void updatePaymentStatus(@Param("rxAddress") String rxAddress, @Param("status") String status);

//    @Query("select c from PaymentDetails c where c.shop = :shop")
//    PaymentDetails findByMemo(@Param("shop") String memo);

    List<PaymentDetailsLTC> findAllByShop(@Param("shop") String shop);
    List<PaymentDetailsLTC> findAll();
}
