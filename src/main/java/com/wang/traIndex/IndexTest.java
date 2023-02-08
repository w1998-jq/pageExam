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
 * @date 2022/3/5 13:36
 */
public class IndexTest {
    public static void main(String[] args) throws IOException {

        //List<Trajectory> trajectoriesSet = ReadToTra.proToTra("D:\\TraDataSet\\波兰\\data");
        List<Trajectory> trajectoriesSet = ReadToTra.proToTra(args[0]);
        CreateBucket createBucket = new CreateBucket(6, trajectoriesSet,
                trajectoriesSet.size(), 15, 200);
        //List<Trajectory> trajectoriesSet = ReadToTra.proToTra(args[0]);
        //new LSH(2,"boLan",trajectoriesSet,trajectoriesSet.size());
        //new SST(200,"boLan",trajectoriesSet, trajectoriesSet.size());
        /*System.out.println("总轨迹数量为 ：" + trajectoriesSet.size());
        int[] counts = {300000, 500000, 1000000, 1500000, 1700000};
        for (int t = 0; t < counts.length; t++) {
            List<Trajectory> trajectories = new ArrayList<>();
            while (trajectories.size() < 20) {

                int i = new Random().nextInt(counts[t]);
                if (trajectoriesSet.get(i).getPoints().length < 30) {
                    continue;
                }
                trajectories.add(trajectoriesSet.get(i));
            }

            int canCount = 0;
            int hitCount = 0;
            LSH lsh = new LSH(2, "boLan", trajectoriesSet, counts[t]);
            long l1 = System.currentTimeMillis();
            for (int i = 0; i < trajectories.size(); i++) {
                Set<String> set = lsh.select(trajectories.get(i));
                canCount += set.size();
                if (set.contains(trajectories.get(i).getName())) {
                    hitCount++;
                }
            }
            System.out.println("LSH候选轨迹数量平均为：" + canCount / trajectories.size() +
                    "命中数量为:" + hitCount +
                    "共有轨迹数量为：" + trajectories.size());
            canCount = 0;
            hitCount = 0;
            long l2 = System.currentTimeMillis();
            System.out.println("lsh查询" + trajectories.size() + "轨迹耗时" + (l2 - l1) / 1000d + "s");


            SST boLan = new SST(50, "boLan", trajectoriesSet, counts[t]);
            long l3 = System.currentTimeMillis();
            for (int i = 0; i < trajectories.size(); i++) {
                Set<String> set = boLan.selectFromIndex(trajectories.get(i));
                canCount += set.size();
                if (set.contains(trajectories.get(i).getName())) {
                    hitCount++;
                }
            }
            System.out.println("SST候选轨迹数量平均为：" + canCount / trajectories.size() +
                    "命中数量为:" + hitCount +
                    "共有轨迹数量为：" + trajectories.size());
            canCount = 0;
            hitCount = 0;
            long l4 = System.currentTimeMillis();
            System.out.println("sst查询" + trajectories.size() + "轨迹耗时" + (l4 - l3) / 1000d + "s");


            long l = System.currentTimeMillis();
            CreateBucket createBucket = new CreateBucket(1, trajectoriesSet,
                    counts[t], 60, 200);

            long l5 = System.currentTimeMillis();
            System.out.println("索引建立时间为" + (l5 - l) / 1000d + "s");
            for (int i = 0; i < trajectories.size(); i++) {
                Set<String> set = createBucket.select_tra(trajectories.get(i));
                canCount += set.size();
                if (set.contains(trajectories.get(i).getName())) {
                    hitCount++;
                }
            }
            System.out.println("bucket候选轨迹数量平均为：" + canCount / trajectories.size() +
                    "命中数量为:" + hitCount +
                    "共有轨迹数量为：" + trajectories.size());
            canCount = 0;
            hitCount = 0;

            long l6 = System.currentTimeMillis();
            System.out.println("bucket查询" + trajectories.size() + "轨迹耗时" + (l6 - l5) / 1000d + "s");
            System.out.println("=================================================================================================================");

        }*/
    }
}