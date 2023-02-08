package com.wang.duxiaoman;

import java.util.HashSet;
import java.util.Scanner;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/8/31 19:38
 */
public class Two {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while(t > 0){
            int n = sc.nextInt();
            int m = sc.nextInt();
            int k = sc.nextInt();
            int x = sc.nextInt();
            int[][] grid = new int[n][m];
            for(int i = 0;i < n;i ++){
                for(int j = 0;j < m;j ++){
                    grid[i][j] = sc.nextInt();
                }
            }
            HashSet<Integer>[][] dp = new HashSet[n][m];
            for(int i = 0;i < n;i ++){
                for(int j = 0;j < m;j ++){
                    dp[i][j] = new HashSet<Integer>();
                }
            }
            dp[0][0].add(grid[0][0]);
            for(int i = 1;i < m;i ++){
                HashSet<Integer> left = dp[0][i - 1];
                for(int num : left){
                    dp[0][i].add(num + grid[0][i]);
                }
            }
            for(int i = 1;i < n;i ++){
                HashSet<Integer> pre = dp[i - 1][0];
                for(int num : pre){
                    dp[i][0].add(num + grid[i][0]);
                }
            }
            for(int i = 1;i < n;i ++){
                for(int j = 1;j < m;j ++){
                    HashSet<Integer> up = dp[i - 1][j];
                    for(int num : up){
                        dp[i][j].add(num + grid[i][j]);
                    }
                    HashSet<Integer> left = dp[i][j - 1];
                    for(int num : up){
                        dp[i][j].add(num + grid[i][j]);
                    }
                }
            }
            for(int num : dp[n - 1][m - 1]){
                System.out.println(num);
            }
            if(dp[n - 1][m - 1].contains(x)){
                System.out.println("yes");
            }else{
                System.out.println("no");
            }
            t --;
        }
    }
}