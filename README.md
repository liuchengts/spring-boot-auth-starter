# spring-boot-auth-starter
基于springbootweb的权限拦截验证
### 使用前需要的准备
* 声明一个接口来抽象具体的权限，可以继承 ```com.boot.auth.starter.common.DefaultRolesConstant``` 
* 配置 ```com.boot.auth.starter.common.AuthProperties``` 中的必要属性
* 正常配置 redis
    ```
    spring:
      redis:
        host: 127.0.0.1
        port: 6379
        password: 123456
    ```
### 使用说明
* 说明：主要针对需要登录或需要访问权限的资源使用
* @Auth 权限注解（默认权限在 ```com.boot.auth.starter.common.DefaultRolesConstant```）
* @NoAuthGetSession 不强制校验权限(默认不强制校验）
* @IgnoreLogin 允许不登录(默认允许不登录）
* 在控制器方法中声明 ```Session session``` 会自动注入

#### 权限注解的使用
* 三个注解可以叠加使用，优先级(由高到低)为 @IgnoreLogin @NoAuthGetSession @Auth
* 三个注解都可以对类或方法级别生效
* 使用示例 [spring-boot-auth-starter-example](https://github.com/liuchengts/spring-boot-auth-starter-example) 参见 com.boot.auth.example.controllers 下的部分示例
