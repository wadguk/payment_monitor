package io.lastwill.eventscan.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import com.rabbitmq.client.ConnectionFactory;
import io.lastwill.eventscan.messages.BaseNotify;
//import io.lastwill.eventscan.messages.Ping;
import io.lastwill.eventscan.messages.PaymentNotify;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.services.ExternalNotifier;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Ping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class MQExternalNotifier implements ExternalNotifier {
    private final static String contentType = "application/json";
    @Autowired
    private ConnectionFactory factory;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${io.lastwill.eventscan.backend-mq.queue.ethereum}")
    private String queueNameEthereum;
    @Value("${io.lastwill.eventscan.backend-mq.queue.btc-mainnet}")
    private String queueNameBtcMainnet;
    @Value("${io.lastwill.eventscan.backend-mq.queue.duc-mainnet}")
    private String queueNameDucMainnet;

    private Map<NetworkType, String> queueByNetwork = new HashMap<>();

    @Value("${io.lastwill.eventscan.backend-mq.ttl-ms}")
    private long messageTTL;

    @Value("${io.lastwill.eventscan.backend-mq.init:false}")
    private boolean initQueue = false;

    private Connection connection;
    private Channel channel;

    @PostConstruct
    protected void init() throws IOException, TimeoutException {
        queueByNetwork.put(NetworkType.ETHEREUM_MAINNET, queueNameEthereum);

        queueByNetwork.put(NetworkType.BTC_MAINNET, queueNameBtcMainnet);

        queueByNetwork.put(NetworkType.DUC_MAINNET, queueNameDucMainnet);

        connection = factory.newConnection();
        channel = connection.createChannel();

        for (String queueName: queueByNetwork.values()) {
            if (initQueue) {
                channel.queueUnbind(queueName, queueName, queueName);
                channel.queueDelete(queueName);
                channel.exchangeDelete(queueName);
            }

            channel.exchangeDeclare(queueName, "direct", true);
            channel.queueDeclare(queueName, true, false, false,  new HashMap<>());
            channel.queueBind(queueName, queueName, queueName);

            byte[] pingJson = objectMapper.writeValueAsBytes(new Ping());

            channel.basicPublish(
                    queueName,
                    queueName,
                    new AMQP.BasicProperties.Builder()
                            .contentType(contentType)
                            .expiration("60000")
                            .type("ping")
                            .build(),
                    pingJson
            );
        }
    }

    @PreDestroy
    protected void close() {
        if (channel != null) {
            try {
                channel.close();
            }
            catch (TimeoutException | IOException e) {
                log.error("Closing channel failed.", e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            }
            catch (IOException e) {
                log.error("Closing queue connection failed.", e);
            }
        }
    }

    public void send(final NetworkType networkType, final BaseNotify notify) {
        final String queueName = queueByNetwork.get(networkType);
        if (queueName == null) {
            throw new UnsupportedOperationException("Notifier does not support network " + networkType);
        }
        send(queueName, notify);
    }


    protected synchronized void send(String queueName, BaseNotify notify) {
        try {
            byte[] json = objectMapper.writeValueAsBytes(notify);

            channel.basicPublish(
                    queueName,
                    queueName,
                    new AMQP.BasicProperties.Builder()
                            .type(notify.getType())
                            .contentType(contentType)
                            .expiration(String.valueOf(messageTTL))
                            .build(),
                    json
            );
            log.debug("Send notification type '{}' to queue '{}':\n{}", notify.getType(), queueName, new String(json));
        }
        catch (JsonProcessingException e) {
            log.error("Error on serializing message {}.", notify, e);
        }
        catch (IOException e) {
            log.error("Error on sending message {}.", notify, e);
        }

    }
}
