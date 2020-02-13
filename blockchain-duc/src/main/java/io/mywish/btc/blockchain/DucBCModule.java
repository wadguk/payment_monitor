package io.mywish.btc.blockchain;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.repositories.LastBlockRepository;
import io.mywish.btc.blockchain.helper.DucatusNetworkParams;
import io.mywish.btc.blockchain.services.DucNetwork;
import io.mywish.btc.blockchain.services.DucScanner;
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
public class DucBCModule {
    @Bean
    @ConditionalOnProperty("etherscanner.ducatus.rpc-url.mainnet")
    @ConditionalOnClass(CloseableHttpClient.class)
    public BtcdClient btcdClient(
            final CloseableHttpClient closeableHttpClient,
            final @Value("${etherscanner.ducatus.rpc-url.mainnet}") URI rpc)
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


    @ConditionalOnProperty("etherscanner.ducatus.rpc-url.mainnet")
    @Bean(name = NetworkType.DUC_MAINNET_VALUE)
    public DucNetwork ducNetMain(
            BtcdClient btcdClient) {
        return new DucNetwork(NetworkType.DUC_MAINNET, btcdClient, new DucatusNetworkParams());
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
        public LastBlockPersister ducMainnetLastBlockPersister(
                LastBlockRepository lastBlockRepository,
                final @Value("${etherscanner.ducatus.last-block.mainnet:#{null}}") Long lastBlock
        ) {
            return new LastBlockDbPersister(NetworkType.DUC_MAINNET, lastBlockRepository, lastBlock);
        }
    }

    @ConditionalOnBean(name = NetworkType.DUC_MAINNET_VALUE)
    @Bean
    public DucScanner ducScannerMain(
            final @Qualifier(NetworkType.DUC_MAINNET_VALUE) DucNetwork network,
            final @Qualifier("ducMainnetLastBlockPersister") LastBlockPersister lastBlockPersister,
            final @Value("${etherscanner.ducatus.polling-interval-ms}") Long pollingInterval,
            final @Value("${etherscanner.ducatus.commit-chain-length}") Integer commitmentChainLength
    ) {
        return new DucScanner(
                network,
                lastBlockPersister,
                pollingInterval,
                commitmentChainLength
        );
    }
}
