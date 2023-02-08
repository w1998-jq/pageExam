package com.wang.test;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/10/10 17:02
 */
public class Singleton {
    private volatile Singleton singleton;
    public Singleton getSingleton(){
        if(singleton == null){
            synchronized (this){
                if(singleton == null){
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }

}