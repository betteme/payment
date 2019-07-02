package com.block.server.eum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 币种枚举
 */
@Getter
@AllArgsConstructor
public enum CoinEnum {
    ETH("eth", "以太币"),
    BTC("btc", "比特币"),
    USDT("usdt", "泰达币");

    private String code;
    private String name;
}
