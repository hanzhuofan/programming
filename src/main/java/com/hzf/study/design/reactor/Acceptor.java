package com.hzf.study.design.reactor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author zhuofan.han
 * @date 2022/1/20
 */
public class Acceptor implements Runnable {
    // server socket port
    private int port;
    private Selector selector;

    // 代表 serversocket，通过LinkedBlockingQueue来模拟外部输入请求队列
    private BlockingQueue<InputSource> sourceQueue = new LinkedBlockingQueue<InputSource>();

    Acceptor(Selector selector, int port) {
        this.selector = selector;
        this.port = port;
    }

    // 外部有输入请求后，需要加入到请求队列中
    public void addNewConnection(InputSource source) {
        sourceQueue.offer(source);
    }

    public int getPort() {
        return this.port;
    }

    @Override
    public void run() {
        while (true) {

            InputSource source = null;
            try {
                // 相当于 serversocket.accept()，接收输入请求，该例从请求队列中获取输入请求
                source = sourceQueue.take();
            } catch (InterruptedException e) {
                // ignore it;
            }

            // 接收到InputSource后将接收到event设置type为ACCEPT，并将source赋值给event
            if (source != null) {
                Event acceptEvent = new Event();
                acceptEvent.setSource(source);
                acceptEvent.setType(EventType.ACCEPT);

                selector.addEvent(acceptEvent);
            }
        }
    }
}
