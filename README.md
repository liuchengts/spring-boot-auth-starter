# spring-boot-auth-starter
基于`spring boot web`的权限拦截验证
## 重要声明
* 本分支由`1.0.7`为第一个版本，此版本默认提供`guava`存储令牌的功能
* 根据`guava`的缓存特性，提供了`2`种缓存方案(`LoadingCache、Cache`)
* 如果需要使用`redis`存储方案，参考: [四、自定义功能及开发](#jump) ；或请直接使用`1.0.5`版本（它的源代码位于`master`分支）

## 一、使用前需要的准备
* maven中央仓库地址 [其他方式集成](https://search.maven.org/artifact/com.github.liuchengts/spring-boot-auth-starter)
```
<dependency>
    <groupId>com.github.liuchengts</groupId>
    <artifactId>spring-boot-auth-starter</artifactId>
    <version>1.0.7</version>
</dependency>
```
* 声明一个接口来抽象具体的权限，可以继承 `com.boot.auth.starter.common.DefaultRolesConstant`
* 根据实际情况配置 `com.boot.auth.starter.common.AuthProperties` 中的属性

## 二、使用示例

`使用示例demo` [spring-boot-auth-starter-example](https://github.com/liuchengts/spring-boot-auth-starter-example) 
* `@Auth` 权限注解（默认权限在 ```com.boot.auth.starter.common.DefaultRolesConstant```）
* `@NoAuthGetSession` 不强制校验权限(默认不强制校验）
* `@IgnoreLogin` 允许不登录(默认允许不登录）
* `@OperLog` 日志记录
* 在控制器方法中声明 ```Session session``` 会自动注入
* 三个注解（`@OperLog`除外）可以叠加使用，优先级(由高到低)为 `@IgnoreLogin > @NoAuthGetSession > @Auth`
* 三个注解（`@OperLog`除外）都可以对类或方法级别生效，在方法使用注解优先级最高


### 1、注解`@Auth`
* `@Auth` 是最基本的注解，表示需要使用权限验证模块，该注解可以在类或者方法上
* 如果没有此注解则不会启用本模块的权限功能
* 如果只使用`@Auth` 而不增加 `roles`属性，则会应用默认的权限验证`DefaultRolesConstant.DEFAULT`
* 本注解体现效果是进行登录验证后马上进行权限验证，通过后即可访问资源
```
    // 在方法上使用，只对当前方法有效
    @Auth
    @GetMapping("/auth1")
    public Session testAuth1(Session session) {
        log.info("访问到了 auth1");
        return session;
    }
```
```
    // 在类上使用，对整个类的方法都有效，
    // 此时可以在方法上穿插使用 @NoAuthGetSession @IgnoreLogin 注解
    @Auth(roles = {RolesConstant.USER_1, RolesConstant.USER_2})
    @RestController
    @RequestMapping("/test")
    public class TestAuthController{
        
        @NoAuthGetSession
        @GetMapping("/auth1")
        public Session testAuth1(Session session) {
            log.info("访问到了 auth1");
            return session;
        }
        
        @GetMapping("/auth2")
        public Session testAuth2(Session session) {
            log.info("访问到了 auth1");
            return session;
        }
     }
```
### 2、注解`@IgnoreLogin`
* `@IgnoreLogin` 是表示不需要登录的注解，该注解可以在类或者方法上
* 即在有`@Auth`的情况下可以忽略`@Auth`的登录验证及权限设置，没有任何阻拦直接访问对应资源
* 本注解体现效果是仿佛没有启用本模块的功能。但是不能保证`session`对象是有值的
```
    @IgnoreLogin
    @GetMapping("/auth3")
    public Session testAuth3(Session session) {
       //由于不校验登录，所以session可能为空
        log.info("访问到了 auth3");
        return session;
    }
```
### 3、注解`@NoAuthGetSession`
* `@NoAuthGetSession` 表示不需要校验权限的注解，该注解可以在类或者方法上
* 即在有`@Auth`的情况下只会进行登录验证，忽略`@Auth`的权限设置，访问对应资源
* 本注解体现效果是在方法上`session`会自动注入当前登录者的信息
```
    @NoAuthGetSession
    @GetMapping("/auth2")
    public Session testAuth2(Session session) {
        log.info("访问到了 auth2");
        return session;
    }
```
### 4、注解`@OperLog`
* `@OperLog` 是用来做日志记录的
* 额外字段使用 `com.boot.auth.starter.bo.RequestHeaderBO` 中的属性写到请求的 `header` 中
* 如果想启用它，需要自定义增加一个声明
```
/**
* 在这里自己定义日志类型
  **/
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
* 然后在需要记录日志的方法上加上以下处理
```
    @OperLog(operType = OperLogConstant.SERVICE2)
    @Auth(roles = RolesConstant.USER_1)
    @GetMapping("/2")
    public Object service2(Session session) {
        log.info("访问到了 service2:{}", session);
        return session;
    }

```
* 自定义日志记录的方式（继承 `DefaultLogServiceImpl` 覆写`addLog`方法）

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

## 三、自定义输出
* (非必须)如果不想使用默认权限拦截后的消息输出，可以继承`DefaultOutJsonServiceImpl`覆写`errorOutJson`方法，增加自定义处理:
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
## <span id="jump"> 四、自定义功能及开发 </span>
- 自定义`guava`的缓存功能（使用`GuavaCacheSupport`类中对应的`set`方法）
  - 全局的`LoadingCache`的`CacheLoader`
  - 全局的缓存移除事件`RemovalListener`
- 自定义缓存（不使用`guava`）
  - 继承`DefaultCacheServiceImpl`类，覆写其所有方法


## 五、版本发布说明
* 1.0.0 基本的权限拦截等功能
* 1.0.1 增加自定义日志、拦截输出等功能；去掉非必须依赖的`lombok` 插件(便于适应`kotlin`的`kpt`插件编译)
* 1.0.2 修改输出`json`的编码为`utf-8`
* 1.0.3 ~ 1.0.5 若干`bug`修复
* 1.0.6 去掉`redis`支持，改由`CacheService`类提供自定义缓存
* 1.0.7 增加`guava`缓存实现；增加排他性的授权认证；支持自定义缓存存储方案；