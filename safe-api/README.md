
参考[这里](https://cloud.tencent.com/developer/article/1546336)


## 接口说明

根据接口所具有的不同权限，可以把接口分为三类：

1. 不需要登录的接口。例如，获取验证码的接口，申请注册的接口。
2. 登录接口。即使用用户名和密码进行登录的接口。
3. 需要登录之后才能使用的接口。如，获取本人的用户信息等。

这些接口可以通过 WebMvcConfiguration 中的拦截器，进行分类处理。 

需要登录以及登录之后才可以使用的接口，统一添加到【addPathPatterns方法】，框架会把这些接口交给 tokenInterceptor 处理： 

当客户端请求url时，框架会先判断url是否在addPathPatterns方法中，如果存在，就调用 tokenInterceptor 来进行预处理（即preHandle）；否则，就直接放行，不做任何处理。

## 请求参数说明

### 不需要登录的接口

在不需要登录的接口中，也不是不需要任何参数就可以访问，不需要任何参数就可以访问，这就无法区分正常用户与黑客。
所以一定要传入参数，这里一般需要传入 appId 和 appkey（aka， secret），二者是配对出现的。

appId： 一般是开放平台分配给每一个客户端或开发者的唯一标识，用来识别调用方身份。
appkey 或 secret：与 appId 配对的密钥，只在服务器端保存，用于生成/校验签名。

调用 unrequireLogin() 方法，来模拟客户端组装生成请求：

```text
### 不需要登录的接口
POST http://localhost:9003/api/token/api_token
Content-Type: application/x-www-form-urlencoded
timestamp: 1749791901262
sign: 0862e501969a46406daf27ae96be615b

appId = 1
```

响应结果

```json
{
  "result": {
    "code": "10000",
    "msg": "success"
  },
  "data": {
    "token": "ddc2a075-3187-4ea5-b31b-8450285c9ffc",
    "expireTime": 1749789497090
  },
  "sign": "30a1d367fc3f36d839ee8902d0f9a51d"
}
```


返回结果中包含了两部分内容，一是 token ，这里的token有点类似于accessToken的概念，可以保存在redis中，并且要求设置过期时间，这样一来，既能表明客户端的身份，又具有时效性。

二是sign，这里的签名主要用来校验服务端给客户端返回数据的过程中，数据是否被篡改。

也就是说，客户端拿到响应数据之后，会对响应数据进行签名，然后拿着自己的签名数据与服务端返回的进行对比，如果一致，就表明收发过程中，数据没有被篡改；否则，就表明响应数据被篡改了。

#### 对 appkey 的说明

上面说只保存在服务端，用于生成和校验签名，那么就涉及到另外一个问题，那就是： 既然只能保存在客户端，那么客户端是如何利用appkey来生成和校验签名呢？

有两种处理办法，针对安全性要求不太高的接口，实际上客户端是知道appkey的；对于安全性要求极高的接口，生成签名和校验签名都是经过服务端接口来搞定的。

也就是说客户端要想生成签名，就必须要发起接口调用，服务端接口会返回签名。校验签名同理。

> 这里有点像HTTPS加解密过程中对非对称加密的私钥的传输过程。

#### sign的使用

sign: 一般用于参数签名，防止参数被非法篡改，最常见的是修改金额等重要敏感参数， 
sign的值一般是将所有非空参数按照升续排序然后+token+key+timestamp+nonce(随机数)拼接在一起，
然后使用某种加密算法进行加密，作为接口中的一个参数sign来传递，也可以将sign放到请求头中。接口在网络传输过程中如果被黑客挟持，
并修改其中的参数值，然后再继续调用接口，虽然参数的值被修改了，但是因为黑客不知道sign是如何计算出来的，
不知道sign都有哪些值构成，不知道以怎样的顺序拼接在一起的，最重要的是不知道签名字符串中的key是什么，所以黑客可以篡改参数的值，
但没法修改sign的值，当服务器调用接口前会按照sign的规则重新计算出sign的值然后和接口传递的sign参数的值做比较，
如果相等表示参数值没有被篡改，如果不等，表示参数被非法篡改了，就不执行接口了。

简而言之，生成过程，一般情况下是对所有参数先进行排序，之后再加上一些特殊的参数（如客户端生成的随机数和时间戳，服务端保存的appkey等），最后对形成的字符串进行md5加密。

timestamp: 时间戳，是客户端调用接口时对应的当前时间戳，时间戳用于防止DoS攻击。当黑客劫持了请求的url去DoS攻击，
每次调用接口时接口都会判断服务器当前系统时间和接口中传的的timestamp的差值，如果这个差值超过某个设置的时间(假如5分钟)，那么这个请求将被拦截掉，
如果在设置的超时时间范围内，是不能阻止DoS攻击的。timestamp机制只能减轻DoS攻击的时间，缩短攻击时间。如果黑客修改了时间戳的值可通过sign签名机制来处理。

nonce：随机值，是客户端随机生成的值，作为参数传递过来，随机值的**目的是增加sign签名的多变性**。随机值一般是数字和字母的组合，6位长度，随机值的组成和长度没有固定规则。

### 登录接口

登录接口，除了要使用 username 和 password 等基本的用户信息外，还需要借助 【不需要登录的接口】的响应结果中的 token（aka， accesstoken）。

> 能不能不借助【不需要登录的接口】的响应结果中的token（aka， accesstoken） 呢？ 不能，因为不借助这个接口，就无法得知，发起登录请求的用户的身份， 
> 也就是说，如果不借助这个接口，那么任何人都可以登录，那么登录接口就不安全了。

本质上是利用了 accessToken 的几个特点：

1. 表明了请求发起方的身份；
2. 具有时效性；

调用 login(String accessToken) 来模拟客户端组装请求的过程：

```text
### 登录接口
POST http://localhost:9003/api/token/user_token
Content-Type: application/x-www-form-urlencoded
nonce: A1scr6
timestamp: 1749791929893
token: 75daa32d-b300-4fb0-abf0-70e0be097b3f
sign: d7c7cba0970613a84864b3e99a50faa2

username = 1 &
password = 123456
```

登录接口的其它**请求头参数**：

1. nonc，随机数
2. timestamp， 时间戳
3. token， 【不需要登录的接口】的响应结果中的 token（aka， accesstoken）
4. sign， 对上面三个参数进行的签名；

响应结果：

```json
{
  "result": {
    "code": "10000",
    "msg": "success"
  },
  "data": {
    "username": "1",
    "mobile": null,
    "email": null,
    "password": "81255cb0dca1a5f304328a70ac85dcbd",
    "salt": "111111",
    "accessToken": {
      "token": "65014ed6-8208-409d-81a0-a612c234ca93",
      "expireTime": 1749799149263
    }
  },
  "sign": "6686cfcb4e6fe92f2f04a6b07e5c624f"
}
```

### 需要登录之后才能使用的接口

例如下面的不需要登录的业务接口：

调用 shouldLogin(String utoken) 方法，来模拟客户端组装请求：

```text
### 需要登录之后才可以访问的接口
GET http://localhost:9003/test/test
Content-Type: application/x-www-form-urlencoded
nonce: A1scr6
timestamp: 1749792504736
token: 65014ed6-8208-409d-81a0-a612c234ca93
sign: 6410b307f83e34b1de833bb16b0418b6
```

响应结果：

```json
{
  "result": {
    "code": "10000",
    "msg": "success"
  },
  "data": "sss",
  "sign": "f2898ccf462d04e19dd85f7eb7b0026d"
}
```













