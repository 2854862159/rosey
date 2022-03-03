package com.rosey.cloud.remoting.codec;

import com.rosey.cloud.remoting.codec.handler.ClientHandler;
import com.rosey.cloud.remoting.codec.type.MyRequest;
import com.rosey.cloud.remoting.codec.type.MyResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ClassName: DefaultFuture <br/>
 * Description: <br/>
 * date: 2021/8/26 8:09 下午<br/>
 *
 * @author tooru<br />
 */
public class DefaultFuture {

    private static final Logger log = LoggerFactory.getLogger(DefaultFuture.class);

    private final Lock lock = new ReentrantLock();
    private final Condition done = lock.newCondition();
    private volatile MyResponse response;
    private Long id;
    private Channel channel;
    private MyRequest request;

    private static final Map<Long, Channel> CHANNELS = new ConcurrentHashMap<>();

    private static final Map<Long, DefaultFuture> FUTURES = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        DefaultFuture defaultFuture = new DefaultFuture();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                try{
                    defaultFuture.lock.lock();
                    defaultFuture.done.signal();
                    System.out.println("唤醒成功");
                }finally {
                    defaultFuture.lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


        try{
            defaultFuture.lock.lock();
            System.out.println("执行成功");
            defaultFuture.done.await();
            Thread.sleep(10000);
            System.out.println("释放锁");


        }finally {
            defaultFuture.lock.unlock();
        }



        System.out.println(123);
    }

    public DefaultFuture(){}

    public DefaultFuture(Channel channel, MyRequest request) {
        this.channel = channel;
        this.request = request;
        this.id = request.getReqId();
        // put into waiting map.
        FUTURES.put(id, this);
        CHANNELS.put(id, channel);
    }

    public static void received(Channel channel, MyResponse response) {
        try {
            // 根据调用编号从 FUTURES 集合中查找指定的 DefaultFuture 对象
            DefaultFuture future = FUTURES.remove(response.getReqId());
            if (future != null) {
                // 继续向下调用
                future.doReceived(response);
            } else {
                log.warn("The timeout response finally returned at ...");
            }
        }
        catch (Exception e){
            log.error("received error", e);
        }
        finally {
            CHANNELS.remove(response.getReqId());
        }
    }

    private void doReceived(MyResponse res) {
        lock.lock();
        try {
            // 保存响应对象
            response = res;
            if (done != null) {
                // 唤醒用户线程
                done.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public void send() throws InterruptedException {
        channel.writeAndFlush(request);
        try{
            lock.lock();
            done.await();
        }
        catch (Exception ex){
            log.error("send error", ex);
        }
        finally {
            lock.unlock();
        }
    }

    public MyResponse getResponse() {
        return response;
    }

    public void setResponse(MyResponse response) {
        this.response = response;
    }
}
