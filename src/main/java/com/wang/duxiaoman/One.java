package com.wang.duxiaoman;

import java.util.Scanner;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/8/31 19:05
 */
public class One {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] arr = new int[n];
        int res = 0;
        //System.out.println(k);
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j <= n - i; j++) {
                int min = Integer.MAX_VALUE;
                int max = Integer.MIN_VALUE;
                for (int s = j; s < j + i; s++) {
                    min = Math.min(min, arr[s]);
                    max = Math.max(max, arr[s]);
                }
                if ((max - min) % k == 0) {
                    //System.out.println(j + " :  " + i);
                    res++;
                }
            }
        }
        System.out.println(res);
    }

}