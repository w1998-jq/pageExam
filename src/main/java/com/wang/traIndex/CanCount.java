package com.wang.traIndex;

import com.wang.dataRead.ReadToTra;
import com.wang.indexStruc.CreateBucket;
import com.wang.tra.Point;
import com.wang.tra.Trajectory;
import org.apache.lucene.util.RamUsageEstimator;

import java.io.IOException;
import java.util.*;

/**
 * 获取候选结果数量
 *
 * @ClassName CanCount
 * Description
 * @Author jqWang
 * Date 2023/2/18 18:07
 **/
public class CanCount {
    public static void main(String[] args) throws IOException {
        List<Trajectory> trajectoriesSet = ReadToTra.geoLifeToTra("D:\\TrajectoryDataset\\geoline");

        //轨迹ID ：轨迹
        Map<String, Trajectory> map = new HashMap<>();
        for (int i = 0; i < trajectoriesSet.size(); i++) {
            map.put(trajectoriesSet.get(i).getName(), trajectoriesSet.get(i));
        }

        List<Trajectory> trajectories = new ArrayList<>();
        while (trajectories.size() < 100) {
            int i = new Random().nextInt(trajectoriesSet.size());
            if (trajectoriesSet.get(i).getPoints().length < 70 || trajectoriesSet.get(i).getPoints().length > 90) {
                continue;
            }
            trajectories.add(trajectoriesSet.get(i));
        }
        /*Map<String, Set<String>> lshIndex = new HashMap<>();
        SST sst = new SST(200, "dataSetName");

        for (int i = 0; i < trajectoriesSet.size(); i++) {
            Point[] points = trajectoriesSet.get(i).getPoints();
            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < points.length; j ++) {

                sb.append(sst.generateGridId(points[j].getLat(), points[j].getLon()));

                String pathKey = sb.toString();
                sb.setLength(0);

                if (!lshIndex.containsKey(pathKey)) {
                    Set<String> set = new HashSet<>();
                    lshIndex.put(pathKey, set);
                }
                lshIndex.get(pathKey).add(trajectoriesSet.get(i).getName());
            }
        }
        Set<String> num = new HashSet<>();
        for (int i = 0; i < trajectories.size(); i++) {
            Point[] points = trajectories.get(i).getPoints();
            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < points.length ; j ++) {
                sb.append(sst.generateGridId(points[j].getLat(), points[j].getLon()));
                String pathKey = sb.toString();
                sb.setLength(0);
                num.addAll(lshIndex.get(pathKey));
            }
        }*/
        /*for (int i = 0; i < trajectoriesSet.size(); i++) {
            Point[] points = trajectoriesSet.get(i).getPoints();
            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < points.length - 2; j += 2) {
                for (int x = j; x < j + 2; x++) {
                    sb.append(sst.generateGridId(points[x].getLat(), points[x].getLon()));
                }
                String pathKey = sb.toString();
                sb.setLength(0);

                if (!lshIndex.containsKey(pathKey)) {
                    Set<String> set = new HashSet<>();
                    lshIndex.put(pathKey, set);
                }
                lshIndex.get(pathKey).add(trajectoriesSet.get(i).getName());
            }
        }


        Set<String> num = new HashSet<>();
        for (int i = 0; i < trajectories.size(); i++) {
            Point[] points = trajectories.get(i).getPoints();
            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < points.length - 2; j += 2) {
                for (int x = j; x < j + 2; x++) {
                    sb.append(sst.generateGridId(points[x].getLat(), points[x].getLon()));
                }
                String pathKey = sb.toString();
                sb.setLength(0);
                num.addAll(lshIndex.get(pathKey));
            }
        }*/
        Set<String> num = new HashSet<>();
        CreateBucket createBucket = new CreateBucket(3,trajectoriesSet,
                trajectoriesSet.size(),30,200);
            for(int i = 0;i < trajectories.size();i++){
                num.addAll(createBucket.top_k_Count(trajectories.get(i),map,10));
            }


        System.out.println("候选结果数量为：" + num.size() / trajectories.size());
    }
}
