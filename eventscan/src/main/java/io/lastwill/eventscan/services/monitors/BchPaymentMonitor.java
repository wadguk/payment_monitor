package io.lastwill.eventscan.services.monitors;

import com.github.kiulian.converter.AddressConverter;
import io.lastwill.eventscan.events.PaymentEvent;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.repositories.PaymentBchRepository;
import io.mywish.blockchain.WrapperOutput;
import io.mywish.blockchain.WrapperTransaction;
import io.mywish.scanner.model.NewBlockEvent;
import io.mywish.scanner.services.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class  BchPaymentMonitor {
    @Autowired
    private PaymentBchRepository paymentBchRepository;
    @Autowired
    private EventPublisher eventPublisher;

    @EventListener
    private void handleBtcBlock(NewBlockEvent event) {
        if (event.getNetworkType() != NetworkType.BCH_MAINNET) {
            return;
        }

        Set<String> addresses = event.getTransactionsByAddress().keySet();
//        log.warn("\u001B[32m"+ " Output {} ."+ "\u001B[0m", addresses);
        if (addresses.isEmpty()) {
            return;
        }

        Set<String> cashAddresses = new HashSet<String>() ;
        for(String temp : addresses){
//            log.warn("\u001B[32m"+ " {} " + "\u001B[0m",temp);
//            log.warn("\u001B[32m"+ " {} " + "\u001B[0m", AddressConverter.toCashAddress(temp).replace("bitcoincash:",""));
            cashAddresses.add(AddressConverter.toCashAddress(temp).replace("bitcoincash:",""));
        }


        log.warn(" Output {} .", cashAddresses);

        paymentBchRepository.findByRxAddress(cashAddresses,"false")
                .forEach(paymentDetails -> {
                    List<WrapperTransaction> txes = event.getTransactionsByAddress().get(AddressConverter.toLegacyAddress(paymentDetails.getRxAddress()));
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

                            //log.warn(" Output {} .", output);
                            String output_address = AddressConverter.toCashAddress(output.getAddress()).replace("bitcoincash:","");
                            log.warn(" Output {} ", output_address);
                            if (!output_address.equalsIgnoreCase(paymentDetails.getRxAddress())) {
                                continue;
                            }
//                            if (!output.getAddress().equalsIgnoreCase(paymentDetails.getRxAddress())) {
//                                continue;
//                            }

                            log.warn("VALUE: {}", output.getValue());
                            log.warn("VALUE: {}", paymentDetails.getValue());

                            if (output.getValue().equals(paymentDetails.getValue())) {

                                eventPublisher.publish(
                                        new PaymentEvent(
                                                tx,
                                                paymentDetails.getRxAddress(),
                                                output.getValue(),
                                                "true"
                                        ));

                                paymentBchRepository.updatePaymentStatus( paymentDetails.getRxAddress(),"true");
                                log.warn("\u001B[32m"+ "PAYMENT {} STATUS UPDATED!" + "\u001B[0m",paymentDetails.getRxAddress());
                            }
                        }
                    }
                });
    }
}
