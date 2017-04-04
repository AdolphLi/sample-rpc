package com.github.adolphli.rpc;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConcurrentTest {

    public static ServiceFactory factory = ServiceFactory.getInstance();
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    @Test
    public void concurrentTest() throws Exception {

        publishService();
        final HelloService helloService = (HelloService) factory.consumer("hello")
                .service("com.github.adolphli.rpc.HelloService")
                .serviceId("uniqueServiceId")
                .methodTimeout("hello", 5000)
                .targetUrl("127.0.0.1:6666")
                .consume();

        final SampleService sampleService = (SampleService) factory.consumer("world")
                .service("com.github.adolphli.rpc.SampleService")
                .serviceId("uniqueServiceId")
                .targetUrl("127.0.0.1:6666")
                .consume();

        int times = 100;
        final CountDownLatch taskDone = new CountDownLatch(times * 2);
        final List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<Throwable>());
        for (int i = 0; i < times; i++) {
            final int index = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        People result = helloService.hello(new People(Integer.toString(index), index));
                        assertEquals(result.getAge(), index * 2);
                        assertEquals(result.getName(), HelloServiceImpl.PREFIX + Integer.toString(index));
                    } catch (Throwable e) {
                        e.printStackTrace();
                        exceptions.add(e);
                    } finally {
                        taskDone.countDown();
                    }
                }
            });

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String result = sampleService.sayHi(String.valueOf(index));
                        assertEquals("Hi, " + index, result);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        exceptions.add(e);
                    } finally {
                        taskDone.countDown();
                    }
                }
            });
        }
        taskDone.await(60, TimeUnit.SECONDS);
        assertTrue(taskDone.getCount() == 0);
        assertEquals(0, exceptions.size());
    }

    private static void publishService() {
        factory.provider("hello")
                .service("com.github.adolphli.rpc.HelloService")
                .serviceId("uniqueServiceId")
                .impl(new HelloServiceImpl())
                .publish();

        factory.provider("world")
                .service("com.github.adolphli.rpc.SampleService")
                .serviceId("uniqueServiceId")
                .impl(new SampleServiceImpl())
                .publish();
    }
}
