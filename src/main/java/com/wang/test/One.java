package com.wang.test;

import org.junit.experimental.max.MaxHistory;

import java.util.*;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/8/13 15:50
 */
public class One {
    public static int res = 0;
    public static boolean isEnd(int[][] edges,boolean[] use,Set<Integer> vis){
        int count = 0;
        for(int i = 0;i < edges.length;i ++){
            if(vis.contains(i)){
                count ++;
            }else{
                int u = edges[i][0];
                int v = edges[i][1];
                if(use[u] || use[v]){
                    count ++;
                }
            }
        }
        return count == edges.length;
    }
    public static void dfs(int[][] edges,boolean[] use,int max,Set<Integer> vis){
        if(isEnd(edges,use,vis)){
            res = Math.max(res,max);
            return;
        }

        for(int i = 0;i < edges.length;i ++){
            int u = edges[i][0];
            int v = edges[i][1];
            if(!use[u] && !use[v]){
                use[u] = true;
                use[v] = true;
                vis.add(i);
                dfs(edges,use,max + 1,vis);
                vis.remove(i);
                use[u] = false;
                use[v] = false;
            }
        }
    }

    public static void main(String[] args) {
         ProductService productService = new ProductServiceImpl();
         ProductService proxy = (ProductService) new ServiceFactory().getService(productService);
         proxy.add();
    }


}