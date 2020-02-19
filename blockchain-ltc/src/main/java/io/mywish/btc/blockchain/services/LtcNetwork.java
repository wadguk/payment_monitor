package io.mywish.btc.blockchain.services;

import com.neemre.btcdcli4j.core.client.BtcdClient;
import io.mywish.btc.blockchain.helper.LtcBlockParser;
import io.lastwill.eventscan.model.NetworkType;
import io.mywish.blockchain.WrapperBlock;
import io.mywish.blockchain.WrapperNetwork;
import io.mywish.blockchain.WrapperTransaction;
import io.mywish.blockchain.WrapperTransactionReceipt;
import org.bitcoinj.core.NetworkParameters;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigInteger;

public class LtcNetwork extends WrapperNetwork {
    final private BtcdClient ltcdClient;

    @Autowired
    private WrapperBlockLtcService blockBuilder;

    private final NetworkParameters networkParameters;

    @Autowired
    private LtcBlockParser ltcBlockParser;

    public LtcNetwork(NetworkType type, BtcdClient ltcdClient, NetworkParameters networkParameters) {
        super(type);
        this.ltcdClient = ltcdClient;
        this.networkParameters = networkParameters;
    }

    @Override
    public Long getLastBlock() throws Exception {
        return ltcdClient.getBlockCount().longValue();
    }

    @Override
    public WrapperBlock getBlock(String hash) throws Exception {
        // TODO optimize
        long height = ltcdClient.getBlock(hash).getHeight();
        return blockBuilder.build(
                ltcBlockParser.parse(
                        networkParameters,
                        (String) ltcdClient.getBlock(hash, false)
                ),
                height,
                networkParameters
        );
    }

    @Override
    public WrapperBlock getBlock(Long number) throws Exception {
        String hash = ltcdClient.getBlockHash(number.intValue());
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
