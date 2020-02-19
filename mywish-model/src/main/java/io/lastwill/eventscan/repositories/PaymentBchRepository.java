package io.lastwill.eventscan.repositories;

import io.lastwill.eventscan.model.PaymentDetailsBCH;
import io.lastwill.eventscan.model.PaymentDetailsBTC;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface PaymentBchRepository extends CrudRepository<PaymentDetailsBCH, Long> {

    @Query("select c from PaymentDetailsBCH c where c.rxAddress  in :rxAddress and c.status = :status")
    List<PaymentDetailsBCH> findByRxAddress(@Param("rxAddress") Collection<String> rxAddress, @Param("status") String status);

    @Modifying
    @Transactional
    @Query("update PaymentDetailsBCH paymentDetailsBCH set paymentDetailsBCH.status = :status where paymentDetailsBCH.rxAddress = :rxAddress")
    void updatePaymentStatus(@Param("rxAddress") String rxAddress, @Param("status") String status);

//    @Query("select c from PaymentDetails c where c.shop = :shop")
//    PaymentDetails findByMemo(@Param("shop") String memo);

    List<PaymentDetailsBCH> findAllByShop(@Param("shop") String shop);
    List<PaymentDetailsBCH> findAll();
}
