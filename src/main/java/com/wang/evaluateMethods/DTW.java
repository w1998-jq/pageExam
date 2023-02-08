package com.wang.evaluateMethods;

import com.wang.tra.Point;
import com.wang.tra.Trajectory;

import java.util.Arrays;
import java.util.List;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/1/10 12:54
 */
public class DTW {
    public static void main(String[] args) {
        int[] seq1 = {1, 1, 2, 3};
        int[] seq2 = {1, 2, 2, 3};

        int[][] test = {{1, 2, 3, 4}, {5, 6, 7, 8}};
        System.out.println(Arrays.deepToString(test));
    }

    public static double DTW(Point[] seq1, Point[] seq2) {
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
        dp[0][0] = dists[0][0];
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i - 1][0] + dists[i][0];
        }
        for (int j = 1; j < n; j++) {
            dp[0][j] = dp[0][j - 1] + dists[0][j];
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = Math.min(Math.min(dp[i - 1][j - 1] + dists[i][j], dp[i - 1][j] + dists[i][j]),
                        dp[i][j - 1] + dists[i][j]);
            }
        }
        return dp[m - 1][n - 1];
    }

    public static void hit(List<Trajectory> trajectories, List<Trajectory> abandon) {
        int rightCount = 0;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            if (i % 100 == 0 && i > 0) {
                System.out.println("DTW 已经计算" + (i) / 10 + "%,命中数量为" + rightCount );
            }
            double min = Double.MAX_VALUE;
            int index = -1;
            for (int j = 0; j < 1000; j++) {
                double mps = DTW(abandon.get(i).getPoints(), trajectories.get(j).getPoints());
                if (min > mps) {
                    min = mps;
                    index = j;
                }
            }
            if (trajectories.get(index).getName().equals(abandon.get(i).getName())) {
                rightCount++;
            }
        }

        double timeCount =  (System.currentTimeMillis() - startTime)/ 1000;
        System.out.println("DTW　计算完毕，总耗时：" + timeCount + "s, 平均百条耗时：" + (timeCount)/10 +"s");
        System.out.println("DTW 命中率为：" + (rightCount / 1000d));
        System.out.println("==========================================================================");
        //return rightCount / (double) trajectories.size();
    }
}