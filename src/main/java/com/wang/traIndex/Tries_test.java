package com.wang.traIndex;

import com.wang.MokerTree.Tries;
import com.wang.dataRead.ReadToTra;
import com.wang.indexStruc.CreateBucket;
import com.wang.tra.Point;
import com.wang.tra.Trajectory;
import org.apache.lucene.util.RamUsageEstimator;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * @ClassName Tries_test
 * Description
 * @Author jqWang
 * Date 2023/2/24 20:56
 **/
public class Tries_test {
    @Test
    public void getMem(){
        LinkedList<Integer> list = new LinkedList<>();
        //Set<Integer> list = new HashSet<>();
        for(int i = 0;i < 210000;i ++){
            list.add(i);
        }
        String size1 = RamUsageEstimator.humanSizeOf(list);
        System.out.println("内存消耗大小为："+ size1);
    }

    /**
     *
     * @author jqWang
     * @date 2023/2/24 21:40
     * @param path
     统计前缀树中节点数量
     */
    public static void getTriesNode(String path) throws IOException {
        List<Trajectory> trajectoriesSet = ReadToTra.proToTra(path);
        //List<Trajectory> trajectoriesSet = ReadToTra.geoLifeToTra(path);
        //轨迹ID ：轨迹
        Map<String,Trajectory> map = new HashMap<>();
        for(int i = 0;i < trajectoriesSet.size();i++){
            map.put(trajectoriesSet.get(i).getName(),trajectoriesSet.get(i));
        }
        //int[] counts = {2,3,4,5,6};
        int[] counts = {600,500,400,200,100};
        //int[] counts = {50,80,110,140,170,200};

        Map<Integer, Tries> bucket = new HashMap<>();
        for(int count : counts){
            new CreateBucket(3,trajectoriesSet,
                    trajectoriesSet.size(),140,count,bucket,"is");
            int Num = 0;
            for(Map.Entry<Integer,Tries> entry : bucket.entrySet()){
                Num += entry.getValue().getCount();
            }
            String size1 = RamUsageEstimator.humanSizeOf(bucket);
            System.out.println("count = "+ count + " 时内存消耗大小为："+ size1);
            bucket = new HashMap<>();

        }
    }

    /**
     *
     * @author jqWang
     * @date 2023/2/24 21:41
     * @param path
     计算所有节点数量
     */
    public static void getPriH(String path) throws IOException {
        List<Trajectory> trajectoriesSet = ReadToTra.proToTra(path);
        //List<Trajectory> trajectoriesSet = ReadToTra.geoLifeToTra(path);
        SST sst = new SST(600, "boLan");
        //轨迹ID ：轨迹

        int[] counts = {2,3,4,5,6};
        for(int count : counts){
            long allNode = 0;
            for (int i = 0; i < trajectoriesSet.size(); i++) {
                Trajectory trajectory = trajectoriesSet.get(i);
                Point[] points = trajectory.getPoints();
                for (int j = 0; j < points.length - count; j++) {
                    allNode += count;
                }
            }

            System.out.println(  "所有结点数量为："+ allNode);
        }
    }

    public static void main(String[] args) throws IOException {
        //getPriH(args[0]);
        //getPriH("D:\\TrajectoryDataset\\geoline");
        //getTriesNode("D:\\TrajectoryDataset\\geoline");
        getTriesNode(args[0]);

    }
}
