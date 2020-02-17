package io.mywish.btc.blockchain.services;

import com.neemre.btcdcli4j.core.client.BtcdClient;

import io.lastwill.eventscan.model.NetworkType;
import io.mywish.blockchain.WrapperBlock;
import io.mywish.blockchain.WrapperNetwork;
import io.mywish.blockchain.WrapperTransaction;
import io.mywish.blockchain.WrapperTransactionReceipt;
import io.mywish.btc.blockchain.helper.BchBlockParser;
import org.bitcoinj.core.NetworkParameters;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigInteger;

public class BchNetwork extends WrapperNetwork {
    final private BtcdClient bchdClient;

    @Autowired
    private WrapperBlockBchService blockBuilder;

    private final NetworkParameters networkParameters;

    @Autowired
    private BchBlockParser bchBlockParser;

    public BchNetwork(NetworkType type, BtcdClient bchdClient, NetworkParameters networkParameters) {
        super(type);
        this.bchdClient = bchdClient;
        this.networkParameters = networkParameters;

    }

    @Override
    public Long getLastBlock() throws Exception {
        return bchdClient.getBlockCount().longValue();
    }

    @Override
    public WrapperBlock getBlock(String hash) throws Exception {
        // TODO optimize
        long height = bchdClient.getBlock(hash).getHeight();
        return blockBuilder.build(
                bchBlockParser.parse(
                        networkParameters,
                        (String) bchdClient.getBlock(hash, false)
                ),
                height,
                networkParameters
        );
    }

    @Override
    public WrapperBlock getBlock(Long number) throws Exception {
        String hash = bchdClient.getBlockHash(number.intValue());
        return getBlock(hash);
    }

    @Override
    public BigInteger getBalance(String address, Long blockNo) {
        throw new UnsupportedOperationException("Method not supported");
    }

    @Override
    public WrapperTransactionReceipt getTxReceipt(WrapperTransaction transaction) {
        throw new UnsupportedOperationException("Method not supported");
    }
}
