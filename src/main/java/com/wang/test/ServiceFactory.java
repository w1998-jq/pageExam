package com.wang.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/10/10 16:04
 */
public class ServiceFactory implements InvocationHandler {

    private ProductService productService;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("代理前");
        method.invoke(this.productService,args);
        System.out.println("代理后");
        return proxy;
    }

    public Object getService(ProductService service) {
        //这里返回的是代理类对象
        this.productService = service;
        Class clazz = service.getClass();
        return Proxy.newProxyInstance(
                clazz.getClassLoader(),
                clazz.getInterfaces(), this);
    }
}