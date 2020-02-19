package io.mywish.btc.blockchain.helper;

import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;
import org.bitcoinj.params.MainNetParams;

import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkState;

public class BchNetworkParams extends MainNetParams {

    public BchNetworkParams() {
//        dumpedPrivateKeyHeader = 177;
//        bip32HeaderPub = 0x019da462; // The 4 byte header that serializes in base58 to "xpub".
//        bip32HeaderPriv = 0x019d9cfe; // The 4 byte header that serializes in base58 to "xprv"
//        packetMagic = 0xf9beb4d9;
//        addressHeader = 49;
//        p2shHeader = 51;
//        maxTarget = new BigInteger("999999999999999999999999999999999999999999999999999999999999999");
//        dumpedPrivateKeyHeader = 128;
//        addressHeader = 0;
//        p2shHeader = 5;
//        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
//        port = 8333;
//        packetMagic = 0xe3e1f3e8L;
//        bip32HeaderPub = 0x0488B21E; //The 4 byte header that serializes in base58 to "xpub".
//        bip32HeaderPriv = 0x0488ADE4; //The 4 byte header that serializes in base58 to "xprv"
//        genesisBlock.setDifficultyTarget(0x1d00ffffL);
//        genesisBlock.setTime(1231006505L);
//        genesisBlock.setNonce(2083236893);
//        subsidyDecreaseBlockCount = 210000;
//        spendableCoinbaseDepth = 100;
//        String genesisHash = genesisBlock.getHashAsString();
//        checkState(genesisHash.equals("000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f"),
//                genesisHash);
//        checkpoints.put(91722, Sha256Hash.wrap("00000000000271a2dc26e7667f8419f2e15416dc6955e5a6c6cdf3f2574dd08e"));
//        checkpoints.put(91812, Sha256Hash.wrap("00000000000af0aed4792b1acee3d966af36cf5def14935db8de83d6f9306f2f"));
//        checkpoints.put(91842, Sha256Hash.wrap("00000000000a4d0a398161ffc163c503763b1f4360639393e0e4c8e300e0caec"));
//        checkpoints.put(91880, Sha256Hash.wrap("00000000000743f190a18c5577a3c2d2a1f610ae9601ac046a38084ccb7cd721"));
//        checkpoints.put(200000, Sha256Hash.wrap("000000000000034a7dedef4a161fa058a2d67a173a90155f3a2fe6fc132e0ebf"));
//        checkpoints.put(478559, Sha256Hash.wrap("000000000000000000651ef99cb9fcbe0dadde1d424bd9f15ff20136191a5eec"));
//        dnsSeeds = new String[] {
//                "seed.bitcoinabc.org",
//                "seed-abc.bitcoinforks.org",
//                "btccash-seeder.bitcoinunlimited.info",
//                "seed.bitprim.org",
//                "seed.deadalnix.me",
//                "seeder.criptolayer.net"
//        };

    }

}
