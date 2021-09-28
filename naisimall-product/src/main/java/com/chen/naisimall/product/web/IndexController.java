package com.chen.naisimall.product.web;


import com.chen.naisimall.product.entity.CategoryEntity;
import com.chen.naisimall.product.service.CategoryService;
import com.chen.naisimall.product.vo.Catelog2Vo;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author woita
 */
@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    RedissonClient redisson;

    @Autowired
    StringRedisTemplate redisTemplate;


    @ResponseBody
    @GetMapping("/hello")
    public String helle(){
        //1,获取一把锁,只要锁的名字一样,就是同一把锁
        RLock lock = redisson.getLock("my-lock");

        //2,加锁
        lock.lock();
        //1),锁的自动续期;//阻塞式等待,默认加的锁都是30s时间
        //2),加锁的业务只要运行完成,就不会给当前锁续期,即使不手动解锁,锁默认在30s以后自动删除
        lock.lock(10, TimeUnit.SECONDS);//10秒自动解锁,自动解锁时间一定要大于业务执行时间
        //问题,lock.lock(10, TimeUnit.SECONDS);锁到了时间,不会自动续期
        //1,如果我们传递了锁的超时时间,就发送给redis执行脚本,进行占锁,默认超时时间就是指定的时间
        //2,如果我们未指定锁的超时时间,就使用30*1000(lockWatchdogTimeout看门狗的默认时间 )
        //  只要占锁成功,就会启动一个定时任务(重新给锁设置过期时间,新的过期时间,就是看门狗的时间30 * 1000)每隔10s都会自动续期,续成满时间30*1000
        //  internalLockLeaseTime(看门狗时间) / 3    10s

        //最佳实战
        //1),lock.lock(10, TimeUnit.SECONDS);省掉了整个续期操作,手动解锁
        try {
            System.out.println("加锁成功..."+Thread.currentThread().getId());
            Thread.sleep(10000);
        }catch (Exception e){

        }finally {
            //3,解锁,假设解锁代码没有执行,redisson会不会出现死锁
            System.out.println("释放锁..."+Thread.currentThread().getId());
            lock.unlock();
        }
        return "Hello";
    }

    @GetMapping({"/","index.html"})
    public String indexPage(Model model){
        //TODO 1,查出所有的1级分类
        List<CategoryEntity> categoryEntitys = categoryService.getLevel1Categorys();


        //视图解析器进行拼串
        //classpath:/templates/+返回值+.html
        model.addAttribute("categorys",categoryEntitys);
        return "index";
    }
    /**
     * index/catalog.json
     */

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String,List<Catelog2Vo>> getCatelogJson(){
        Map<String, List<Catelog2Vo>> catelogJson = categoryService.getCatelogJson();
        return catelogJson;
    }

    //保证一定能读到最新数据,修改期间,写锁是一个排他锁(互斥锁,独享锁),读锁是一个共享锁
    //写锁没释放,读就必须等待
    //1,读+读:相当于无锁,并发读,只会在redis中记录好,所有当前的读锁,他们都会同时加锁成功
    //2,写+读:等待写锁释放
    //3,写+写:阻塞方式
    //4,读+写:有读锁,写也需要等待
    //只要有写的存在,都必须等待
    @GetMapping("/write")
    @ResponseBody
    public String writeValue(){
        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");

        String s = "";
        RLock rLock = lock.writeLock();
        try {
            //1,改数据加写锁,读数据加读锁
            rLock.lock();
            System.out.println("写锁加锁成功"+Thread.currentThread().getId());
            s = UUID.randomUUID().toString();
            Thread.sleep(30000);
            redisTemplate.opsForValue().set("writeValue",s);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            rLock.unlock();
            System.out.println("写锁释放"+Thread.currentThread().getId());
        }
        return s;
    }

    @GetMapping("/read")
    @ResponseBody
    public String readValue(){
        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");
        String s = "";
        //加读锁
        RLock rLock = lock.readLock();
        rLock.lock();
        System.out.println("读锁加锁成功"+Thread.currentThread().getId());
        try {
            s = redisTemplate.opsForValue().get("writeValue");
            Thread.sleep(30000);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            rLock.unlock();
            System.out.println("读锁释放"+Thread.currentThread().getId());
        }
        return s;
    }

    /**
     * 车库停车
     * 信号量也可以作为分布式限流
     */
    @GetMapping("/park")
    @ResponseBody
    public String pary() throws InterruptedException {
        RSemaphore park = redisson.getSemaphore("park");
        //park.acquire();
        boolean b = park.tryAcquire();
        return "ok==>"+b;
    }

    @GetMapping("/go")
    @ResponseBody
    public String go() throws InterruptedException {
        RSemaphore park = redisson.getSemaphore("park");
        park.release();//释放一个车位
        return "ok";
    }


    /**
     * 放假锁门
     * 所有班级都走完,才可以锁大门
     */
    @GetMapping("/lockDoor")
    @ResponseBody
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.trySetCount(5);
        door.await();//等待闭锁都完成

        return "放假了";
    }

    @GetMapping("/gogogo/{id}")
    @ResponseBody
    public String gogogo(@PathVariable("id")Long id){

        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.countDown();//计数减1
        return id+"班的人都走了";
    }

}
