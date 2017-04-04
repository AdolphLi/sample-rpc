package com.github.adolphli.rpc;

/**
 * Created by Cao on 2017/3/19.
 */
public class SampleServiceImpl implements SampleService {
    @Override
    public String sayHi(String name) {
        return "Hi, " + name;
    }
}
