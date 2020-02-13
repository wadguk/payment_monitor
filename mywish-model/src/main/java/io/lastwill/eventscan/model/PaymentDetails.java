package io.lastwill.eventscan.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.ZonedDateTime;

@Entity
@Setter
@Table(name = "payment_details")
@Getter
//@ToString(of = {"id", "email"})
public class PaymentDetails {
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


//    public PaymentDetails(String rxAddress, BigInteger value, String shop, String status) {
//        this.rxAddress = rxAddress;
//        this.value = value;
//        this. shop = shop;
//        this.status = status;
//    }
public PaymentDetails() {}
}
