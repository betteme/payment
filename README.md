# 接口约束

**正式环境-接口前缀：** 
- `https://payapi.atcc.pro/`

**测试环境-接口前缀：** 
- `https://tpayapi.atcc.pro/`


**接口数据格式：** 
- `数据交互格式默认为:application/json`

**接口返回状态码：** 
接口通过http状态码定义接口的处理结果，常用状态码如下:
- `200---代表请求正确`
- `400---代表错误请求，如参数缺失、格式错误等`
- `401---代表未授权`
- `403---代表解决访问`
- `500---内部错误`

# 接口签名

**签名生成数据部分：** 
- `Body：从request inputstream中获取保存为String形式`
String body=IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8)

- `Parameter：按照key=values（多个value按照字典顺序拼接）字典顺序进行拼接`
String params = request.getParameterMap()

- `Url：不带请求头的uri`

- `apiSecret：平台为接入商家分配的应用秘钥`

如果存在多种数据形式，则按照body、parameter、Url的顺序进行再拼接，得到所有数据的拼接值。


**生成签名示例(java)：** 

```java 
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
```


**接口调用：** 
  请求head传递X-Auth-Key、X-Auth-Sign
- `X-Auth-Key：平台为接入商家分配的应用秘钥`
- `X-Auth-Sign：上述签名规则生成的签名串`



# 测试币申请渠道

**ETH测试币申请：** 
- `https://faucet.ropsten.be/`

**ERC20测试币申请：** 
- `联系平台`

**BTC测试币申请：** 
- `https://tbtc.bitaps.com/`

**USDT测试币申请：** 
- `通过发送BTC到moneyqMan7uh8FqdCA2BV5yZ8qVrc9ikLP获取`


# 测试区块浏览

**ETH：** 
- `https://ropsten.etherscan.io/`

**BTC：** 
- `https://tbtc.bitaps.com/`




# 正式区块浏览

**ETH：** 
- `https://etherscan.io/`

**BTC：** 
- `https://www.blockchain.com/explorer?currency=BTC`

**USDT：** 
- `https://www.omniexplorer.info/`




# 业务接口

# 创建钱包地址
    
**简要描述：** 

- 创建钱包地址

**请求URL：** 
- ` /v1/addresses `
  
**请求方式：**
- POST 

**请求参数：** 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|coinCode  |是  |string  |币种代码
|userId  |是  |string  |商家平台用户标识

**请求示例：** 
``` 
{
	"coinCode": "eth",
	"userId": "001"
}
```

 **返回参数说明：** 

|参数名|类型|说明|
|:-----  |:-----|-----                           |
|address |string   |钱包地址  |

 **返回示例：**

``` 
{
    "address": "0xb96775a20168e28a018a6e3b9e876377c9cee2a8"
}

```



# 1.2 校验钱包地址是否可用

    
**简要描述：** 

-  校验地址是否可用

**请求URL：** 
- ` /v1/addresses/validate `
  
**请求方式：**
- GET 

**请求参数：** 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|coinCode  |是  |string  |币种代码
|address  |是  |string  |钱包地址

**请求示例：** 
``` 
code=eth&address=0xfdba8c3140e8a390f20080dd17d1a5f5e70cd979
```

 **返回参数说明：** 

|参数名|类型|说明|
|:-----  |:-----|-----                           |
|isValid |boolean   |true可用 false不可用  |

 **返回示例：**

``` 
{
    "isValid": true
}

```

# 1.3 提现

    
**简要描述：** 

- 提现到指定地址

**请求URL：** 
- ` /v1/withdraws `
  
**请求方式：**
- POST 

**请求参数：** 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|coinCode  |是  |string  |币种代码
|amount  |是  |double  |提现金额
|address  |是  |string  |提现地址
|memo  |否  |string  |提现备注(eos提现填tag)

**请求示例：** 
``` 
{
	"coinCode": "eth",
	"amount": 10.0,
	"address": "0xb96775a20168e28a018a6e3b9e876377c9cee21s"
}
```

 **返回参数说明：** 

|参数名|类型|说明|
|:-----  |:-----|-----                           |
|orderNo |string   |托管平台订单号(托管平台提现回调会传此参数)  |

 **返回示例：**

``` 
{
    "orderNo": "TX201904102107287788"
}
```



# 1.4 查询所有币种资产

    
**简要描述：** 

- 查询所有币种资产

**请求URL：** 
- ` /v1/assets `
  
**请求方式：**
- GET 

**请求参数：** 

|参数名|必选|类型|说明|
``` 
无
```

**请求示例：** 
``` 
无
```

 **返回参数说明：** 

|参数名|类型|说明|
|:-----  |:-----|-----                           |
|coinCode |string   |币种代码  |
|availBalance |double   |可用余额  |
|freeAmount |double   |冻结金额  |

 **返回示例：**

``` 
[
    {
        "coinCode": "eth",
        "availBalance": 90,
        "freeAmount": 10
    },
    {
        "coinCode": "btc",
        "availBalance": 0,
        "freeAmount": 0
    },
    {
        "coinCode": "usdt",
        "availBalance": 0,
        "freeAmount": 0
    }
]
```



# 1.5 查询单个币种资产

    
**简要描述：** 

- 查询单个币种资产

**请求URL：** 
- ` /v1/assets/{coinCode} `
  
**请求方式：**
- GET 

**请求参数：** 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|coinCode  |是  |string  |币种代码

**请求示例：** 
``` 
参数传递在url上
```

 **返回参数说明：** 

|参数名|类型|说明|
|:-----  |:-----|-----                           |
|coinCode |string   |币种代码  |
|availBalance |double   |可用余额  |
|freeAmount |double   |冻结金额  |

 **返回示例：**

``` 
{
    "coinCode": "eth",
    "availBalance": 90,
    "freeAmount": 10
}
```






# 1.6 查询充值列表

    
**简要描述：** 

- 分页查询充值列表

**请求URL：** 
- ` /v1/recharges `
  
**请求方式：**
- GET 

**请求参数：** 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|offset  |是  |int  |当前页 0开始
|limit  |是  |int  |每页查询条数
|status  |否  |int  |充值状态(0待确认 1已确认 2充值成功)
|beginTime  |否  |date  |开始时间(格式yyyy-mm-dd)
|endTime  |否  |date  |结束时间(格式yyyy-mm-dd)

**请求示例：** 
``` 
?status=0&beginTime=2019-04-01&endTime=2019-04-05
```

 **返回参数说明：** 

|参数名|类型|说明|
|:-----  |:-----|-----                           |
|orderNo |string   |托管平台订单号  |
|coinCode |string   |充值币种  |
|address |string   |充值地址  |
|amount |double   |充值金额  |
|status |int   |充值状态(0待节点确认 1节点已确认 2充值成功)  |
|createTime |date   |充值时间  |

 **返回示例：**

``` 
{
    "recordsTotal": 4,
    "data": [
        {
            "orderNo": "CZ201904071321035151",
            "coinCode": "usdt",
            "address": "myGRpVUTgY2Mv3S4MyoyzdiwqDSDx3XLBp",
            "amount": 0.01,
            "status": 2,
            "createTime": "2019-04-07 13:21:03"
        },
        {
            "orderNo": "CZ201903252315445140",
            "coinCode": "eth",
            "address": "0xf298173431cf9a4e0189f04ddebefabeac8d3d40",
            "amount": 1,
            "status": 1,
            "createTime": "2019-03-25 23:15:44"
        }
    ]
}
```




# 1.7 查询提现列表

    
**简要描述：** 

- 分页查询提现列表

**请求URL：** 
- ` /v1/withdraws `
  
**请求方式：**
- GET 

**请求参数：** 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|offset  |是  |int  |当前页 0开始
|limit  |是  |int  |每页查询条数
|status  |否  |int  |提现状态(0待确认 1已确认 2提现成功)
|beginTime  |否  |date  |开始时间(格式yyyy-mm-dd)
|endTime  |否  |date  |结束时间(格式yyyy-mm-dd)

**请求示例：** 
``` 
?status=0&beginTime=2019-04-01&endTime=2019-04-05
```

 **返回参数说明：** 

|参数名|类型|说明|
|:-----  |:-----|-----                           |
|orderNo |string   |托管平台订单号  |
|coinCode |string   |提现币种  |
|toAddress |string   |提现地址  |
|amount |double   |提现金额  |
|status |int   |提现状态(0待审核 1待节点确认 2节点已确认 3提现成功 4审核拒绝)  |
|createTime |date   |提现时间  |

 **返回示例：**

``` 
{
    "recordsTotal": 15,
    "data": [
        {
            "orderNo": "TX201904102107287788",
            "coinCode": "eth",
            "toAddress": "0xb96775a20168e28a018a6e3b9e876377c9cee21s",
            "amount": 10,
            "charge": 0.1,
            "status": 0,
            "createTime": "2019-04-10 21:07:28"
        },
        {
            "orderNo": "TX201904071236145275",
            "coinCode": "usdt",
            "toAddress": "myGRpVUTgY2Mv3S4MyoyzdiwqDSDx3XLBp",
            "amount": 0.01,
            "charge": 0.00000257,
            "status": 2,
            "createTime": "2019-04-07 12:36:14"
        }
    ]
}
```




# 1.8 充值回调

    
**简要描述：** 

- 充值成功后托管平台回调商家地址

**请求URL：** 
- ` 商家配置的充值回调地址 `

**签名：** 
- ` 回调采取同样的签名方式[签名的url用回调地址全路径]，在header传送X-Auth-Sign `
  
**请求方式：**
- POST 

**请求参数：** 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|orderNo  |是  |string  |托管平台订单号
|address  |是  |string  |收账地址
|amount  |是  |double  |充值金额
|txHash  |是  |date  |交易hash

**请求示例：** 
``` 
{
	"orderNo": "CZ201903252315445140",
	"address": "0xf298173431cf9a4e0189f04ddebefabeac8d3d40",
	"amount": 1.0,
	"txHash": "0x05819d768d454a794f7e0b75e9873cb3693f262fe638ee718e1d086fcdaa4b2d"
}
```

 **返回参数说明：** 

|参数名|类型|说明|
|:-----  |:-----|-----                           |
|code |string   |状态码 200代表回调成功  |
|message |string   |回调失败消息  |

 **返回示例：**

``` 
{
    "code": "200",
    "message":""
}
```




# 1.9 提现回调

    
**简要描述：** 

- 提现成功后托管平台回调商家地址

**请求URL：** 
- ` 商家配置的提现回调地址 `

**签名：** 
- ` 回调采取同样的签名方式[签名的url用回调地址全路径]，在header传送X-Auth-Sign `
  
**请求方式：**
- POST 

**请求参数：** 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|orderNo  |是  |string  |托管平台订单号

**请求示例：** 
``` 
{
	"orderNo": "CZ201903252315445140"
}
```

 **返回参数说明：** 

|参数名|类型|说明|
|:-----  |:-----|-----                           |
|code |string   |状态码 200代表回调成功  |
|message |string   |回调失败消息  |

 **返回示例：**

``` 
{
    "code": "200",
    "message":""
}
```

