package com.chen.naisimall.search.thread;

import com.chen.common.to.SpuBoundTo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {

    public static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main...start....");
//        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//            System.out.println("当前线程:" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果:" + i);
//        }, executor);
            //方法完成后的感知
//            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//                System.out.println("当前线程:" + Thread.currentThread().getId());
//                int i = 10 / 0;
//                System.out.println("运行结果:" + i);
//                return i;
//            }, executor).whenComplete((res,exception) -> {
                    //仍然能得到异常信息
//                System.out.println("异步任务成功完成...结果是:"+res+"异常是:"+exception);
//            }).exceptionally(throwable -> {
            //可以感知异常,同时返回默认值
//                return 10;
//            });
            //
            //方法执行完成后的处理
//            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//                System.out.println("当前线程:" + Thread.currentThread().getId());
//                int i = 10 / 3;
//                System.out.println("运行结果:" + i);
//                return i;
//            }, executor).handle((res,exception) -> {
//                if (res != null) {
//                    return res * 2;
//                }
//                if (exception != null){
//                    return 0;
//                }
//                return 0;
//            });
            /**
             * 线程串行化
             * 1),thenRun:不能提取到上一步的执行结果,无返回值
             *              .thenRunAsync(() -> {
             *                  System.out.println("任务2启动了");
             *              }, executor)
             * 2),能接受上一步结果,但是无返回值
             *      thenAcceptAsync
             * 3),能接受上一步结果,有返回值
             *      thenApplyAsync
             */
//            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//                System.out.println("当前线程:" + Thread.currentThread().getId());
//                int i = 10 / 3;
//                System.out.println("运行结果:" + i);
//                return i;
//            }, executor).thenApplyAsync(res -> {
//                System.out.println("任务2启动了");
//                return res * 10;
//            }, executor);

//            CompletableFuture<Object> future01 = CompletableFuture.supplyAsync(() -> {
//                System.out.println("任务一线程:" + Thread.currentThread().getId());
//                int i = 10 / 3;
//                System.out.println("任务一结束:" + i);
//                return i;
//            }, executor);
//            CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
//
//                System.out.println("任务二线程:" + Thread.currentThread().getId());
//                try {
//                    Thread.sleep(3000);
//                    System.out.println("任务二结束:" + Thread.currentThread().getId());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return "hello";
//            }, executor);
//            future01.runAfterBothAsync(future02,() -> {
//                System.out.println("任务三开始...");
//            },executor);
//            future01.thenAcceptBothAsync(future02,(f1,f2) -> {
//                System.out.println("任务三开始,之前的结果:"+f1+"===>"+f2);
//            },executor);
//        CompletableFuture<String> future = future01.thenCombineAsync(future02, (f1, f2) -> {
//            return f1 + ":--->" + f2 + "--->world";
//        }, executor);
        /**
         * 两个任务,只要有一个成功,我们就执行任务3
         * runAfterEitherAsync:不感知结果,自己也没有返回值
         */
//        future01.runAfterEitherAsync(future02,() -> {
//            System.out.println("任务三开始,之前的结果:");
//        },executor);
        /**
         * acceptEitherAsync:感知结果,自己没有返回值
         */
//        future01.acceptEitherAsync(future02,(res)->{
//            System.out.println("任务三开始,之前的结果:" + res);
//        },executor);

//        CompletableFuture<String> future = future01.applyToEitherAsync(future02, (res) -> {
//            System.out.println("任务三开始,之前的结果:" + res);
//            return res + "hello world";
//        }, executor);

        CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的图片信息");
            return "heool.jpg";
        },executor);
        CompletableFuture<String> futureAttr = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的属性信息");
            return "黑色";
        },executor);
        CompletableFuture<String> futureBrand = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品品牌");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "华为";
        },executor);
//        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureImg, futureAttr, futureBrand);
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(futureImg, futureAttr, futureBrand);
        //allOf.get();//等待所有完成

        System.out.println("main...end...."+anyOf.get());
    }
}
