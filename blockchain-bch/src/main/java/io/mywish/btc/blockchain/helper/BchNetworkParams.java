package io.mywish.btc.blockchain.helper;

import org.bitcoinj.params.MainNetParams;

import java.math.BigInteger;

public class BchNetworkParams extends MainNetParams {
    public BchNetworkParams() {
        dumpedPrivateKeyHeader = 176;
        bip32HeaderPub = 0x019da462; // The 4 byte header that serializes in base58 to "xpub".
        bip32HeaderPriv = 0x019d9cfe; // The 4 byte header that serializes in base58 to "xprv"
        packetMagic = 0xf9beb4d9;
        addressHeader = 48;
        p2shHeader = 50;
        maxTarget = new BigInteger("999999999999999999999999999999999999999999999999999999999999999");

    }
}