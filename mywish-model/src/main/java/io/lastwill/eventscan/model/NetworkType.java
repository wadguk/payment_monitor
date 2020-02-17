package io.lastwill.eventscan.model;

import lombok.Getter;

@Getter
public enum NetworkType {
    DAPS_MAINNET(NetworkProviderType.DAPS),
    DUC_MAINNET(NetworkProviderType.DUC),
    DUC_SAVE(NetworkProviderType.DUC),
    BTC_MAINNET(NetworkProviderType.BTC),
    BTC_TESTNET_3(NetworkProviderType.BTC),
    ETHEREUM_MAINNET(NetworkProviderType.WEB3),
    ETHEREUM_ROPSTEN(NetworkProviderType.WEB3),
    LTC_MAINNET(NetworkProviderType.LTC),
    BCH_MAINNET(NetworkProviderType.BCH),
;

    public final static String DUC_MAINNET_VALUE = "DUC_MAINNET";
    public final static String DUC_SAVE_VALUE = "DUC_SAVE";
    public final static String BTC_MAINNET_VALUE = "BTC_MAINNET";
    public final static String BTC_TESTNET_3_VALUE = "BTC_TESTNET_3";
    public final static String ETHEREUM_MAINNET_VALUE = "ETHEREUM_MAINNET";
    public final static String ETHEREUM_ROPSTEN_VALUE = "ETHEREUM_ROPSTEN";
    public final static String LTC_MAINNET_VALUE = "LTC_MAINNET";
    public final static String BCH_MAINNET_VALUE = "BCH_MAINNET";

    private final NetworkProviderType networkProviderType;

    NetworkType(NetworkProviderType networkProviderType) {
        this.networkProviderType = networkProviderType;
    }

}
