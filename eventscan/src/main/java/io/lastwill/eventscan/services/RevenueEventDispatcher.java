package io.lastwill.eventscan.services;

import io.lastwill.eventscan.events.PaymentEvent;
import io.lastwill.eventscan.events.RevenuetEvent;
import io.lastwill.eventscan.messages.PaymentNotify;
import io.lastwill.eventscan.messages.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnBean(ExternalNotifier.class)
public class RevenueEventDispatcher {
    @Autowired
    private ExternalNotifier externalNotifier;

    @EventListener
    private void balanceChangedHandler(final RevenuetEvent event) {
        try {
            externalNotifier.send(
                    new PaymentNotify(
                            event.getRxAddress(),
                            event.getRxAddress(), //TO DO
                            event.getAmount(),
                            PaymentStatus.COMMITTED,
                            event.getTransaction().getHash())
            );
        }
        catch (Throwable e) {
            log.error("Sending notification about new payment failed.", e);
        }
    }
}
