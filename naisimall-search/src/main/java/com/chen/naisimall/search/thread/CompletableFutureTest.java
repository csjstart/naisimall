package com.chen.naisimall.search.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureTest {
    //创建线程池,不建议使用该方法创建,本文主要是讲解CompletableFuture,线程池部分不详细介绍
    public static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main方法开始。。。");
//        CompletableFuture.runAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//        },executor);//使用我们自己的线程池

        //只要启动异步任务，都会返回一个CompletableFuture
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 0;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, executor).whenComplete((res,exception) -> {
//            //虽然能得到异常信息，但是没法修改返回数据
//            System.out.println("异步任务成功完成了。。。结果是：" + res + "异常是：" + exception);
//        }).exceptionally(throwable -> {
//            //可以感知异常，同时返回默认值
//            return 10;
//        });

        //方法执行完成后的处理
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, executor).handle((res,exec) -> {
//            if (res != null){
//                return res * 4;
//            }
//            if (exec != null){
//                return 0;
//            }
//            return 0;
//        });

//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, executor).thenApplyAsync(res -> {
//            return "Hello" + res;
//        }, executor);


//        CompletableFuture<Object> future01 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务1线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("任务1结束：");
//            return i;
//        }, executor);
//        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务2线程：" + Thread.currentThread().getId());
//            try {
//                Thread.sleep(3000);
//                System.out.println("任务2结束：");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return "hello";
//        }, executor);
//        CompletableFuture<String> future = future01.applyToEitherAsync(future02, res -> {
//            System.out.println("任务3开始。。。之前的结果:" + res);
//            return res.toString() + "===>很爱很爱你";
//        }, executor);
        CompletableFuture<String> future01 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("上厕所");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "蹲马桶";
        }, executor);

        CompletableFuture<String> future02 = CompletableFuture.supplyAsync(() -> {
            System.out.println("刷抖音");
            return "看小姐姐跳舞";
        }, executor);

        CompletableFuture<String> future03 = CompletableFuture.supplyAsync(() -> {
            System.out.println("抽烟");
            return "吸芙蓉王";
        }, executor);

        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(future01, future02, future03);

        //获取异步任务的返回值
        System.out.println("main方法结束。。。" + anyOf.get());
        /**
         * 运行结果
         * main方法开始。。。
         * 刷抖音
         * main方法结束。。。看小姐姐跳舞
         * 抽烟
         */

    }
}
