package com.wang.evaluateMethods;

import com.wang.tra.Point;
import com.wang.tra.Trajectory;

import java.util.List;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/1/11 19:58
 */
public class SimSt_o {
    public static double sim_S(Point[] seq1,Point[] seq2){
        int m = seq1.length;
        int n = seq2.length;

        double[][] dists = new double[m][n];
        double[][] dp = new double[m][n];


        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dists[i][j] = seq1[i].getSpaceDif(seq2[j]);
            }
        }

        // 初始化dp的第一行和第一列
        dp[0][0] = Math.pow(Math.E,-dists[0][0]);
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i - 1][0] + Math.pow(Math.E,-dists[i][0]);
        }
        for (int j = 1; j < n; j++) {
            dp[0][j] = dp[0][j - 1] + Math.pow(Math.E,-dists[0][j]);
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = Math.max(dp[i - 1][j],dp[i][j - 1]) + Math.pow(Math.E,-dists[i][j]);
            }
        }
        return dp[m - 1][n - 1];
    }

    public static double sim_T(Point[] seq1,Point[] seq2){
        int m = seq1.length;
        int n = seq2.length;

        double[][] dists = new double[m][n];
        double[][] dp = new double[m][n];


        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dists[i][j] = seq1[i].getTimeDif(seq2[j]);
            }
        }

        // 初始化dp的第一行和第一列
        dp[0][0] = Math.pow(Math.E,-dists[0][0]);
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i - 1][0] + Math.pow(Math.E,-dists[i][0]);
        }
        for (int j = 1; j < n; j++) {
            dp[0][j] = dp[0][j - 1] + Math.pow(Math.E,-dists[0][j]);
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = Math.max(dp[i - 1][j],dp[i][j - 1]) + Math.pow(Math.E,-dists[i][j]);
            }
        }
        return dp[m - 1][n - 1];
    }

    public static double sim_st_o(Point[] seq1,Point[] seq2,double elat){
        double simS = sim_S(seq1, seq2);
        double simT = sim_T(seq1, seq2);
        return simS * elat + simT * (1 - elat);
    }

    public static void hit(List<Trajectory> trajectories, List<Trajectory> abandon){
        int rightCount = 0;
        long startTime = System.currentTimeMillis();
        for(int i = 0;i < trajectories.size();i++){
            if(i % 100 == 0 && i > 0){
                System.out.println("Sim_st_0 已经计算" + (i) / 10 + "%");
            }
            double min = Double.MAX_VALUE;
            int index = -1;
            for(int j = 0;j < trajectories.size();j++){
                double mps = sim_st_o(abandon.get(i).getPoints(), trajectories.get(j).getPoints(), 0.5);
                if(min > mps){
                    min = mps;
                    index = j;
                }
            }
            if(trajectories.get(index).getName().equals(abandon.get(i).getName())){
                rightCount++;
            }
        }
        double timeCount =  (System.currentTimeMillis() - startTime)/ 1000;
        System.out.println("Sim_st_0　计算完毕，总耗时：" + timeCount + "s, 平均百条耗时：" + (timeCount)/(trajectories.size()/100) +"s");
        System.out.println("Sim_st_0 命中率为：" + (rightCount / trajectories.size()));
        System.out.println("==========================================================================");
        //return rightCount/ (double)trajectories.size();
    }
}