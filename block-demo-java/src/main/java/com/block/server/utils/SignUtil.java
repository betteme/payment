package com.block.server.utils;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Map;


public class SignUtil {
    /**
     * 签名
     *
     * @param body      body参数[json形式参数]
     * @param params    url|form参数[url或者表单形式传参]
     * @param uri       uri,不带主机头
     * @param apiSecret 应用秘钥
     * @return
     */
    public static String sign(String body, Map<String, Object> params, String uri, String apiSecret) {
        StringBuilder sb = new StringBuilder("");
        if (StringUtils.isNotBlank(body)) {
            sb.append(body).append("#");
        }
        if (!CollectionUtils.isEmpty(params)) {
            params.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(paramEntry -> {
                        sb.append(paramEntry.getKey()).append("=").append(paramEntry.getValue()).append("#");
                    });
        }
        if (StringUtils.isNotBlank(uri)) {
            sb.append(uri);
        }
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, apiSecret).hmacHex(sb.toString());
    }

}