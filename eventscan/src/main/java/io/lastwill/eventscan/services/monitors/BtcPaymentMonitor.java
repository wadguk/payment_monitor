package io.lastwill.eventscan.services.monitors;

import io.lastwill.eventscan.events.PaymentEvent;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.repositories.PaymentBtcRepository;
import io.mywish.blockchain.WrapperOutput;
import io.mywish.blockchain.WrapperTransaction;
import io.mywish.scanner.model.NewBlockEvent;
import io.mywish.scanner.services.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class BtcPaymentMonitor {
    @Autowired
    private PaymentBtcRepository paymentBtcRepository;
    @Autowired
    private EventPublisher eventPublisher;

    @EventListener
    private void handleBtcBlock(NewBlockEvent event) {
        if (event.getNetworkType() != NetworkType.BTC_MAINNET) {
            return;
        }
        Set<String> addresses = event.getTransactionsByAddress().keySet();
        if (addresses.isEmpty()) {
            return;
        }
        paymentBtcRepository.findByRxAddress(addresses)
                .forEach(paymentDetails -> {
                    List<WrapperTransaction> txes = event.getTransactionsByAddress().get(paymentDetails.getRxAddress());
                    if (txes == null) {
                        //log.warn("There is no UserSiteBalance entity found for BTC address {}.", userSiteBalance.getRxAddress());
                        return;
                    }
                    for (WrapperTransaction tx: txes) {
                        for (WrapperOutput output: tx.getOutputs()) {
                            if (output.getParentTransaction() == null) {
                                log.warn("Skip it. Output {} has not parent transaction.", output);
                                continue;
                            }
                            if (!output.getAddress().equalsIgnoreCase(paymentDetails.getRxAddress())) {
                                continue;
                            }

                            if (output.getValue().equals(paymentDetails.getValue())) {

                                eventPublisher.publish(
                                        new PaymentEvent(
                                                tx,
                                                paymentDetails.getRxAddress(),
                                                output.getValue(),
                                                "true"
                                        ));

                                paymentBtcRepository.updatePaymentStatus( paymentDetails.getRxAddress(),"true");
                                log.warn("\u001B[32m"+ "PAYMENT {} STATUS UPDATED!" + "\u001B[0m",output.getAddress());
                            }
                        }
                    }
                });
    }
}
