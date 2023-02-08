package com.wang.indexStruc;


import com.wang.MokerTree.Trie;
import com.wang.evaluateMethods.Bucket;
import com.wang.tra.Point;
import com.wang.tra.Trajectory;
import com.wang.traIndex.SST;
import com.wang.utils.MapValueComparator;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/1/15 13:55
 */
public class CreateBucket {
    int segment; // 轨迹段
    int timeWidth; //时间槽跨度
    SST sst;
    Map<Integer, Map<String, Set<String>>> bucket;

    public CreateBucket(int segment, List<Trajectory> trajectories, int count, int timeWidth, int spaceLan) {
        this.segment = segment;
        this.timeWidth = timeWidth;
        bucket = new HashMap<>();
        sst = new SST(spaceLan, "boLan");
        createBucket(trajectories, count);
    }

    public CreateBucket(int segment, List<Trajectory> trajectories, int count, int timeWidth, int spaceLan,Map<Integer, Trie> bucket) {
        this.segment = segment;
        this.timeWidth = timeWidth;
        bucket = new HashMap<>();
        sst = new SST(spaceLan, "boLan");
        createBucket_Tree(trajectories, count,bucket);
    }

    /**
     * 使用 Map按value进行排序
     *
     * @param oriMap
     * @return
     */
    public Map<String, Integer> sortMapByValue(Map<String, Integer> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(
                oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());

        Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();
        Map.Entry<String, Integer> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }


    public String merge(String[] path, int k) {
        StringBuilder sb = new StringBuilder();
        sb.append(path[0]);
        for (int i = 1; i < path.length; i++) {
            if (i == k) {
                continue;
            }
            sb.append("-").append(path[i]);
        }
        return sb.toString();
    }

    public String[] getFuzzyPath(String path, int count) {

        String[] split = path.split("-");
        if (count == 0 || split.length <= 2) {
            return new String[]{path};
        }
        String[] res = new String[split.length - 2];

        for (int i = 1; i < split.length - 1; i++) {
            res[i - 1] = (merge(split, i));
        }
        return res;
    }

    public Map<String, Integer> mergeBucket(Map<Integer, Map<String, Set<String>>> bucket) {
        Map<String, Integer> res = new HashMap<>();
        for (Map.Entry<Integer, Map<String, Set<String>>> integerMapEntry : bucket.entrySet()) {
            //遍历时间分桶
            Map<String, Set<String>> value = integerMapEntry.getValue();
            for (Map.Entry<String, Set<String>> entry : value.entrySet()) {
                //遍历轨迹分桶
                List<String> list = new ArrayList<>();
                list.addAll(entry.getValue());

                for (int i = 0; i < list.size(); i++) {
                    for (int j = i + 1; j < list.size(); j++) {
                        String key = list.get(i) + "#" + list.get(j);
                        String key1 = list.get(j) + "#" + list.get(i);

                        if (res.containsKey(key)) {
                            res.put(key, res.getOrDefault(key, 0) + 1);
                        } else if (res.containsKey(key1)) {
                            res.put(key1, res.getOrDefault(key1, 0) + 1);
                        } else {
                            res.put(key, 1);
                        }
                    }
                }
            }
        }
        System.out.println("伴随用户数量：" + res.size());
        return res;
    }

    public Set<String> select_tra(Trajectory trajectory) throws IOException {
        Set<String> res = new HashSet<>();
        //List<Trajectory> trajectories = ReadToTra.proToTra("D:\\TraDataSet\\波兰\\data");
        //List<Trajectory> trajectories = ReadToTra.readFromFile("D:\\TraDataSet\\T-drive Taxi Trajectories\\len30");

        Point[] points = trajectory.getPoints();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < points.length - segment; i++) {
            sb.append(sst.generateGridId(points[i].getLat(), points[i].getLon()));
            //sb.append(CompressPoint.geoHash(points[i].getLon(),points[i].getLat(),7));
            for (int j = i + 1; j < i + segment; j++) {
                sb.append("-").append(sst.generateGridId(points[j].getLat(), points[j].getLon()));
                //sb.append("-").append(CompressPoint.geoHash(points[j].getLon(),points[j].getLat(),7));
            }
            String subPath = sb.toString();
            sb.setLength(0);

            int time = points[i].getTime();
            int flag = time / timeWidth;
            res.addAll(getByPath(flag, subPath));
            res.addAll(getByPath(flag - 1, subPath));
            res.addAll(getByPath(flag + 1, subPath));
        }
        return res;
    }

    public Map<Integer, Map<String, Set<String>>> createBucket(List<Trajectory> trajectories, int count) {

        for (int i = 0; i < count; i++) {
            if (i % 100000 == 0) {
                System.out.println("剩余" + (trajectories.size() - i) + " 轨迹");
            }

            Trajectory trajectory = trajectories.get(i);

            Point[] points = trajectory.getPoints();

            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < points.length - segment; j++) {
                sb.append(sst.generateGridId(points[j].getLat(), points[j].getLon()));
                //sb.append(CompressPoint.geoHash(points[j].getLon(),points[j].getLat(),7));
                for (int k = j + 1; k < j + segment; k++) {
                    sb.append("-").append(sst.generateGridId(points[k].getLat(), points[k].getLon()));
                    //sb.append("-").append(CompressPoint.geoHash(points[k].getLon(),points[k].getLat(),7));
                }
                //md.update(sb.toString().getBytes());
                //String subPath = sb.toString();
                String subPath = DigestUtils.md5Hex(sb.toString());
                sb.setLength(0);

                int timeFlag = points[j].getTime() / timeWidth;
                if (!bucket.containsKey(timeFlag)) {
                    Map<String, Set<String>> stringSetMap = new HashMap<>();
                    bucket.put(timeFlag, stringSetMap);
                }
                Map<String, Set<String>> stringSetMap = bucket.get(timeFlag);

                if (!stringSetMap.containsKey(subPath)) {
                    Set<String> set = new HashSet<>();
                    stringSetMap.put(subPath, set);
                }
                Set<String> set = stringSetMap.get(subPath);
                set.add(trajectories.get(j).getName());

            }
        }
        return bucket;
    }

    public Map<Integer, Trie> createBucket_Tree(List<Trajectory> trajectories, int count,Map<Integer,Trie> bucket) {

        for (int i = 0; i < count; i++) {
            if (i % 100000 == 0) {
                System.out.println("剩余" + (trajectories.size() - i) + " 轨迹");
            }

            Trajectory trajectory = trajectories.get(i);

            Point[] points = trajectory.getPoints();

            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < points.length - segment; j++) {
                sb.append(sst.generateGridId(points[j].getLat(), points[j].getLon()));
                //sb.append(CompressPoint.geoHash(points[j].getLon(),points[j].getLat(),7));
                for (int k = j + 1; k < j + segment; k++) {
                    sb.append("-").append(sst.generateGridId(points[k].getLat(), points[k].getLon()));
                    //sb.append("-").append(CompressPoint.geoHash(points[k].getLon(),points[k].getLat(),7));
                }
                //md.update(sb.toString().getBytes());
                String subPath = sb.toString();
                //String subPath = DigestUtils.md5Hex(sb.toString());
                sb.setLength(0);

                int timeFlag = points[j].getTime() / timeWidth;
                if (!bucket.containsKey(timeFlag)) {
                    Trie root = new Trie();
                    bucket.put(timeFlag, root);
                }
                bucket.get(timeFlag).insert(subPath);

            }
        }
        return bucket;
    }


    public Set<String> getByPath(int flag, String path) {
        Set<String> set = new HashSet<>();
        if (bucket.containsKey(flag)) {
            if (bucket.get(flag).containsKey(path)) {
                set.addAll(bucket.get(flag).get(path));
            }
        }
        return set;
    }

    public int top_k(Trajectory trajectory, Map<String, Trajectory> trajectoriesMap, int k) {
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
            set.addAll(getByPath(timeFlag, subPath));
            set.addAll(getByPath(preFlag, subPath));
            set.addAll(getByPath(afterFlag, subPath));
            canCount += set.size();
            for (String tra : set) {
                double realSim = Bucket.Bucket_modif_2(trajectory.getPoints(), trajectoriesMap.get(tra).getPoints());
                heap.offer(new String[]{realSim + "", tra});
                /*if (heap.size() < k) {
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
                }*/
            }
        }
        //System.out.println("堆中轨迹平均数量为" + heap.size());
        return canCount;
    }
}