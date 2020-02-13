package io.mywish.scanner.services;

import io.lastwill.eventscan.model.LastBlock;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.model.PaymentDetails;
import io.lastwill.eventscan.repositories.LastBlockRepository;
import io.lastwill.eventscan.repositories.PaymentMapRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

@Slf4j
public class PaymentDetailsDbPersister implements PaymentDetailsPersister {
    private final PaymentMapRepository paymentMapRepository;

    public PaymentDetailsDbPersister(
            @NonNull PaymentMapRepository paymentMapRepository
    ) {
        this.paymentMapRepository = paymentMapRepository;
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
