package com.wang.evaluateMethods;

import com.wang.dataRead.Abandon;
import com.wang.exam_result.HitRatio;
import com.wang.tra.Point;
import com.wang.tra.Trajectory;

import java.io.IOException;
import java.util.List;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/1/10 14:58
 */
public class SimSt {
    public static double sim_s(Point[] seq1,Point[] seq2){
        double res = 0d;
        for(int i = 0; i < seq1.length;i++){
            double min = Double.MAX_VALUE;
            for(int j = 0; j < seq2.length;j++){
                double dis = seq1[i].getSpaceDif(seq2[j]);
                min = Math.min(min,dis);
            }
            res += Math.pow(Math.E, -min);
        }
        return res / seq1.length;
    }
    public static int sim_t(Point[] seq1,Point[] seq2){
        int res = 0;
        for(int i = 0;i < seq1.length;i++){
            int min = Integer.MAX_VALUE;
            for(int j = 0;j < seq2.length;j++){
                int timeDif = seq1[i].getTimeDif(seq2[j]);
                min = Math.min(min,timeDif);
            }
            res += Math.pow(Math.E, -min);
            //res += min;
        }
        return res/seq1.length;
    }
    public static double SimSt(Point[] seq1,Point[] seq2,double elat){
        if(elat < 0 || elat > 1){
            System.out.println("elat 非法");
            return 0;
        }
        double dif_S = SimSt.sim_s(seq1,seq2) + SimSt.sim_s(seq2,seq1);
        int dif_t = SimSt.sim_t(seq1,seq2) + SimSt.sim_t(seq2,seq1);
        return dif_S * (1-elat) + dif_t * elat;
    }

    public static void hit(List<Trajectory> trajectories, List<Trajectory> abandon){
        int rightCount = 0;
        long startTime = System.currentTimeMillis();
        for(int i = 0;i < abandon.size();i++){
            if(i % 100 == 0 && i > 0){
                System.out.println("命中数量为：" + rightCount+"SimSt 已经计算" + (i) / 10 + "%");
            }
            double min = Double.MIN_VALUE;
            int index = -1;
            for(int j = 0;j < trajectories.size();j++){
                double mps = SimSt(abandon.get(i).getPoints(),trajectories.get(j).getPoints(),0.5);
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
        System.out.println("SimSt　计算完毕，总耗时：" + timeCount + "s, 平均百条耗时：" + (timeCount)/(abandon.size()/100) +"s");
        System.out.println( "SimSt 命中条数为：" + rightCount+ " SimSt 命中率为：" + (rightCount / (double)abandon.size()));
        System.out.println("==========================================================================");
        //return rightCount/ (double)trajectories.size();
    }

    public static void main(String[] args) throws IOException {
        double[] rates = {0.02,0.03,0.05,0.1,0.15,0.1,0.2,0.3,0.4};
        //List<Trajectory> trajectories = HitRatio.hitRatio("D:\\TraDataSet\\波兰\\sample100000");
        List<Trajectory> trajectories = HitRatio.hitRatio(args[0]);

        for(int i = 0;i < rates.length;i++){
            System.out.println("当前采样率为 : " + rates[i]);
            List<Trajectory> abandon = Abandon.abandon(trajectories, rates[i]);
            SimSt.hit(trajectories,abandon);
        }
    }
}