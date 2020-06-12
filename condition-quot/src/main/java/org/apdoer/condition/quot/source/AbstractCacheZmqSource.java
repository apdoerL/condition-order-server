package org.apdoer.condition.quot.source;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.condition.quot.properties.ZmqSourceProperties;
import org.zeromq.ZMQ;

import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/6/7 10:32
 */
@Slf4j
public abstract class AbstractCacheZmqSource<T> implements ZmqSource<T>, Runnable {

    protected static final int DEFAULT_CAPACITY = 100000;
    protected static final int DEFAULT_RELEASE_SIZE = 20000;

    protected int queueSize;

    protected int releaseSize;

    protected ZMQ.Context context;
    protected ZMQ.Socket socket;
    protected Queue<String> queue;

    protected ZmqSourceProperties.ZmqSource quotConfig;

    protected volatile boolean runFlag = true;

    public AbstractCacheZmqSource(ZmqSourceProperties.ZmqSource quotConfig) {
        this.quotConfig = quotConfig;
    }

    @Override
    public void init() {
        this.queueSize = null == this.quotConfig.getQueueSize() ? DEFAULT_CAPACITY : this.quotConfig.getQueueSize();
        this.releaseSize = null == this.quotConfig.getReleaseSize() ? DEFAULT_RELEASE_SIZE : this.quotConfig.getReleaseSize();
        this.queue = new ArrayBlockingQueue<>(this.queueSize);
    }


    @Override
    public void open() {
        String url = quotConfig.getUrl();
        this.context = ZMQ.context(1);
        this.socket = context.socket(ZMQ.SUB);
        this.socket.setRcvHWM(0);
        this.socket.setReceiveBufferSize(0);
        this.socket.connect(url);
        this.socket.subscribe("".getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void cleanup() {
        this.queue.clear();
    }

    @Override
    public void close() {
        if (null != socket) {
            this.socket.close();
        }
        this.runFlag = false;
    }

    @Override
    public void run() {
        log.info("quot source recv start");
        while (runFlag) {
            try {
                byte[] recv = this.socket.recv(0);
                if (recv != null) {
                    String json = new String(recv, StandardCharsets.UTF_8);
                    this.queueRelease();
                    this.queue.add(json);
                }
            } catch (Exception e) {
                log.error("zmq recv exception", e);
            }
        }
    }

    protected abstract void queueRelease();

    @Override
    public SourceType getSourceType() {
        return SourceType.ZMQ;
    }
}
