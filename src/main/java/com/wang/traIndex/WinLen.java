package com.wang.traIndex;

import com.wang.dataRead.ReadToTra;
import com.wang.indexStruc.CreateBucket;
import com.wang.tra.Trajectory;

import java.io.IOException;
import java.util.*;

/**
 * 调整滑动窗口的长度以及模糊数量
 *
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/3/11 20:09
 */
public class WinLen {
    /**
     * @description:  滑动窗口长度调整作为参数
     * @param: args
     * @return: void
     * @author jqwang
     * @date: 2022/6/6 20:43
     */
    public static void main(String[] args) throws IOException {
        //List<Trajectory> trajectoriesSet = ReadToTra.proToTra(args[0]);
        List<Trajectory> trajectoriesSet = ReadToTra.proToTra(
                "D:\\TraDataSet\\T-drive Taxi Trajectories\\len80");
        //轨迹ID ：轨迹
        Map<String,Trajectory> map = new HashMap<>();
        for(int i = 0;i < trajectoriesSet.size();i++){
            map.put(trajectoriesSet.get(i).getName(),trajectoriesSet.get(i));
        }

        List<Trajectory> trajectories = new ArrayList<>();
        while(trajectories.size() < 100){
            int i = new Random().nextInt(trajectoriesSet.size());
            if(trajectoriesSet.get(i).getPoints().length < 70 || trajectoriesSet.get(i).getPoints().length >90){
                continue;
            }
            trajectories.add(trajectoriesSet.get(i));
        }
        //int[] counts = {1,2,3,4,5,6};
        int[] counts = {6};
        for(int count : counts){
            CreateBucket createBucket = new CreateBucket(count,trajectoriesSet,
                    trajectoriesSet.size(),30,200);

            List<Long> timeList = new ArrayList<>();
            long time = 0l;
            for(int i = 0;i < trajectories.size();i++){
                long l = System.currentTimeMillis();
                createBucket.top_k(trajectories.get(i),map,10);
                long selectTime = System.currentTimeMillis() - l;
                timeList.add(selectTime);
                time += selectTime;
            }

            long junTime = time/ trajectories.size();
            double fangCha = 0.0;
            for(int i = 0;i < timeList.size();i ++){
                fangCha += (junTime - timeList.get(i))*(junTime - timeList.get(i));
            }
            fangCha = fangCha/timeList.size();
            System.out.println("滑动窗口长度为：" + count  +
                    "时候，top_k查询耗时平均耗时 :" + junTime + "ms，方差为" + fangCha);
        }
    }
}