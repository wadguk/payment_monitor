package io.mywish.scanner.services;

import io.lastwill.eventscan.repositories.PaymentDucRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentDetailsDbPersister implements PaymentDetailsPersister {
    private final PaymentDucRepository paymentDucRepository;

    public PaymentDetailsDbPersister(
            @NonNull PaymentDucRepository paymentDucRepository
    ) {
        this.paymentDucRepository = paymentDucRepository;
    }


    /*@Override
    public void open() {
        if (lastBlockNumber != null) {
            saveLastBlock(lastBlockNumber);
        } else {
            lastBlockNumber = paymentMapRepository.getLastBlockForNetwork(networkType);
        }
    }*/

//    @Override
//    public void close() {
//    }

    /*@Override
    public Long getLastBlock() {
        return lastBlockNumber;
    }*/

//    @Override
//    public synchronized void savePaymentEventStatus(String rxAddress, BigInteger value, String shop, String status) {
//        paymentMapRepository.save(new PaymentDetails(rxAddress, value,shop,status));
//
//    }
}
