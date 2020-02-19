package io.mywish.btc.blockchain;

import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.repositories.LastBlockRepository;
import io.mywish.btc.blockchain.helper.BchNetworkParams;
import io.mywish.btc.blockchain.services.BchNetwork;
import io.mywish.btc.blockchain.services.BchScanner;
import io.mywish.scanner.services.LastBlockDbPersister;
import io.mywish.scanner.services.LastBlockPersister;
import org.apache.http.impl.client.CloseableHttpClient;

import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@ComponentScan
@Configuration
public class BchBCModule {
    /**
     * Solution for test purposes only.
     * When we scan mainnet blocks we build addresses in mainnet format. And is not the same like testnet address.
     * This flag is solve the issue.
     */
    @Value("${etherscanner.bch.treat-testnet-as-mainnet:false}")
    private boolean treatTestnetAsMainnet;

    @ConditionalOnProperty("etherscanner.bch.rpc-url.mainnet")
    @Bean(name = NetworkType.BCH_MAINNET_VALUE)
    public BchNetwork bchNetMain(
            final CloseableHttpClient closeableHttpClient,
            final @Value("${etherscanner.bch.rpc-url.mainnet}") URI rpc
    ) throws Exception {
        String user = null, password = null;
        if (rpc.getUserInfo() != null) {
            String[] credentials = rpc.getUserInfo().split(":");
            if (credentials.length > 1) {
                user = credentials[0];
                password = credentials[1];
            }
        }
        return new BchNetwork(
                NetworkType.BCH_MAINNET,
                new BtcdClientImpl(
                        closeableHttpClient,
                        rpc.getScheme(),
                        rpc.getHost(),
                        rpc.getPort(),
                        user,
                        password
                ),
                treatTestnetAsMainnet ? new MainNetParams() : new MainNetParams()
        );
    }

//    @ConditionalOnProperty("etherscanner.bitcoin.rpc-url.testnet")
//    @Bean(name = NetworkType.BCH_TESTNET_3_VALUE)
//    public BchNetwork btcNetTest(
//            final CloseableHttpClient closeableHttpClient,
//            final @Value("${etherscanner.bitcoin.rpc-url.testnet}") URI rpc
//    ) throws Exception {
//        String user = null, password = null;
//        if (rpc.getUserInfo() != null) {
//            String[] credentials = rpc.getUserInfo().split(":");
//            if (credentials.length > 1) {
//                user = credentials[0];
//                password = credentials[1];
//            }
//        }
//        return new BchNetwork(
//                NetworkType.BTC_TESTNET_3,
//                new BtcdClientImpl(
//                        closeableHttpClient,
//                        rpc.getScheme(),
//                        rpc.getHost(),
//                        rpc.getPort(),
//                        user,
//                        password
//                ),
//                new TestNet3Params());
//    }

    @Configuration
    @ConditionalOnProperty("etherscanner.bch.db-persister")
    public class DbPersisterConfiguration {
        @Bean
        public LastBlockPersister bchMainnetLastBlockPersister(
                LastBlockRepository lastBlockRepository,
                final @Value("${etherscanner.bch.last-block.mainnet:#{null}}") Long lastBlock
        ) {
            return new LastBlockDbPersister(NetworkType.BCH_MAINNET, lastBlockRepository, lastBlock);
        }

//        @Bean
//        public LastBlockPersister btcTestnetLastBlockPersister(
//                LastBlockRepository lastBlockRepository,
//                final @Value("${etherscanner.bitcoin.last-block.testnet:#{null}}") Long lastBlock
//        ) {
//            return new LastBlockDbPersister(NetworkType.BTC_TESTNET_3, lastBlockRepository, lastBlock);
//        }
    }

//    @Configuration
//    @ConditionalOnProperty(value = "etherscanner.bitcoin.db-persister", havingValue = "false", matchIfMissing = true)
//    public class FilePersisterConfiguration {
//        @Bean
//        public LastBlockPersister btcMainnetLastBlockPersister(
//                final @Value("${etherscanner.start-block-dir}") String dir,
//                final @Value("${etherscanner.bitcoin.last-block.mainnet:#{null}}") Long lastBlock
//        ) {
//            return new LastBlockFilePersister(NetworkType.BTC_MAINNET, dir, lastBlock);
//        }
//
//        @Bean
//        public LastBlockPersister btcTestnetLastBlockPersister(
//                final @Value("${etherscanner.start-block-dir}") String dir,
//                final @Value("${etherscanner.bitcoin.last-block.testnet:#{null}}") Long lastBlock
//        ) {
//            return new LastBlockFilePersister(NetworkType.BTC_TESTNET_3, dir, lastBlock);
//        }
//    }

    @ConditionalOnBean(name = NetworkType.BCH_MAINNET_VALUE)
    @Bean
    public BchScanner bchScannerMain(
            final @Qualifier(NetworkType.BCH_MAINNET_VALUE) BchNetwork network,
            final @Qualifier("bchMainnetLastBlockPersister") LastBlockPersister lastBlockPersister,
            final @Value("${etherscanner.bch.polling-interval-ms}") Long pollingInterval,
            final @Value("${etherscanner.bch.commit-chain-length}") Integer commitmentChainLength
    ) {
        return new BchScanner(
                network,
                lastBlockPersister,
                pollingInterval,
                commitmentChainLength
        );
    }

//    @ConditionalOnBean(name = NetworkType.BTC_TESTNET_3_VALUE)
//    @Bean
//    public BchScanner btcScannerTest(
//            final @Qualifier(NetworkType.BTC_TESTNET_3_VALUE) BchNetwork network,
//            final @Qualifier("btcTestnetLastBlockPersister") LastBlockPersister lastBlockPersister,
//            final @Value("${etherscanner.bitcoin.polling-interval-ms}") Long pollingInterval,
//            final @Value("${etherscanner.bitcoin.commit-chain-length}") Integer commitmentChainLength
//    ) {
//        return new BchScanner(
//                network,
//                lastBlockPersister,
//                pollingInterval,
//                commitmentChainLength
//        );
//    }
}
/*
package io.mywish.btc.blockchain;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import io.lastwill.eventscan.model.NetworkType;
import io.lastwill.eventscan.repositories.LastBlockRepository;
import io.mywish.btc.blockchain.helper.BchNetworkParams;
import io.mywish.btc.blockchain.services.BchNetwork;
import io.mywish.btc.blockchain.services.BchScanner;
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
public class BchBCModule {
    @Bean
    @ConditionalOnProperty("etherscanner.bch.rpc-url.mainnet")
    @ConditionalOnClass(CloseableHttpClient.class)
    public BtcdClient bchdClient(
            final CloseableHttpClient closeableHttpClient,
            final @Value("${etherscanner.bch.rpc-url.mainnet}") URI rpc)
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


    @ConditionalOnProperty("etherscanner.bch.rpc-url.mainnet")
    @Bean(name = NetworkType.BCH_MAINNET_VALUE)
    public BchNetwork bchNetMain(
            BtcdClient bchdClient) {
        return new BchNetwork(NetworkType.BCH_MAINNET, bchdClient, new BchNetworkParams());
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
        public LastBlockPersister bchMainnetLastBlockPersister(
                LastBlockRepository lastBlockRepository,
                final @Value("${etherscanner.bch.last-block.mainnet:#{null}}") Long lastBlock
        ) {
            return new LastBlockDbPersister(NetworkType.BCH_MAINNET, lastBlockRepository, lastBlock);
        }
    }

    @ConditionalOnBean(name = NetworkType.BCH_MAINNET_VALUE)
    @Bean
    public BchScanner bchScannerMain(
            final @Qualifier(NetworkType.BCH_MAINNET_VALUE) BchNetwork network,
            final @Qualifier("bchMainnetLastBlockPersister") LastBlockPersister lastBlockPersister,
            final @Value("${etherscanner.bch.polling-interval-ms}") Long pollingInterval,
            final @Value("${etherscanner.bch.commit-chain-length}") Integer commitmentChainLength
    ) {
        return new BchScanner(
                network,
                lastBlockPersister,
                pollingInterval,
                commitmentChainLength
        );
    }
}
*/