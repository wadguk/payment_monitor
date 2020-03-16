package io.lastwill.eventscan.messages;

import io.lastwill.eventscan.model.CryptoCurrency;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;

@ToString(callSuper = true)
@Getter
public class PaymentNotify extends BaseNotify {
    private final BigInteger amount;
    private final String fromAddress;
    private final String toAddress;


    public PaymentNotify( String fromAddress, String toAddress, BigInteger amount, PaymentStatus status, String txHash) {
        super(status, txHash);
        this.amount = amount;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
    }

    @Override
    public String getType() {
        return "payment";
    }
}
