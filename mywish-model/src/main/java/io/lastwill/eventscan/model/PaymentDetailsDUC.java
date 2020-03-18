package io.lastwill.eventscan.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.ZonedDateTime;

@Entity
@Setter
@Table(name = "payment_requests_paymentrequest")
@Getter
public class PaymentDetailsDUC {
    @Id
    private int id;
    @Column(name = "duc_address")
    private String rxAddress;
    @Column(name = "value")
    private BigInteger value;
    @Column(name = "shop")
    private String shop;
    @Column(name = "status")
    private String status;


    public PaymentDetailsDUC() {}
}

