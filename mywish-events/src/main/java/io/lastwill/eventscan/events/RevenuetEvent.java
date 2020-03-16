package io.lastwill.eventscan.events;

import io.lastwill.eventscan.events.model.BaseEvent;
import io.mywish.blockchain.WrapperTransaction;
import lombok.Getter;

import java.math.BigInteger;

@Getter
public class RevenuetEvent extends BaseEvent {
    private final WrapperTransaction transaction;
    private final String rxAddress;
    private final BigInteger amount;
    private final String status;

    public RevenuetEvent(WrapperTransaction transaction, String rxAddress, BigInteger amount, String status) {
        this.transaction = transaction;
        this.rxAddress = rxAddress;
        this.amount = amount;
        this.status = status;
    }
}
