package com.github.adolphli.rpc;

/**
 * Created by Cao on 2017/3/5.
 */
public class HelloServiceImpl implements HelloService {

    public static final String PREFIX = "Hello, ";

    @Override
    public People hello(People people) {

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new People(PREFIX + people.getName(), people.getAge() * 2);
    }
}
