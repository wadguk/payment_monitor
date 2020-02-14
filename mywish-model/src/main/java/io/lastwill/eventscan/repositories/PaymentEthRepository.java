package io.lastwill.eventscan.repositories;

import io.lastwill.eventscan.model.PaymentDetailsDUC;
import io.lastwill.eventscan.model.PaymentDetailsETH;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface PaymentEthRepository extends CrudRepository<PaymentDetailsETH, Long> {

    @Query("select c from PaymentDetailsETH c where c.rxAddress in :rxAddress")
    List<PaymentDetailsETH> findByRxAddress(@Param("rxAddress") Collection<String> rxAddress);

    @Modifying
    @Transactional
    @Query("update PaymentDetailsETH paymentDetailsETH set paymentDetailsETH.status = :status where paymentDetailsETH.rxAddress = :rxAddress")
    void updatePaymentStatus(@Param("rxAddress") String rxAddress, @Param("status") String status);

//    @Query("select c from PaymentDetails c where c.shop = :shop")
//    PaymentDetails findByMemo(@Param("shop") String memo);

    List<PaymentDetailsETH> findAllByShop(@Param("shop") String shop);
    List<PaymentDetailsETH> findAll();
}
