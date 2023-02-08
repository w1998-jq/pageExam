package com.wang.evaluateMethods;

import com.wang.dataRead.Abandon;
import com.wang.exam_result.HitRatio;
import com.wang.tra.Point;
import com.wang.tra.Trajectory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * SST论文中提出的轨迹相似度算法方法
 * 返回值越小，轨迹相似度越高
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/1/10 16:19
 */
public class MPS {
    private static double elat = 0.5;

    /**
     * @description:  两个点之间的相似度，返回值越小，相似度越大
     * @param: p1 点
     * @param: p2 点
     * @param: elat 超参
     * @return: double
     * @author jqwang
     * @date: 2022/1/10 16:30
     */
    public static double point_point(Point p1,Point p2){
        double spaceDif = p1.getSpaceDif(p2);
        int timeDif = p1.getTimeDif(p2);
        return  Math.pow(Math.E,-spaceDif) * elat + Math.pow(Math.E,-timeDif) * (1 - elat);
    }

    /**
     * @description:  两个片段之间的相似度
     * @param: start_p 第一个开始节点
     * @param:end_p 第一个结束节点
     * @param:start_e 第二个开始节点
     * @param:end_e 第二个结束节点
     * @return: double
     * @author jqwang
     * @date: 2022/1/10 16:35
     */
    public static double line_line(Point start_p,Point end_p, Point start_e,Point end_e){
        double first = Math.max(MPS.point_point(start_p, start_e), MPS.point_point(start_p, end_e));
        double second = Math.max(MPS.point_point(end_p, start_e), MPS.point_point(end_p, end_e));
        return Math.min(first,second);
    }

    public static double MPS(Point[] seq1,Point[] seq2,double f){

        double res1 = 0d;
        for(int i = 0;i < seq1.length - 1;i ++){
            double min = Double.MIN_VALUE;
            for(int j = 0;j < seq2.length - 1;j++){
                double sim = MPS.line_line(seq1[i], seq1[i + 1], seq2[j], seq2[j + 1]);
                if(min < sim){
                    min = sim;
                }
            }
            res1 += min;
        }
        double res2 = 0d;
        for(int i = 0;i < seq2.length - 1;i++){
            double min = Double.MIN_VALUE;
            for(int j = 0;j <seq1.length - 1;j++){
                double sim = MPS.line_line(seq2[i], seq2[i + 1], seq1[j], seq1[j + 1]);
                if(min < sim){
                    min = sim;
                }
            }
            res2 += min;
        }
        return (res1/seq1.length) * f + (res2/seq2.length) * (1-f);
    }

    public static void hit(List<Trajectory> trajectories,List<Trajectory> abandon){
        int rightCount = 0;
        long startTime = System.currentTimeMillis();
        for(int i = 0;i < 100;i++){
            if(i % 100 == 0 && i > 0){
                //System.out.println(i);
                System.out.println("MPS 已经计算" + (i) / 10 + "%  命中数量" + rightCount);
            }
            double min = Double.MIN_VALUE;
            int index = -1;
            for(int j = 0;j < 100;j++){
                double mps = MPS(abandon.get(i).getPoints(), trajectories.get(j).getPoints(), 0.5);
                //System.out.println(mps);
                if(min < mps){
                    min = mps;
                    index = j;
                }
            }
            if(trajectories.get(index).getName().equals(abandon.get(i).getName())){
                rightCount++;
            }
        }
        double timeCount =  (System.currentTimeMillis() - startTime)/ 1000;
        System.out.println("MPS　计算完毕，总耗时：" + timeCount + "s, 平均百条耗时：" + (timeCount)/(trajectories.size()/100) +"s");
        System.out.println("MPS 命中率为：" + (rightCount / (double)trajectories.size()) + " 命中数量为：" + rightCount);
        System.out.println("==========================================================================");

    }

    public static void main(String[] args) throws IOException {
        double[] rates = {0.03,0.04,0.05,0.06,0.08,0.1,0.12,0.15,0.2};
        //List<Trajectory> trajectories = HitRatio.hitRatio("D:\\TraDataSet\\波兰\\sample100000");
        List<Trajectory> trajectories = HitRatio.hitRatio(args[0]);

        for(int i = 0;i < rates.length;i++){
            System.out.println("当前采样率为 : " + rates[i]);
            List<Trajectory> abandon = Abandon.abandon(trajectories, rates[i]);
            MPS.hit(trajectories,abandon);
        }
    }

}