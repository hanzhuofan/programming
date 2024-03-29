package com.hzf.study.program.thread;

import lombok.SneakyThrows;

import java.util.concurrent.*;

/**
 * 异步任务
 *
 * @author zhuofan.han
 * @date 2022/1/15
 */
public class CompletableFutureTest {
    @SneakyThrows
    public static void main(String[] args) {
        CompletableFutureTest test = new CompletableFutureTest();
        /*通常的线程异步*/
//        test.test1();
        /*supplyAsync异步执行任务，有返回值*/
        test.test2();
        /*exceptionally异常回调方法*/
//        test.test3();
        /*runAsync无返回值*/
//        test.test4();
        /*thenApply异步有返回值*/
//        test.test5();
        /*thenAccept无返回值，thenRun无入参，也没有返回值*/
//        test.test6();
        /*whenComplete是当某个任务执行完成后执行的回调方法*/
//        test.test7();
        /*handle跟whenComplete基本一致，区别在于handle的回调方法有返回值，且handle方法返回的CompletableFuture的result是回调方法的执行*/
//        test.test8();
    }

    public void test1() throws Exception {
        // 创建异步执行任务:
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Double> cf = executorService.submit(() -> {
            System.out.println(Thread.currentThread() + " start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if (false) {
                throw new RuntimeException("test");
            } else {
                System.out.println(Thread.currentThread() + " exit,time->" + System.currentTimeMillis());
                return 1.2;
            }
        });
        System.out.println("main thread start,time->" + System.currentTimeMillis());
        // 等待子任务执行完成,如果已完成则直接返回结果
        // 如果执行任务异常，则get方法会把之前捕获的异常重新抛出
        System.out.println("run result->" + cf.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
        executorService.shutdown();
    }

    public void test2() throws Exception {
        // 创建异步执行任务，有返回值
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + " start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if (false) {
                throw new RuntimeException("test");
            } else {
                System.out.println(Thread.currentThread() + " exit,time->" + System.currentTimeMillis());
                return 1.2;
            }
        });
        Thread.sleep(5000);
        System.out.println("main thread start,time->" + System.currentTimeMillis());
        // 等待子任务执行完成
        System.out.println("run result->" + cf.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }

    public void test3() throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        // 创建异步执行任务:
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "job1 start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if (true) {
                throw new RuntimeException("test");
            } else {
                System.out.println(Thread.currentThread() + "job1 exit,time->" + System.currentTimeMillis());
                return 1.2;
            }
        }, pool);
        // cf执行异常时，将抛出的异常作为入参传递给回调方法
        CompletableFuture<Double> cf2 = cf.exceptionally((param) -> {
            System.out.println(Thread.currentThread() + " start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println("error stack trace->");
            param.printStackTrace();
            System.out.println(Thread.currentThread() + " exit,time->" + System.currentTimeMillis());
            return -1.1;
        });
        // cf正常执行时执行的逻辑，如果执行异常则不调用此逻辑
        CompletableFuture cf3 = cf.thenAccept((param) -> {
            System.out.println(Thread.currentThread() + "job2 start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println("param->" + param);
            System.out.println(Thread.currentThread() + "job2 exit,time->" + System.currentTimeMillis());
        });
        System.out.println("main thread start,time->" + System.currentTimeMillis());
        // 等待子任务执行完成,此处无论是job2和job3都可以实现job2退出，主线程才退出，如果是cf，则主线程不会等待job2执行完成自动退出了
        // cf2.get时，没有异常，但是依然有返回值，就是cf的返回值
        System.out.println("run result->" + cf2.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }

    public void test4() throws Exception {
        // 创建异步执行任务，无返回值
        CompletableFuture cf = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread() + " start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if (false) {
                throw new RuntimeException("test");
            } else {
                System.out.println(Thread.currentThread() + " exit,time->" + System.currentTimeMillis());
            }
        });
        System.out.println("main thread start,time->" + System.currentTimeMillis());
        // 等待子任务执行完成
        System.out.println("run result->" + cf.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }

    public void test5() throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        // 创建异步执行任务:
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + " start job1,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread() + " exit job1,time->" + System.currentTimeMillis());
            return 1.2;
        }, pool);
        // cf关联的异步任务的返回值作为方法入参，传入到thenApply的方法中
        // thenApply这里实际创建了一个新的CompletableFuture实例
        CompletableFuture<String> cf2 = cf.thenApply((result) -> {
            System.out.println(Thread.currentThread() + " start job2,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread() + " exit job2,time->" + System.currentTimeMillis());
            return "test:" + result;
        });
        System.out.println("main thread start cf.get(),time->" + System.currentTimeMillis());
        // 等待子任务执行完成
        System.out.println("run result->" + cf.get());
        System.out.println("main thread start cf2.get(),time->" + System.currentTimeMillis());
        System.out.println("run result->" + cf2.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }

    public void test6() throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        // 创建异步执行任务:
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + " start job1,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread() + " exit job1,time->" + System.currentTimeMillis());
            return 1.2;
        }, pool);
        // cf关联的异步任务的返回值作为方法入参，传入到thenApply的方法中
        CompletableFuture cf2 = cf.thenApply((result) -> {
            System.out.println(Thread.currentThread() + " start job2,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread() + " exit job2,time->" + System.currentTimeMillis());
            return "test:" + result;
        }).thenAccept((result) -> { // 接收上一个任务的执行结果作为入参，但是没有返回值
            System.out.println(Thread.currentThread() + " start job3,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println(result);
            System.out.println(Thread.currentThread() + " exit job3,time->" + System.currentTimeMillis());
        }).thenRun(() -> { // 无入参，也没有返回值
            System.out.println(Thread.currentThread() + " start job4,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println("thenRun do something");
            System.out.println(Thread.currentThread() + " exit job4,time->" + System.currentTimeMillis());
        });
        System.out.println("main thread start cf.get(),time->" + System.currentTimeMillis());
        // 等待子任务执行完成
        System.out.println("run result->" + cf.get());
        System.out.println("main thread start cf2.get(),time->" + System.currentTimeMillis());
        // cf2 等待最后一个thenRun执行完成
        System.out.println("run result->" + cf2.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }

    public void test7() throws Exception {
        // 创建异步执行任务:
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "job1 start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if (false) {
                throw new RuntimeException("test");
            } else {
                System.out.println(Thread.currentThread() + "job1 exit,time->" + System.currentTimeMillis());
                return 1.2;
            }
        });
        // cf执行完成后会将执行结果和执行过程中抛出的异常传入回调方法，如果是正常执行的则传入的异常为null
        CompletableFuture<Double> cf2 = cf.whenComplete((a, b) -> {
            System.out.println(Thread.currentThread() + "job2 start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if (b != null) {
                System.out.println("error stack trace->");
                b.printStackTrace();
            } else {
                System.out.println("run succ,result->" + a);
            }
            System.out.println(Thread.currentThread() + "job2 exit,time->" + System.currentTimeMillis());
        });
        // 等待子任务执行完成
        System.out.println("main thread start wait,time->" + System.currentTimeMillis());
        // 如果cf是正常执行的，cf2.get的结果就是cf执行的结果
        // 如果cf是执行异常，则cf2.get会抛出异常
        System.out.println("run result->" + cf2.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }

    public void test8() throws Exception {
        // 创建异步执行任务:
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread() + "job1 start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if (true) {
                throw new RuntimeException("test");
            } else {
                System.out.println(Thread.currentThread() + "job1 exit,time->" + System.currentTimeMillis());
                return 1.2;
            }
        });
        // cf执行完成后会将执行结果和执行过程中抛出的异常传入回调方法，如果是正常执行的则传入的异常为null
        CompletableFuture<String> cf2 = cf.handle((a, b) -> {
            System.out.println(Thread.currentThread() + "job2 start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if (b != null) {
                System.out.println("error stack trace->");
                b.printStackTrace();
            } else {
                System.out.println("run succ,result->" + a);
            }
            System.out.println(Thread.currentThread() + "job2 exit,time->" + System.currentTimeMillis());
            if (b != null) {
                return "run error";
            } else {
                return "run succ";
            }
        });
        // 等待子任务执行完成
        System.out.println("main thread start wait,time->" + System.currentTimeMillis());
        // get的结果是cf2的返回值，跟cf没关系了
        System.out.println("run result->" + cf2.get());
        System.out.println("main thread exit,time->" + System.currentTimeMillis());
    }
}
