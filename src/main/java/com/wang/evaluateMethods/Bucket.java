package com.wang.evaluateMethods;

import com.wang.tra.Point;
import com.wang.tra.Trajectory;

import java.util.*;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/1/10 19:33
 */
public class Bucket {
    private static double minDis = 200d; //单位为米，距离阈值
    private static int minTime = 30; //单位为秒，时间阈值

    public static double Bucket(Point[] seq1,Point[] seq2){
        //分桶
        Map<Integer, Set<Integer>> bucket = new HashMap<>();
        //将一条轨迹添加到分桶
        for(int i = 0; i < seq1.length; i ++){
            //根据当前位置点的时间戳将其存放到对应的时间分桶
            int bucketID = seq1[i].getTime()/15;

            if(!bucket.containsKey(bucketID)){
                Set<Integer> set = new HashSet<>();
                bucket.put(bucketID,set);
            }
            bucket.get(bucketID).add(i);
        }
        double res_S = 0;
        int res_T = 0;
        for(int i = 0;i < seq2.length;i++){
            int bucketID = seq2[i].getTime()/15;
            Set<Integer> points = bucket.get(bucketID);
            Set<Integer> pre = bucket.get(bucketID - 1);
            Set<Integer> after = bucket.get(bucketID + 1);
            if(points == null){
                points = new HashSet<>();
            }
            if(pre != null){
                points.addAll(pre);
            }
            if(after != null){
                points.addAll(after);
            }
            if(points.size() == 0){
                res_S += minDis;
                res_T += minTime;
                continue;
            }
            //计算空间相似度
            double min = minDis;
            int minT = minTime;
            for(int point : points){
                int timeDif = seq1[point].getTimeDif(seq2[i]);
                double spaceDis = seq1[point].getSpaceDif(seq2[i]);
                if(timeDif > minTime){
                    min = Math.min(min,minDis);
                    minT = Math.min(minT,minTime);
                }else{
                    min = Math.min(min,spaceDis);
                    minT = Math.min(minT,timeDif);
                }
            }
            res_S += min;
            res_T += minT;
        }
        return ((res_S * (seq1.length + seq2.length))/seq1.length * seq2.length) + ((res_T * (seq1.length + seq2.length))/seq1.length * seq2.length);
    }

    public static double Bucket_modif(Point[] seq1,Point[] seq2){
        //分桶
        Map<Integer, Set<Integer>> bucket = new HashMap<>();
        //将一条轨迹添加到分桶
        for(int i = 0; i < seq1.length; i ++){
            //根据当前位置点的时间戳将其存放到对应的时间分桶
            int bucketID = seq1[i].getTime()/15;

            if(!bucket.containsKey(bucketID)){
                Set<Integer> set = new HashSet<>();
                bucket.put(bucketID,set);
            }
            bucket.get(bucketID).add(i);
        }
        double res_S = 0;
        int res_T = 0;

        for(int i = 0;i < seq2.length;i++){
            int bucketID = seq2[i].getTime()/15;
            Set<Integer> points = bucket.get(bucketID);
            Set<Integer> pre = bucket.get(bucketID - 1);
            Set<Integer> after = bucket.get(bucketID + 1);
            if(points == null){
                points = new HashSet<>();
            }
            if(pre != null){
                points.addAll(pre);
            }
            if(after != null){
                points.addAll(after);
            }
            if(points.size() == 0){
                continue;
            }

            //计算空间相似度
            double min = minDis;
            int minT = minTime;
            for(int point : points){

                int timeDif = seq1[point].getTimeDif(seq2[i]); //时间差异
                double spaceDis = seq1[point].getSpaceDif(seq2[i]); //空间差异

                if(timeDif < minTime){
                    min = Math.min(min,spaceDis);
                    minT = Math.min(minT,timeDif);
                }
            }
            res_S += min;
            res_T += minT;
        }
        return ((seq2.length + seq1.length)/ seq2.length * seq1.length) * res_S + ((seq2.length + seq1.length)/ seq2.length * seq1.length) * res_T;
    }

    public static double[] bucket_top(Point[] seq1,Point[] seq2,boolean flag){
        //flag 为True时为max
        //分桶
        Map<Integer, Set<Integer>> bucket = new HashMap<>();
        //将一条轨迹添加到分桶
        for(int i = 0; i < seq1.length; i ++){
            //根据当前位置点的时间戳将其存放到对应的时间分桶
            int bucketID = seq1[i].getTime()/15;

            if(!bucket.containsKey(bucketID)){
                Set<Integer> set = new HashSet<>();
                bucket.put(bucketID,set);
            }
            bucket.get(bucketID).add(i);
        }
        double res_S_Max = 0d,res_S_Min = 0d;
        int res_T_Max = 0,res_T_Min = 0;

        for(int i = 0;i < seq2.length;i++){
            int bucketID = seq2[i].getTime()/15;
            Set<Integer> points = bucket.get(bucketID);
            Set<Integer> pre = bucket.get(bucketID - 1);
            Set<Integer> after = bucket.get(bucketID + 1);
            if(points == null){
                points = new HashSet<>();
            }
            if(pre != null){
                points.addAll(pre);
            }
            if(after != null){
                points.addAll(after);
            }
            if(points.size() == 0){
                continue;
            }

            //计算空间相似度
            double min = minDis;
            int minT = minTime;

            res_S_Max +=  1;
            res_S_Min += Math.pow(Math.E,-min);
            res_T_Max +=   1;
            res_T_Min += Math.pow(Math.E,-minT);
        }
        return new double[]{(res_S_Max*0.5 + res_T_Max*0.5)/(seq2.length + seq1.length),(res_S_Min*0.5 + res_T_Min*0.5)/(seq2.length+seq1.length)};
    }

    public static double Bucket_modif_2_single(Point[] seq1,Point[] seq2){
        //分桶
        Map<Integer, Set<Integer>> bucket = new HashMap<>();
        //将一条轨迹添加到分桶
        for(int i = 0; i < seq1.length; i ++){
            //根据当前位置点的时间戳将其存放到对应的时间分桶
            int bucketID = seq1[i].getTime()/15;

            if(!bucket.containsKey(bucketID)){
                Set<Integer> set = new HashSet<>();
                bucket.put(bucketID,set);
            }
            bucket.get(bucketID).add(i);
        }
        double res_S = 0;
        int res_T = 0;

        for(int i = 0;i < seq2.length;i++){
            int bucketID = seq2[i].getTime()/15;
            Set<Integer> points = bucket.get(bucketID);
            Set<Integer> pre = bucket.get(bucketID - 1);
            Set<Integer> after = bucket.get(bucketID + 1);
            if(points == null){
                points = new HashSet<>();
            }
            if(pre != null){
                points.addAll(pre);
            }
            if(after != null){
                points.addAll(after);
            }
            if(points.size() == 0){
                continue;
            }

            //计算空间相似度
            double min = minDis;
            int minT = minTime;
            for(int point : points){

                int timeDif = seq1[point].getTimeDif(seq2[i]); //时间差异
                double spaceDis = seq1[point].getSpaceDif(seq2[i]); //空间差异

                if(timeDif < minTime){
                    min = Math.min(min,spaceDis);
                    minT = Math.min(minT,timeDif);
                }
            }
            res_S += Math.pow(Math.E,-min);
            //res_S += (-min/ minDis) + 1.5;

            res_T += Math.pow(Math.E,-minT);
            //res_T += (-minT/(double)minTime) + 1.5;
        }
        return (res_S*0.5 + res_T*0.5)/seq2.length;
    }


    public static double Bucket_modif_2(Point[] seq1,Point[] seq2){
        double sim_st_2 = Bucket_modif_2_single(seq1, seq2);
        double sim_st_1 = Bucket_modif_2_single(seq2, seq1);
        return sim_st_1 * 0.5 + sim_st_2*0.5;
    }

    public static void hit(List<Trajectory> trajectories, List<Trajectory> abandon){
        int rightCount = 0;
        long startTime = System.currentTimeMillis();
        for(int i = 0;i < 1000;i++){
            if(i % 100 == 0 && i > 0){
                System.out.println("Bucket 已经计算" + (i) / 10 + "%");
            }
            double min = Double.MIN_VALUE;
            int index = -1;
            for(int j = 0;j < 1000;j++){
                double mps = Bucket_modif_2(abandon.get(i).getPoints(), trajectories.get(j).getPoints());
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
        System.out.println("Bucket　计算完毕，总耗时：" + timeCount + "s, 平均百条耗时：" + (timeCount)/10 +"s");
        System.out.println("Bucket 命中率为：" + (rightCount / 1000d));
        System.out.println("==========================================================================");
        //return rightCount/ (double)trajectories.size();
    }
}