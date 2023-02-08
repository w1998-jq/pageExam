package com.wang.traIndex;

import com.wang.dataRead.ReadToTra;
import com.wang.tra.Point;
import com.wang.tra.Trajectory;

import java.io.IOException;
import java.util.*;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/3/3 14:05
 */
public class LSH {
    int segment;
    String path; //数据集目录
    String dataSetName; //数据集名称
    String mapName; //经纬度映射方法
    Map<String, Set<String>> lshIndex;
    SST sst ;

    public LSH(int segment,String dataSetName,List<Trajectory> trajectoriesSet,int count){
        this.segment = segment;
        this.path = null;
        this.dataSetName = dataSetName;
        this.lshIndex = new HashMap<>();
        this.sst = new SST(200,dataSetName);
        createIndex(dataSetName,trajectoriesSet,count);
    }

    public void createIndex(String dataSetName,List<Trajectory> trajectoriesSet,int count){
        long l = System.currentTimeMillis();
        List<Trajectory> trajectories = null;
        try {
            if(dataSetName.equals("boLan")){
                //波兰数据集
                trajectories = trajectoriesSet;
            }else{
                //new york 数据集
                trajectories = ReadToTra.readFromFile(path);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }


        for(int i = 0;i < count;i++){
            if(i % 100000 == 0){
                System.out.println("已经处理了" + i + "条轨迹") ;
            }
            insertToIndex(trajectories.get(i));
        }
        System.out.println("生成轨迹耗时：" + (System.currentTimeMillis() - l)/1000d + "s");
    }

    public void insertToIndex(Trajectory trajectory){
        Point[] points = trajectory.getPoints();
        StringBuilder sb = new StringBuilder();

        for(int i = 0;i < points.length - segment;i+=segment){
            for(int j = i;j < i + segment;j++){
                sb.append(sst.generateGridId(points[j].getLat(),points[j].getLon()));
            }
            String pathKey = sb.toString();
            sb.setLength(0);

            if(!lshIndex.containsKey(pathKey)){
                Set<String> set = new HashSet<>();
                lshIndex.put(pathKey,set);
            }
            lshIndex.get(pathKey).add(trajectory.getName());
        }
    }

    public Set<String> select(Trajectory trajectory){
        Point[] points = trajectory.getPoints();
        StringBuilder sb = new StringBuilder();
        Set<String> res = new HashSet<>();

        for(int i = 0;i < points.length - segment;i+=segment){
            for(int j = i;j < i + segment;j++){
                sb.append(sst.generateGridId(points[j].getLat(),points[j].getLon()));
            }
            String pathKey = sb.toString();
            sb.setLength(0);

            if(lshIndex.get(pathKey) == null){
                //System.out.println(trajectory.getName());
                continue;
            }
            res.addAll(lshIndex.get(pathKey));

        }

        return res;
    }

}