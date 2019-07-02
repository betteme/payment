package com.block.server.web;

import com.block.server.eum.CoinEnum;
import com.block.server.module.PageTableResponse;
import com.block.server.utils.JsonUtil;
import com.block.server.utils.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口调用demo
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    private static final Logger log = LoggerFactory.getLogger("adminLogger");

    @Autowired
    private RestTemplate restTemplate;

    //申请的API-KEY(商户平台查看)
    private final String API_KEY = "2019041610290219";
    //分配的KEY密钥(商户平台查看)
    private final String API_SECRET = "ODQxODE0NTAyMDc0MDQ1ODcyNjM4MTU2NDM1MzMx";


    //接口前缀
    private final String API_PREFIX_URL = "https://tpayapi.atcc.pro";
    //创建钱包地址
    private final String CREATE_ADDRESS_URL = "/v1/addresses";
    //校验钱包地址是否可用
    private final String VALIDATE_ADDRESS_URL = "/v1/addresses/validate";
    //提现到指定地址
    private final String WITHDRAWS_URL = "/v1/withdraws";
    //查询币种资产
    private final String GET_ASSETS_URL = "/v1/assets";
    //分页查询充值记录
    private final String GET_RECHARGES_URL = "/v1/recharges";
    //分页查询提现记录
    private final String GET_WITHDRAWS_URL = "/v1/withdraws";

    /**
     * 创建钱包地址
     */
    @PostMapping("/createAddress")
    public void createAddress() {
        //构造请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("coinCode", CoinEnum.ETH.getCode());
        params.put("userId", "001");

        //参数转json
        String jsonParam = JsonUtil.toJson(params);

        //接口签名
        String sign = SignUtil.sign(jsonParam, null, CREATE_ADDRESS_URL, API_SECRET);

        //发送接口请求
        HttpHeaders headers = getJsonHeader(sign);//设置header
        HttpEntity<String> httpEntity = new HttpEntity(jsonParam, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(API_PREFIX_URL + CREATE_ADDRESS_URL, HttpMethod.POST, httpEntity, Map.class);
        Map<String, Object> result = responseEntity.getBody();
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            log.info("钱包地址:{}", result.get("address"));
            //TODO 根据得到的结果进行业务操作
        } else {
            //TODO 解析返回的错误消息
            log.info("错误代码:{},错误消息:{}", result.get("code"), result.get("message"));
        }
    }

    /**
     * 校验钱包地址是否可用
     */
    @GetMapping("/validateAddress")
    public void validateAddress() {
        //构造请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("coinCode", CoinEnum.ETH.getCode());
        params.put("address", "0xad55da7b7ac5952d3ff3690876ef15931fd12f57");

        //接口签名
        String sign = SignUtil.sign(null, params, VALIDATE_ADDRESS_URL, API_SECRET);

        //发送接口请求
        HttpHeaders headers = getHeader(sign); //设置header
        HttpEntity<String> httpEntity = new HttpEntity(null, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(API_PREFIX_URL + VALIDATE_ADDRESS_URL + "?coinCode={coinCode}&address={address}",
                HttpMethod.GET, httpEntity, Map.class, params);

        //解析结果
        Map<String, Object> result = responseEntity.getBody();
        log.info("是否可用:{}", result.get("isValid"));

        //TODO 根据得到的结果进行业务操作
    }

    /**
     * 提现
     */
    @PostMapping("/withdraw")
    public void withdraw() {
        //构造请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("coinCode", CoinEnum.ETH.getCode());
        params.put("amount", BigDecimal.valueOf(0.01d));
        params.put("address", "0xad55da7b7ac5952d3ff3690876ef15931fd12f57");

        //参数转json
        String jsonParam = JsonUtil.toJson(params);

        //接口签名
        String sign = SignUtil.sign(jsonParam, null, WITHDRAWS_URL, API_SECRET);

        //发送接口请求
        HttpHeaders headers = getJsonHeader(sign);//设置header
        HttpEntity<String> httpEntity = new HttpEntity(jsonParam, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(API_PREFIX_URL + WITHDRAWS_URL, HttpMethod.POST, httpEntity, Map.class);

        //解析结果
        Map<String, Object> result = responseEntity.getBody();
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            log.info("平台提现订单号:{}", result.get("orderNo"));
            //TODO 根据得到的结果进行业务操作,商家业务系统提现后要保存平台返回的提现定单号，平台会根据此订单号进行回调
        } else {
            //TODO 解析返回的错误消息
            log.info("错误代码:{},错误消息:{}", result.get("code"), result.get("message"));
        }
    }


    /**
     * 查询所有币种资产
     */
    @GetMapping("/assets")
    public void assets() {
        //接口签名
        String sign = SignUtil.sign(null, null, GET_ASSETS_URL, API_SECRET);

        //发送接口请求
        HttpHeaders headers = getHeader(sign); //设置header
        HttpEntity<String> httpEntity = new HttpEntity(null, headers);
        ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(API_PREFIX_URL + GET_ASSETS_URL,
                HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<Map<String, Object>>>() {
                });

        //解析结果
        List<Map<String, Object>> result = responseEntity.getBody();
        result.forEach(o -> {
            log.info("币种代码:{},可用余额:{},冻结金额:{}", o.get("coinCode"), o.get("availBalance"), o.get("freeAmount"));
        });

        //TODO 根据得到的结果进行业务操作
    }

    /**
     * 查询单个币种资产
     */
    @GetMapping("/assetDetail")
    public void assetDetail() {
        String url = GET_ASSETS_URL + "/" + CoinEnum.ETH.getCode();
        //接口签名
        String sign = SignUtil.sign(null, null, url, API_SECRET);

        //发送接口请求
        HttpHeaders headers = getHeader(sign); //设置header
        HttpEntity<String> httpEntity = new HttpEntity(null, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(API_PREFIX_URL + url,
                HttpMethod.GET, httpEntity, Map.class);

        //解析结果
        Map<String, Object> result = responseEntity.getBody();
        log.info("币种代码:{},可用余额:{},冻结金额:{}", result.get("coinCode"), result.get("availBalance"), result.get("freeAmount"));

        //TODO 根据得到的结果进行业务操作
    }


    /**
     * 分页查询充值记录
     */
    @GetMapping("/recharges")
    public void recharges() {
        //构造请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("offset", 0);
        params.put("limit", 10);

        //接口签名
        String sign = SignUtil.sign(null, params, GET_RECHARGES_URL, API_SECRET);

        //发送接口请求
        HttpHeaders headers = getHeader(sign); //设置header
        HttpEntity<String> httpEntity = new HttpEntity(null, headers);
        ResponseEntity<PageTableResponse> responseEntity = restTemplate.exchange(API_PREFIX_URL + GET_RECHARGES_URL + "?offset={offset}&limit={limit}",
                HttpMethod.GET, httpEntity, PageTableResponse.class, params);

        //解析结果
        PageTableResponse pageTableResponse = responseEntity.getBody();
        List<Map<String, Object>> result = (List<Map<String, Object>>) pageTableResponse.getData();
        result.forEach(o -> {
            log.info("订单号:{},充值币种:{},充值地址:{},充值金额:{},充值状态:{},充值时间:{}",
                    o.get("orderNo"), o.get("coinCode"), o.get("address"), o.get("amount"), o.get("status"), o.get("createTime"));
        });

        //TODO 根据得到的结果进行业务操作
    }

    /**
     * 分页查询提现记录
     */
    @GetMapping("/withdraws")
    public void withdraws() {
        //构造请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("offset", 0);
        params.put("limit", 10);

        //接口签名
        String sign = SignUtil.sign(null, params, GET_WITHDRAWS_URL, API_SECRET);

        //发送接口请求
        HttpHeaders headers = getHeader(sign); //设置header
        HttpEntity<String> httpEntity = new HttpEntity(null, headers);
        ResponseEntity<PageTableResponse> responseEntity = restTemplate.exchange(API_PREFIX_URL + GET_WITHDRAWS_URL + "?offset={offset}&limit={limit}",
                HttpMethod.GET, httpEntity, PageTableResponse.class, params);

        //解析结果
        PageTableResponse pageTableResponse = responseEntity.getBody();
        List<Map<String, Object>> result = (List<Map<String, Object>>) pageTableResponse.getData();
        result.forEach(o -> {
            log.info("订单号:{},提现币种:{},提现地址:{},提现金额:{},交易手续费:{},提现状态:{},提现时间:{}",
                    o.get("orderNo"), o.get("coinCode"), o.get("address"), o.get("amount"), o.get("charge"), o.get("status"), o.get("createTime"));
        });

        //TODO 根据得到的结果进行业务操作
    }

    /**
     * 构造请求header，支持传json
     *
     * @param sign
     * @return
     */
    private HttpHeaders getJsonHeader(String sign) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Sign", sign);
        headers.add("X-Auth-Key", API_KEY);
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return headers;
    }

    private HttpHeaders getHeader(String sign) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Sign", sign);
        headers.add("X-Auth-Key", API_KEY);
        return headers;
    }
}

