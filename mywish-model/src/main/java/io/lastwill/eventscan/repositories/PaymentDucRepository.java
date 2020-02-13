package io.lastwill.eventscan.repositories;

import io.lastwill.eventscan.model.PaymentDetailsDUC;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface PaymentDucRepository extends CrudRepository<PaymentDetailsDUC, Long> {

    @Query("select c from PaymentDetailsDUC c where c.rxAddress in :rxAddress")
    List<PaymentDetailsDUC> findByRxAddress(@Param("rxAddress") Collection<String> rxAddress);

    @Modifying
    @Transactional
    @Query("update PaymentDetailsDUC paymentDetailsDUC set paymentDetailsDUC.status = :status where paymentDetailsDUC.rxAddress = :rxAddress")
    void updatePaymentStatus(@Param("rxAddress") String rxAddress, @Param("status") String status);

//    @Query("select c from PaymentDetails c where c.shop = :shop")
//    PaymentDetails findByMemo(@Param("shop") String memo);

    List<PaymentDetailsDUC> findAllByShop(@Param("shop") String shop);
    List<PaymentDetailsDUC> findAll();
}
