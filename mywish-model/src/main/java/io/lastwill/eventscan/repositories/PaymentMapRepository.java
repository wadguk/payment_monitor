package io.lastwill.eventscan.repositories;

import io.lastwill.eventscan.model.PaymentDetails;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface PaymentMapRepository extends CrudRepository<PaymentDetails, Long> {

    @Query("select c from PaymentDetails c where c.rxAddress in :rxAddress")
    List<PaymentDetails> findByRxAddress(@Param("rxAddress") Collection<String> rxAddress);

    @Modifying
    @Transactional
    @Query("update PaymentDetails paymentDetails set paymentDetails.status = :status where paymentDetails.rxAddress = :rxAddress")
    void updatePaymentStatus(@Param("rxAddress") String rxAddress, @Param("status") String status);

//    @Query("select c from PaymentDetails c where c.shop = :shop")
//    PaymentDetails findByMemo(@Param("shop") String memo);

    List<PaymentDetails> findAllByShop(@Param("shop") String shop);
    List<PaymentDetails> findAll();
}
