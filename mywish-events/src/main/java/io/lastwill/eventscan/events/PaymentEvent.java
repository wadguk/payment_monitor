package io.lastwill.eventscan.events;

import io.lastwill.eventscan.events.model.BaseEvent;
import io.lastwill.eventscan.model.CryptoCurrency;
import io.lastwill.eventscan.model.NetworkType;
import io.mywish.blockchain.WrapperTransaction;
import lombok.Getter;

import java.math.BigInteger;

@Getter
public class PaymentEvent extends BaseEvent {
    private final WrapperTransaction transaction;
    private final String rxAddress;
    private final BigInteger amount;
    private final String status;

    public PaymentEvent(WrapperTransaction transaction, String rxAddress, BigInteger amount, String status) {
        this.transaction = transaction;
        this.rxAddress = rxAddress;
        this.amount = amount;
        this.status = status;
    }
}
