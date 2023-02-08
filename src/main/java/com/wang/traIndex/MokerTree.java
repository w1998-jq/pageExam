package com.wang.traIndex;

import com.wang.MokerTree.Trie;
import com.wang.dataRead.ReadToTra;
import com.wang.indexStruc.CreateBucket;
import com.wang.tra.Trajectory;

import java.io.IOException;
import java.util.*;

/**
 使用树结构构建轨迹索引
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/12/10 12:12
 */
public class MokerTree {
    int segment; // 轨迹段
    int timeWidth; //时间槽跨度
    SST sst;
    Map<Integer, Map<String, Set<String>>> bucket;

    public static void main(String[] args) throws IOException {
        List<Trajectory> trajectoriesSet = ReadToTra.proToTra(args[0]);
        //List<Trajectory> trajectoriesSet = ReadToTra.proToTra("D:\\TraDataSet\\T-drive Taxi Trajectories\\len80");
        //轨迹ID ：轨迹
        Map<String,Trajectory> map = new HashMap<>();
        for(int i = 0;i < trajectoriesSet.size();i++){
            map.put(trajectoriesSet.get(i).getName(),trajectoriesSet.get(i));
        }

        int[] counts = {6};
        Map<Integer, Trie> bucket = new HashMap<>();
        for(int count : counts){
            CreateBucket createBucket = new CreateBucket(count,trajectoriesSet,
                    trajectoriesSet.size(),30,200,bucket);
        }

        while(true){

        }

    }
}