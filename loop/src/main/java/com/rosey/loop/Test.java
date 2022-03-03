package com.rosey.loop;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ClassName: Test <br/>
 * Description: <br/>
 * date: 2021/12/2 10:16 下午<br/>
 *
 * @author tooru<br />
 */
public class Test {

    public static void main(String[] args) {
        MyLinkedRunnable runnable = new MyLinkedRunnable("task1");
        new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    runnable.lock.lock();
                    runnable.names.offer(UUID.randomUUID().toString());
                    runnable.done.signal();
                } finally {
                    runnable.lock.unlock();
                }
            }
        }).start();

        MyLinkedRunnable runnable2 = new MyLinkedRunnable("task2");
        new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    runnable2.lock.lock();
                    runnable2.names.offer(UUID.randomUUID().toString());
                    runnable2.done.signal();
                } finally {
                    runnable2.lock.unlock();
                }
            }
        }).start();

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        scheduledExecutorService.schedule(runnable, 10, TimeUnit.SECONDS);
        scheduledExecutorService.schedule(runnable2, 10, TimeUnit.SECONDS);
    }

    static class MyRunnable implements Runnable {

        private String name;

        public MyRunnable(String name) {
            this.name = name;
        }

        public ArrayBlockingQueue<String> names = new ArrayBlockingQueue<String>(10000);

        @Override
        public void run() {
            while (true) {
                try {
                    String take = names.take();
                    System.out.println(name + "执行任务" + take + "长度" + names.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class MyLinkedRunnable implements Runnable {

        private String name;

        public MyLinkedRunnable(String name) {
            this.name = name;
        }

        public volatile ConcurrentLinkedQueue<String> names = new ConcurrentLinkedQueue<String>();

        public final Lock lock = new ReentrantLock();
        public final Condition done = lock.newCondition();

        @Override
        public void run() {
            while (true) {
                String take = names.poll();
                if(take == null){
                    lock.lock();
                    try {
                        done.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }else {
                    System.out.println(name + "执行任务" + take + "长度" + names.size());
                }

            }
        }
    }
}
