package com.wang.traIndex;

import com.wang.MokerTree.MerkelTrie;
import com.wang.dataRead.ReadToTra;
import com.wang.indexStruc.CreateBucket;
import com.wang.tra.Trajectory_shi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 测试十所内存大小
 * @ClassName MokerTree_Shi
 * Description
 * @Author jqWang
 * Date 2023/2/12 11:10
 **/
public class MokerTree_Shi {
    int segment; // 轨迹段
    int timeWidth; //时间槽跨度
    SST sst;
    Map<Integer, Map<String, Set<String>>> bucket;

    public static void main(String[] args) throws IOException {
        List<Trajectory_shi> trajectoriesSet = ReadToTra.shiSuoToTra(args[0]);
        //List<Trajectory> trajectoriesSet = ReadToTra.proToTra("D:\\TrajectoryDataset\\NewYork\\len80");
        //轨迹ID ：轨迹
        Map<String,Trajectory_shi> map = new HashMap<>();
        for(int i = 0;i < trajectoriesSet.size();i++){
            map.put(trajectoriesSet.get(i).getName(),trajectoriesSet.get(i));
        }

        int[] counts = {3};
        Map<Integer, MerkelTrie> bucket = new HashMap<>();
        for(int count : counts){
            CreateBucket createBucket = new CreateBucket(count,trajectoriesSet,
                    trajectoriesSet.size(),5,200,bucket,true);
        }

    }
}
