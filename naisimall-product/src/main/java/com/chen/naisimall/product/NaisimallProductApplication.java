package com.chen.naisimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.spring.session.config.EnableRedissonHttpSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.HashMap;
import java.util.Map;

/**
 * @author woita
 * 1,整合Mybatis-Plus
 * 1),导入依赖
 * 2),配置
 * 1,配置数据源
 * 1),导入数据库的驱动
 * 2)在application.yml配置数据源相关信息
 * <p>
 * 2,配置Mybatis-Plus;
 * 1),使用@MapperScan
 * 2),告诉Mybatis-Plus,sql映射文件位置
 * 2,逻辑删除
 * 1,配置全局的逻辑删除规则(省略)
 * 2,配置逻辑删除的组件bean(可以省略)
 * 3,给bean加上逻辑删除注解@TableLogic
 * 3,JSR303
 * 1),给Bean添加校验注解:javax.validation.constraints 并定义自己的message提示
 * 2),开启校验功能@Valid
 * 效果:校验错误以后会有默认的响应
 * 3),给校验的bean后紧跟一个BindingResult,就可以获取到校验的结果
 * 4),分组校验(多场景的复杂场景)
 * 1),    @NotBlank(message = "品牌名必须提交",groups = {AddGroup.class,UpdateGroup.class})
 * 给校验注解标注什么情况需要进行校验
 * 2),Controller方法上添加  @Validated({AddGroup.class})
 * 3),默认没有指定分组的校验注解@NotBlank,在分组校验情况(@Validated({AddGroup.class}))下不生效,只会在@Validated生效;
 * 5),自定义校验
 * 1),编写一个自定义的校验注解
 * 2),编写一个自定义的校验器  ConstraintValidator
 * 3),关联自定义的校验器和自定义的校验注解
 * @Documented
 * @Constraint(validatedBy = { ListValueConstraintValidator.class【可以指定多个不同的校验器，适配不同类型的校验】)
 * @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
 * @Retention(RUNTIME) public @interface ListValue {
 * <p>
 * 4,统一的异常处理
 * @ControllerAdvice 1),编写异常处理类,使用@ControllerAdvice
 * 2),使用@ExceptionHandler标注方法可以处理的异常
 * 5,模板引擎
 *      1),thymeleaf-starter:关闭缓存
 *      2),静态资源都放在static文件夹下,就可以按照路径直接访问
 *      3),页面放在templates下,直接访问
 *          SpringBoot,访问项目的时候,默认会找index
 *      4)页面修改不重启服务器实时更新
 *          1),引入dev-tools
 *          2),修改完页面controller +shift + F9
 * 6,整合Redis
 *      1),引入data-redis-starter
 *      2),简单配置redis的host等信息
 *      3),使用SpringBoot自动配置好的StringRedisTemplate来操作Redis
 *          redis->Map:存放数据key数据值value
 * 7,整合redisson作为分布式锁等功能框架
 *      1),引入依赖
 *      <dependency>
 *           <groupId>ord.redisson</groupId>
 *           <artifactId>redisson</artifactId>
 *           <version>3.12.0</version>
 *       </dependency>
 *      2),配置redisson
 *              MyredissonConfig给容器中配置一个RedissonClient实例即可
 *      3),使用
 *              参照文档
 * 8,整合SpringChche简化缓存开发
 *      1),引入依赖
 *          spring-boot-starter-data-redis  spring-boot-starter-cache
 *      2),写配置
 *          (1),自动配置了那些
 *              CacheAutoConfiguration会导入RedisCacheConfiguration;
 *              自动配好了缓存管理器RedisCacheManager
 *          (2),配置实用redis作为缓存
 *          spring.cache.type=redis
 *      (3),测试使用缓存
 *          @Cacheable: Triggers cache population.:触发将数据保存到缓存的操作
 *          @CacheEvict: Triggers cache eviction.:触发将数据从缓存删除的操作
 *          @CachePut: Updates the cache without interfering with the method execution.不影响方法执行更新缓存
 *          @Caching: Regroups multiple cache operations to be applied on a method.:组合以上多个操作
 *          @CacheConfig: Shares some common cache-related settings at class-level.:在类级别共享缓存的相同配置
 *          1),开启缓存功能 @EnableCaching
 *          2),只需要使用注解就能完成缓存操作
 *      4),原理
 *      CacheAutoConfiguration ->RedisCacheConfiguration ->
 *      自动配置了RedisCacheManager -> 初始化所有的缓存 -> 每个缓存决定使用什么配置
 *      -> 想改缓存的配置,只需要给容器中放一个RedisCacheConfiguration即可
 *      -> 就会应用到当前RedisCacheManager管理的所有的缓存分区中
 *
 *
 *      4,Spring-Cache的不足
 * 	1),读模式:
 * 		缓存穿透:查询一个null数据,解决:缓存空数据:cache-null-value=true
 * 		缓存击穿:大量并发进来同时查询一个正好过期的数据,解决:加锁	默认是无加锁的;sync=true(加锁,解决击穿)
 * 		缓存雪崩:大量的key同时过期,(超大型应用会有,一般没有),解决:加随机时间,加上过期时间:spring.cache.redis.time-to-live=3600000
 * 	2),写模式:(缓存与数据库一致)
 * 		1),读写加锁(读多写少)
 * 		2),引入Canal,感知MySQL的更新去更新缓存
 * 		3),读多写多,直接去数据库查询就行
 * 	总结:
 * 		常规数据(读多写少,即时性,一致性要求不高的数据),完全可以使用Spring-Cache:写模式(只要缓存的数据有过期时间就足够了)
 * 		特殊数据(想用缓存,特殊设计)
 * 	原理:
 * 		CacheManager(RedisCacheManager)->Cache(RedisCache)->Cache负责缓存的读写
 *
 */

@EnableRedisHttpSession
@MapperScan("com.chen.naisimall.product.dao")
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.chen.naisimall.product.feign")
public class NaisimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaisimallProductApplication.class, args);

        HashMap<String, String> map = new HashMap<>();
        HashMap<String, Map<String,String>> map1 = new HashMap<>();


    }

}
