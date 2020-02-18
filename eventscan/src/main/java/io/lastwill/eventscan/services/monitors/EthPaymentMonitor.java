package io.lastwill.eventscan.services.monitors;

import io.lastwill.eventscan.events.PaymentEvent;
import io.lastwill.eventscan.model.CryptoCurrency;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.model.PaymentDetailsETH;
import io.lastwill.eventscan.repositories.PaymentEthRepository;
import io.lastwill.eventscan.services.TransactionProvider;
import io.mywish.blockchain.WrapperTransaction;
import io.mywish.scanner.model.NewBlockEvent;
import io.mywish.scanner.services.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class EthPaymentMonitor {
    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private PaymentEthRepository paymentEthRepository;

    @Autowired
    private TransactionProvider transactionProvider;

    @EventListener
    private void onNewBlockEvent(NewBlockEvent event) {
        // payments only in mainnet works
        if (event.getNetworkType() != NetworkType.ETHEREUM_ROPSTEN) {
            return;
        }

        Set<String> addresses = event.getTransactionsByAddress().keySet();
        if (addresses.isEmpty()) {
            return;
        }

        List<PaymentDetailsETH> paymentDetails = paymentEthRepository.findByRxAddress(addresses, "false");
        for (PaymentDetailsETH paymentDetailsETH : paymentDetails) {
            final List<WrapperTransaction> transactions = event.getTransactionsByAddress().get(
                    paymentDetailsETH.getRxAddress().toLowerCase()
            );

            if (transactions == null) {
                log.error("User {} received from DB, but was not found in transaction list (block    {}).", paymentDetailsETH, event.getBlock().getNumber());
                continue;
            }

            transactions.forEach(transaction -> {
                if (!paymentDetailsETH.getRxAddress().equalsIgnoreCase(transaction.getOutputs().get(0).getAddress())) {
                    log.debug("Found transaction out from internal address. Skip it.");
                    return;
                }

                if (paymentDetailsETH.getValue().equals(transaction.getOutputs().get(0).getValue())) {

                transactionProvider.getTransactionReceiptAsync(event.getNetworkType(), transaction)
                        .thenAccept(receipt -> {
                            eventPublisher.publish(new PaymentEvent(
                                    transaction,
                                    paymentDetailsETH.getRxAddress(),
                                    transaction.getOutputs().get(0).getValue(),
                                    "true"
                            ));
                        })
                        .exceptionally(throwable -> {
                            log.error("UserPaymentEvent handling failed.", throwable);
                            return null;
                        });

                    paymentEthRepository.updatePaymentStatus( paymentDetailsETH.getRxAddress(),"true");
                    log.warn("\u001B[32m"+ "PAYMENT {} STATUS UPDATED!" + "\u001B[0m",paymentDetailsETH.getRxAddress());
            }
            });
        }
    }

    private BigInteger getAmountFor(final String address, final WrapperTransaction transaction) {
        BigInteger result = BigInteger.ZERO;
        if (address.equalsIgnoreCase(transaction.getInputs().get(0))) {
            result = result.subtract(transaction.getOutputs().get(0).getValue());
        }
        if (address.equalsIgnoreCase(transaction.getOutputs().get(0).getAddress())) {
            result = result.add(transaction.getOutputs().get(0).getValue());
        }
        return result;
    }

}
