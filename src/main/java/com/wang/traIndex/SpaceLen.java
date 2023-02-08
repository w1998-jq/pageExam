package com.wang.traIndex;

import com.wang.dataRead.ReadToTra;
import com.wang.indexStruc.CreateBucket;
import com.wang.tra.Trajectory;

import java.io.IOException;
import java.util.*;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/3/12 14:14
 */
public class SpaceLen {
    public static void main(String[] args) throws IOException {
        //List<Trajectory> trajectoriesSet = ReadToTra.proToTra(args[0]);
        List<Trajectory> trajectoriesSet = ReadToTra.proToTra("D:\\TraDataSet\\T-drive Taxi Trajectories\\len80");
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
        int[] counts = {50, 100, 200, 400, 500, 600};
        CreateBucket createBucket;
         for (int count : counts) {
            createBucket = null;
            createBucket = new CreateBucket(1, trajectoriesSet,
                    trajectoriesSet.size(), 50, count);

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