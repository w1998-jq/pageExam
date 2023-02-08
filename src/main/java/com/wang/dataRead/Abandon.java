package com.wang.dataRead;

import com.wang.tra.Point;
import com.wang.tra.Trajectory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/1/10 20:24
 */
public class Abandon {
    /**
     * @description:
     * @param: tras 轨迹数据集
     * @param: rate 子轨迹的采样率
     * @return: java.util.Map<java.lang.String,com.wang.tra.Point[]>
     * @author jqwang
     * @date: 2022/1/10 20:30
     */
    public static List<Trajectory> abandon(List<Trajectory> tras , double rate) {
        Random random = new Random();
        List<Trajectory> res = new ArrayList<>();
        for(int i = 0;i < tras.size();i++){
            Point[] tra = tras.get(i).getPoints();
            //System.out.print(tra.length + "    ");
            List<Point> list = new ArrayList<>();
            for(Point point : tra){
                list.add(point);
            }

            int count = (int) (tra.length * (1 - rate));
            int aba = 0;
            while(aba < count){
                int index = random.nextInt(list.size());
                list.remove(index);
                aba ++;
            }
            Point[] points = new Point[list.size()];
            for(int j = 0;j < points.length;j++){
                points[j] = list.get(j);
            }
            Trajectory trajectory = new Trajectory(tras.get(i).getName(), points);
            res.add(trajectory);
            //System.out.println(points.length);
        }
        return res;
    }
}