package com.wang.traIndex;

import com.wang.dataRead.ReadToTra;
import com.wang.indexStruc.CompressPoint;
import com.wang.tra.Point;
import com.wang.tra.Trajectory;

import java.io.IOException;
import java.util.*;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/3/3 13:28
 */
public class SST {


    Map<Integer, Set<String>> grids;
    int sideLen; //网格宽度
    String path; //数据集目录
    String dataSetName; //数据集名称
    CompressPoint compressPoint;
    public SST(int sideLen,String dataSetName){
        this.sideLen = sideLen;
        this.dataSetName = dataSetName;
        compressPoint = new CompressPoint();
    }
    public SST(int sideLen,String dateSetName,List<Trajectory> trajectoriesSet,int count) {
        this.sideLen = sideLen;
        this.dataSetName = dateSetName;
        compressPoint = new CompressPoint();
        this.grids = mappingToGrid(trajectoriesSet,count);
    }

    /**
     * @description:  将经纬度转换为对应的ID
     * @param: sideLen 网格ID的宽度
     * @param: lat 纬度
     * @param: lon 经度
     * @param: dataSetName 数据集的名称
     * @return: int 网格ID　
     * @author jqwang
     * @date: 2022/3/3 13:34
     */
    public int generateGridId(double lat,double lon){
        double preLat = (double) sideLen/111d * 0.001;
        double preLon = (double) sideLen/85d * 0.001;
        int len;
        if(dataSetName.equals("boLan")){
            len = (int) (6d/preLat);
        }else {
            len = (int) (6d/preLat);
        }
        return (int) ((lat - 37d)/preLat) + (int) ((lon-(-9d))/preLon) * len;

        //return compressPoint.getGeoHashID(lon,lat,7);
    }



    /**
     * @description:
     * @param: sideLen 网格的边长
     * @param: path 数据集路径
     * @param: dateSetName 数据集名称
     * @return:  Map<Integer,Set<String>> grids,网格索引
     * @author jqwang
     * @date: 2022/3/3 13:48
     */
    public Map<Integer, Set<String>> mappingToGrid(List<Trajectory> trajectoriesSet,int count){
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
        Map<Integer, Set<String>> grids = new HashMap<>();

        for(int i = 0;i < count;i++){
            if(i % 100000 == 0){
                System.out.println("已处理" + i + "条轨迹");
            }
            Trajectory trajectory = trajectories.get(i);
            Point[] points = trajectory.getPoints();
            for(int j = 0;j < points.length;j++){
                int gridId = generateGridId(points[j].getLat(),points[j].getLon());
                if(!grids.containsKey(gridId)){
                    Set<String> set = new HashSet<>();
                    grids.put(gridId,set);
                }
                grids.get(gridId).add(trajectory.getName());
            }
        }
        System.out.println("索引建立时间为" + (System.currentTimeMillis() - l) / 1000d + "s");
        return grids;
    }

    public void addToIndex(Trajectory trajectory){
        Point[] points = trajectory.getPoints();
        for(int i = 0;i < points.length;i++){
            int gridID = generateGridId(points[i].getLat(),points[i].getLon());
            if(!grids.containsKey(gridID)){
                Set<String> set = new HashSet<>();
                grids.put(gridID,set);
            }
            grids.get(gridID).add(trajectory.getName());
        }
    }

    public Set<String> selectFromIndex(Trajectory trajectory){
        Set<String> res = new HashSet<>();
        Point[] points = trajectory.getPoints();
        for(int i = 0;i < points.length;i++){
            int gridID = generateGridId(points[i].getLat(),points[i].getLon());
            res.addAll(grids.get(gridID));
        }
        return res;
    }

}