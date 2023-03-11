package com.wang.exam_result;

import com.wang.dataRead.Abandon;
import com.wang.dataRead.ReadToTra;
import com.wang.evaluateMethods.*;
import com.wang.indexStruc.CreateBucket;
import com.wang.tra.Point;
import com.wang.tra.Trajectory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/1/12 12:48
 */
public class HitRatio {
    /**
     * @description:  获取实验测试的样本
     * @param: path  轨迹的存在文件路径
     * @param: count 测试的轨迹的条数
     * @return: void
     * @author jqwang
     * @date: 2022/1/12 12:49
     */
    public static void getSample(String path,int count,String outputPath) throws IOException {
        List<Trajectory> trajectories = ReadToTra.proToTra(path);
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
        int counts = 0;
        for(int i = 0;i < trajectories.size();i++){
            if(trajectories.get(i).getPoints().length >= 100){
                bw.write(trajectories.get(i).getName() + "#" + trajectories.get(i).getPoints()[0].getTime() +  "\t");
                Point[] points = trajectories.get(i).getPoints();
                for(int j = 0;j < points.length; j++){
                    bw.write(points[j].getLon() + "#" + points[j].getLat() + "\t");
                }
                bw.write("\n");
                counts++;
            }
        }
        System.out.println("长度大于100的轨迹数量为：" + counts);
        bw.flush();
        bw.close();
    }


    public static List<Trajectory> hitRatio(String path) throws IOException {
        //List<Trajectory> trajectories = ReadToTra.proToTra(path);
        List<Trajectory> trajectories = ReadToTra.geoLifeToTra(path);
        //List<Trajectory> trajectories = ReadToTra.readFromFile(path);
        System.out.println(trajectories.size());
        Iterator<Trajectory> iterator = trajectories.iterator();
        int c = 0;
        while(c < 9000){
            if(iterator.hasNext()){
                iterator.next();
                iterator.remove();
            }
            c ++;
        }
        System.out.println(trajectories.size());
        List<Trajectory> res = new ArrayList<>();
        int lenCount = 0;
        for(int i = 0;i < 1000;i++){
            res.add(trajectories.get(i));
            lenCount += trajectories.get(i).getPoints().length;
        }
        System.out.println("平均长度为 ：" + (lenCount/1000));
        return res;
    }

    public static void main(String[] args) throws IOException {
        //String outputPath = "D:\\TrajectoryDataset\\Porto\\simTra_1000_最相似轨迹";
        String outputPath = "D:\\TrajectoryDataset\\geoline";
        //String outputPath = args[0];;
        double[] count = {0.01,0.03,0.05,0.07,0.15,0.23,0.31,0.39};
        for(double c : count){
            System.out.println("采样率为" + c );
            List<Trajectory> trajectories = hitRatio(outputPath);
            List<Trajectory> abandon = Abandon.abandon(trajectories,c);
            //EDwP.hit(trajectories,abandon);
            //DTW.hit(trajectories,abandon);
            //SimSt.hit(trajectories,abandon);
            Bucket.hit(trajectories,abandon);
        }

    }
}