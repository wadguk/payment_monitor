package io.lastwill.eventscan.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

@Entity
@Setter
@Table(name = "payment_requests_merchantshop")
@Getter
public class Revenue {
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


public Revenue() {}
}
