package io.lastwill.eventscan.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.ZonedDateTime;

@Entity
@Setter
@Table(name = "payment_details_duc")
@Getter
public class PaymentDetailsDUC {
    @Id
    private int id;
    @Column(name = "rx_address")
    private String rxAddress;
    @Column(name = "value")
    private BigInteger value;
    @Column(name = "shop")
    private String shop;
    @Column(name = "status")
    private String status;


public PaymentDetailsDUC() {}
}
