# spring-boot-auth-starter
基于springbootweb的权限拦截验证

### 使用前需要的准备
* maven中央仓库地址 [其他方式集成](https://search.maven.org/artifact/com.github.liuchengts/spring-boot-auth-starter)
```
<dependency>
    <groupId>com.github.liuchengts</groupId>
    <artifactId>spring-boot-auth-starter</artifactId>
    <version>1.0.4</version>
</dependency>

<!--哈希算法实现（高位aes加密需要）-->
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-jdk15on</artifactId>
    <version>1.70</version>
</dependency>
```
* 声明一个接口来抽象具体的权限，可以继承 ```com.boot.auth.starter.common.DefaultRolesConstant``` 
* 配置 ```com.boot.auth.starter.common.AuthProperties``` 中的必要属性
* 正常配置 redis
    ```
    spring:
      redis:
        host: 127.0.0.1
        port: 6379
    ```
* 如果想启用 ``` @OperLog ``` 自定义增加一个声明即可，例如以下方式:
```
//todo 在这里自己定义日志类型
public interface OperLogConstant {

    /**
     * 业务1
     **/
    String SERVICE1 = "service1";
    /**
     * 业务2
     **/
    String SERVICE2 = "service2";
}

```
* (非必须)如果不想使用默认的日志记录方式，可以按以下方式增加自定义处理:
```
@Primary
@Service
public class LogServiceImpl extends DefaultLogServiceImpl {
    @Override
    public void addLog(OperLogAnnotationEntity logEntity) {
        log.debug("add  log :{}", logEntity);
        //todo 在这里自己定义访问日志处理的方式
    }
}
```

 * (非必须)如果不想使用默认权限拦截后的消息输出，可以按以下方式增加自定义处理:
 ```
 @Primary
 @Service
 public class OutJsonServiceImpl extends DefaultOutJsonServiceImpl {
     @Autowired
     ObjectMapper objectMapper;
 
     @Override
     public String errorOutJson(String msg, String code) {
         //todo 在这里自己定义被权限拦截后的数据返回格式
         try {
             return objectMapper.writeValueAsString("error");
         } catch (Exception e) {
             throw new AuthException(e);
         }
     }
 }
 ```
### 使用说明
* 说明：主要针对需要登录或需要访问权限的资源使用
* @Auth 权限注解（默认权限在 ```com.boot.auth.starter.common.DefaultRolesConstant```）
* @NoAuthGetSession 不强制校验权限(默认不强制校验）
* @IgnoreLogin 允许不登录(默认允许不登录）
* @OperLog 日志记录
* 在控制器方法中声明 ```Session session``` 会自动注入

### 使用示例
1、```@Auth``` 是最基本的注解，该注解可以在类或者方法上，表示需要使用权限验证模块，如果没有此注解则不会启用本模块的权限功能，如果只使用```@Auth``` 而不增加 ```roles```属性，则会应用默认的权限验证```DefaultRolesConstant.DEFAULT```；直接的体现就是进行登录验证后马上进行权限验证，通过后即可访问资源。
```
    @Auth
    @GetMapping("/auth1")
    public Session testAuth1(Session session) {
        log.info("访问到了 auth1");
        return session;
    }
```
2、```@IgnoreLogin``` 是不需要登录的注解，该注解可以在类或者方法上，即在有```@Auth```的情况下可以忽略```@Auth```的登录验证及权限设置，没有任何阻拦直接访问对应资源；直接的体现就是仿佛没有启用本模块的功能。
```
    @IgnoreLogin
    @GetMapping("/auth3")
    public Session testAuth3(Session session) {
       //由于不校验登录，所以session为空
        log.info("访问到了 auth3");
        return session;
    }
```
3、```@NoAuthGetSession``` 是不需要校验权限的注解，该注解可以在类或者方法上，即在有```@Auth```的情况下只会进行登录验证，忽略```@Auth```的权限设置，访问对应资源；直接的体现就是在方法上```session```会自动注入当前登录者的信息
```
    @NoAuthGetSession
    @GetMapping("/auth2")
    public Session testAuth2(Session session) {
        log.info("访问到了 auth2");
        return session;
    }
```
4、```@OperLog``` 日志记录，额外字段使用 ```com.boot.auth.starter.bo.RequestHeaderBO``` 中的属性写到请求的 ```header``` 中
* 用法：
```
    @OperLog(operType = OperLogConstant.SERVICE2)
    @Auth(roles = RolesConstant.USER_1)
    @GetMapping("/2")
    public Object service2(Session session) {
        log.info("访问到了 service2:{}", session);
        return session;
    }

```
#### 权限注解的使用
* 四个注解可以叠加使用，优先级(由高到低)为 @IgnoreLogin @NoAuthGetSession @Auth
* 四个注解都可以对类或方法级别生效，在方法使用注解优先级最高
* 使用示例 [spring-boot-auth-starter-example](https://github.com/liuchengts/spring-boot-auth-starter-example) 参见 com.boot.auth.example.controllers 下的部分示例

## 版本发布说明
* 1.0.0 基本的权限拦截等功能
* 1.0.1 增加自定义日志、拦截输出等功能；去掉非必须依赖的 lombok 插件(便于适应kotlin的 kpt插件编译)
* 1.0.2 修改输出json的编码为 utf-8