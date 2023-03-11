package com.wang.traIndex;

import com.wang.MokerTree.MerkelTrie;
import com.wang.MokerTree.TrieNode;
import com.wang.dataRead.ReadToTra;
import com.wang.evaluateMethods.Bucket;
import com.wang.indexStruc.CreateBucket;
import com.wang.tra.Point;
import com.wang.tra.Trajectory;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.lucene.util.RamUsageEstimator;

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
    int timeWidth = 60; //时间槽跨度
    SST sst;
    Map<Integer, Map<String, Set<String>>> bucket;

    public Set<String> getByPath(int flag, String path,Map<Integer, MerkelTrie> bucket) {
        Set<String> set = new HashSet<>();
        if (bucket.containsKey(flag)) {
            set.addAll(bucket.get(flag).searchCan(path));
        }
        return set;
    }

    /**
     *
     * @author jqWang
     * @date 2023/2/28 15:56
    从优化后的树结构中查询
     * @return int
     */
    public int topK_Merkle(Trajectory trajectory, Map<String, Trajectory> trajectoriesMap, int k,Map<Integer, MerkelTrie> bucket){
        sst = new SST(600, "boLan");
        Queue<String[]> heap = new PriorityQueue<>(new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return Double.parseDouble(o1[0]) > Double.parseDouble(o2[0]) ? 1 : 0;
            }
        });
        Point[] points = trajectory.getPoints();
        StringBuilder sb = new StringBuilder();
        int canCount = 0;
        for (int i = 0; i < points.length - segment; i++) {
            sb.append(sst.generateGridId(points[i].getLat(), points[i].getLon()));
            //sb.append(CompressPoint.geoHash(points[i].getLon(),points[i].getLat(),7));
            for (int j = i + 1; j < i + segment; j++) {
                sb.append("-").append(sst.generateGridId(points[j].getLat(), points[j].getLon()));
                //sb.append("-").append(CompressPoint.geoHash(points[j].getLon(),points[j].getLat(),7));
            }
            String subPath = DigestUtils.md5Hex(sb.toString());

            sb.setLength(0);
            int timeFlag = points[i].getTime() / timeWidth;
            int preFlag = timeFlag - 1;
            int afterFlag = timeWidth + 1;
            double min = Double.MAX_VALUE; //最小的轨迹相似度

            Set<String> set = new HashSet<>();

            set.addAll(getByPath(preFlag, subPath,bucket));

            set.addAll(getByPath(timeFlag, subPath,bucket));

            set.addAll(getByPath(afterFlag, subPath,bucket));
            canCount += set.size();
            int count = 0;
            for (String tra : set) {
                count += trajectoriesMap.get(tra).getPoints().length;
                //double realSim = Bucket.Bucket_modif_2(trajectory.getPoints(), trajectoriesMap.get(tra).getPoints());
                //heap.offer(new String[]{realSim + "", tra});
                if (heap.size() < k) {
                    double realSim = Bucket.Bucket_modif_2(trajectory.getPoints(), trajectoriesMap.get(tra).getPoints());
                    double[] doubles = Bucket.bucket_top(trajectory.getPoints(), trajectoriesMap.get(tra).getPoints(), true);
                    min = Math.min(min, doubles[1]);
                    heap.offer(new String[]{realSim + "", tra});
                } else {
                    double[] doubles = Bucket.bucket_top(trajectory.getPoints(), trajectoriesMap.get(tra).getPoints(), true);
                    double max = doubles[0];
                    if (max < min) {
                        continue;
                    }
                    double minTemp = doubles[1];
                    min = Math.max(min, minTemp);
                    double realSim = Bucket.Bucket_modif_2(trajectory.getPoints(), trajectoriesMap.get(tra).getPoints());
                    heap.offer(new String[]{realSim + "", tra});
                }
            }
            //System.out.println("堆中轨迹平均数量为" + count);
        }
        return canCount;
    }

    /** 
     * 
     * @author jqWang
     * @date 2023/3/10 15:51
     合并路径，对相邻时间槽中重复的轨迹ID合并到一起
     */
    public static void merge(Map<Integer, MerkelTrie> bucket){
        Queue<String[]> heap = new PriorityQueue<>(new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return Integer.parseInt(o1[0]) > Integer.parseInt(o2[0]) ? 1 : 0;
            }
        });
        HashMap<String,List<Integer>> MM = new HashMap<>();
        for(Map.Entry<Integer,MerkelTrie> entry : bucket.entrySet()){
            MerkelTrie root = entry.getValue();
            root.getAllPaths(root.getRoot(), heap,new StringBuilder());
            while(!heap.isEmpty()){
                String[] poll = heap.poll();
                String path = poll[0];
                Set<Integer> ids = entry.getValue().search(path);
                int benefit = 0;
                Set<String> merged = new HashSet<>();
                Set<String> neighbor = root.getNeighbor(path);
                while(true){
                    int count = Integer.MIN_VALUE;
                    String max_path = null;
                    for(String p : neighbor){
                        Set<Integer> ids_p = entry.getValue().search(p);
                        ids_p.retainAll(ids);
                        if(ids_p.size() < count){
                            count = ids_p.size();
                            max_path = p;
                        }
                    }
                    int curBenefit = merged.size() * count;
                    if(benefit < curBenefit){
                        benefit = curBenefit;
                        merged.add(max_path);
                        neighbor.addAll(entry.getValue().getNeighbor(max_path));
                        ids.retainAll(entry.getValue().search(max_path));
                    }else{
                        LinkedList<Integer> temp = new LinkedList<>();
                        for(int id : ids){
                            temp.add(id);
                        }
                        for(String p : merged){
                            MM.put(p,temp);
                            entry.getValue().delete(p,ids);
                        }
                        break;
                    }
                }
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        //List<Trajectory> trajectoriesSet = ReadToTra.proToTra(args[0]);
        //List<Trajectory> trajectoriesSet = ReadToTra.proToTra("D:\\TrajectoryDataset\\NewYork\\len80");
        List<Trajectory> trajectoriesSet = ReadToTra.geoLifeToTra("D:\\TrajectoryDataset\\geoline");
        //List<Trajectory> trajectoriesSet = ReadToTra.proToTra("D:\\didi\\DiDiData");
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

        //int[] counts = {50,100,200,400,500,600};
        //int[] counts = {600,500,400,200,100,50};
        int[] counts = {50,80,110,140,170,200};
        Map<Integer, MerkelTrie> bucket = new HashMap<>();
        for(int count : counts){
            CreateBucket createBucket = new CreateBucket(3, trajectoriesSet,
                    trajectoriesSet.size(), count, 600, bucket);
            String size1 = RamUsageEstimator.humanSizeOf(bucket);

            System.out.println("count = "+ count + " 时内存消耗大小为："+ size1);
            bucket = new HashMap<>();
            int Num = 0;
            for(Map.Entry<Integer, MerkelTrie> entry : bucket.entrySet()){
                Num += entry.getValue().getCount();
            }
            System.out.println("树节点数量为："+ Num);


            long time = 0l;
            for(int i = 0;i < trajectories.size();i++){
                long l = System.currentTimeMillis();
                new MokerTree().topK_Merkle(trajectories.get(i),map,20,bucket);
                long selectTime = System.currentTimeMillis() - l;
                time += selectTime;
            }

            long junTime = time/ trajectories.size();
            double fangCha = 0.0;

            System.out.println("滑动窗口长度为：" + count  +
                    "时候，top_k查询耗时平均耗时 :" + junTime + "ms，方差为" + fangCha);
        }

    }
}