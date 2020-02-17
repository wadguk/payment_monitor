package io.mywish.btc.blockchain;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.repositories.LastBlockRepository;
import io.mywish.btc.blockchain.helper.LtcNetworkParams;
import io.mywish.btc.blockchain.services.LtcNetwork;
import io.mywish.btc.blockchain.services.LtcScanner;
import io.mywish.scanner.services.LastBlockDbPersister;
import io.mywish.scanner.services.LastBlockPersister;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@ComponentScan
@Configuration
public class LtcBCModule {
    @Bean
    @ConditionalOnProperty("etherscanner.ltc.rpc-url.mainnet")
    @ConditionalOnClass(CloseableHttpClient.class)
    public BtcdClient btcdClient(
            final CloseableHttpClient closeableHttpClient,
            final @Value("${etherscanner.ltc.rpc-url.mainnet}") URI rpc)
            throws BitcoindException, CommunicationException {
        String user = null, password = null;
        if (rpc.getUserInfo() != null) {
            String[] credentials = rpc.getUserInfo().split(":");
            if (credentials.length > 1) {
                user = credentials[0];
                password = credentials[1];
            }
        }
        return new BtcdClientImpl(
                closeableHttpClient,
                rpc.getScheme(),
                rpc.getHost(),
                rpc.getPort(),
                user,
                password
        );
    }


    @ConditionalOnProperty("etherscanner.ltc.rpc-url.mainnet")
    @Bean(name = NetworkType.LTC_MAINNET_VALUE)
    public LtcNetwork ducNetMain(
            BtcdClient btcdClient) {
        return new LtcNetwork(NetworkType.LTC_MAINNET, btcdClient, new LtcNetworkParams());
    }

//    @ConditionalOnProperty("etherscanner.ducatus.rpc-url.mainnet")
//    @Bean(name = NetworkType.DUC_MAINNET_VALUE)
//    public DucNetwork ducNetMain(
//            final CloseableHttpClient closeableHttpClient,
//            final @Value("${etherscanner.ducatus.rpc-url.mainnet}") URI rpc
//    ) throws Exception {
//        String user = null, password = null;
//        if (rpc.getUserInfo() != null) {
//            String[] credentials = rpc.getUserInfo().split(":");
//            if (credentials.length > 1) {
//                user = credentials[0];
//                password = credentials[1];
//            }
//        }
//        return new DucNetwork(
//                NetworkType.DUC_MAINNET,
//                new BtcdClientImpl(
//                        closeableHttpClient,
//                        rpc.getScheme(),
//                        rpc.getHost(),
//                        rpc.getPort(),
//                        user,
//                        password
//                ), new DucatusNetworkParams()
//        );
//    }

    @Configuration
    public class DbPersisterConfiguration {
        @Bean
        public LastBlockPersister ltcMainnetLastBlockPersister(
                LastBlockRepository lastBlockRepository,
                final @Value("${etherscanner.ltc.last-block.mainnet:#{null}}") Long lastBlock
        ) {
            return new LastBlockDbPersister(NetworkType.LTC_MAINNET, lastBlockRepository, lastBlock);
        }
    }

    @ConditionalOnBean(name = NetworkType.LTC_MAINNET_VALUE)
    @Bean
    public LtcScanner ducScannerMain(
            final @Qualifier(NetworkType.LTC_MAINNET_VALUE) LtcNetwork network,
            final @Qualifier("ltcMainnetLastBlockPersister") LastBlockPersister lastBlockPersister,
            final @Value("${etherscanner.ltc.polling-interval-ms}") Long pollingInterval,
            final @Value("${etherscanner.ltc.commit-chain-length}") Integer commitmentChainLength
    ) {
        return new LtcScanner(
                network,
                lastBlockPersister,
                pollingInterval,
                commitmentChainLength
        );
    }
}
