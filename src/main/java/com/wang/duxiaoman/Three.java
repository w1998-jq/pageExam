package com.wang.duxiaoman;

import java.util.Scanner;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/8/31 20:12
 */
public class Three {

    public static int jie(int m) {
        if (m == 0) {
            return 1;
        }
        int res = 1;
        for(int i = 2;i <= m;i ++){
            res *= i;
        }
        return res;
    }

    public static int getC(int n, int m) {
        int temp = 0;
        try {
            temp = jie(n) / (jie(m) * jie(n - m));
        }catch (ArithmeticException e){
            System.out.println(n + "  " + m + "  " + jie(n) + "  " + jie(m) + "  " + jie(n - m));

        }
        return temp;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long res = 1;
        for (long i = 0; i < n; i++) {
            res *= 9;
        }
        for (int i = n - 1; i > 1; i--) {
            int curCount = getC(n, i) * 8;
            res -= curCount;
        }

        System.out.println(res - 1);

    }

}