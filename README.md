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


