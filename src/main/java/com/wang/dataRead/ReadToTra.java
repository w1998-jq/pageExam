package com.wang.dataRead;

import com.csvreader.CsvReader;
import com.wang.tra.Point;
import com.wang.tra.Trajectory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author jqwang
 * @version 1.0
 * @description: 将原始文件读为 Point 列表
 * @date 2022/1/10 20:12
 */
public class ReadToTra {

    /**
     * @description: 从原始下载文件中将数据读取为指定的格式文件
     * @param: path
     * @return: void
     * @author jqwang
     * @date: 2022/1/11 12:56
     */
    public static Map<String, String> readToFile(String path) throws IOException {
        // 第一参数：读取文件的路径 第二个参数：分隔符（不懂仔细查看引用百度百科的那段话） 第三个参数：字符集
        CsvReader csvReader = new CsvReader(path, ',', Charset.forName("UTF-8"));

        // 如果你的文件没有表头，这行不用执行
        // 这行不要是为了从表头的下一行读，也就是过滤表头
        csvReader.readHeaders();
        int count = 0;
        // 读取每行的内容
        Map<String,String> map = new HashMap<>();
        while (csvReader.readRecord()) {
            String trip_id = csvReader.get("TRIP_ID");
            String timestamp = csvReader.get("TIMESTAMP");
            String polyline = csvReader.get("POLYLINE");
            map.put(trip_id + "#" + timestamp ,polyline);
            if(map.size() % 100000 == 0){
                System.out.println(map.size());
            }
        }
        csvReader.close();
        return map;
    }

    public static int stampToSecond(long stamp,int i){
        stamp = stamp + (i - 1) * 15;
        Date date = new Date(stamp);
        return date.getHours() * 3600 + date.getMinutes() * 60 + date.getSeconds();
    }
    /**
     * @description:  将波兰数据集中的轨迹读为  Trajectory 实例对象
     * @param: path
     * @return: java.util.Map<java.lang.String,com.wang.tra.Point[]>
     * @author jqwang
     * @date: 2022/1/11 14:56
     */
    public static List<Trajectory> proToTra(String path) throws IOException {
        LineIterator it = FileUtils.lineIterator(new File(path));
        List<Trajectory> res = new ArrayList<>();
        int count = 0;
        while (it.hasNext()){
            String[] split = it.next().split("\t");
            String name_time = split[0];
            long time = Long.parseLong(name_time.split("#")[1]) * 1000;
            Point[] tra = new Point[split.length - 1];
            for(int i = 1;i < split.length;i++){
                String[] lat_lon = split[i].split("#"); //经纬度
                Point point = new Point(Double.parseDouble(lat_lon[0]),
                                        Double.parseDouble(lat_lon[1]),
                                   stampToSecond(time,i));

                tra[i - 1] = point;
            }
            Trajectory trajectory = new Trajectory(name_time.split("#")[0], tra);
            res.add(trajectory);
            count++;
            if(count % 100000 == 0){
                System.out.println("已读取轨迹数量：" + count);
            }
        }
        return res;
    }

    /**
     * @description:  将滴滴数据集读取为 Trajectory 对象
     * @param: path
     * @return: java.util.List<com.wang.tra.Trajectory>
     * @author jqwang
     * @date: 2022/1/11 15:20
     */
    public static List<Trajectory> didiToTra(String path) throws IOException {
        LineIterator it = FileUtils.lineIterator(new File(path));
        String name = null;
        List<Point> points = new ArrayList<>();

        List<Trajectory> res = new ArrayList<>();
        int counts = 0;
        while(it.hasNext()){
            String[] line = it.next().split(",");
            if(name == null){
                name = line[0];
            }
            Point point = new Point(Double.parseDouble(line[3]), Double.parseDouble(line[4]), Integer.parseInt(line[2]));
            if(!name.equals(line[0])){
                counts ++;
                Point[] p = new Point[points.size()];
                for(int i = 0;i < p.length;i++){
                    p[i] = points.get(i);
                }
                Trajectory trajectory = new Trajectory(name, p);
                res.add(trajectory);
                name = line[0];
                points.clear();
                points = new ArrayList<>();
            }else{
                points.add(point);
            }
        }
        System.out.println(counts);
        System.out.println(res.size());
        return res;
    }


    /**
     * @description:  将newYork轨迹数据集处理为指定格式文件
     * @param: path
     * @return: java.util.List<com.wang.tra.Trajectory>
     * @author jqwang
     * @date: 2022/1/19 16:12
     */
    public static void newYorkToTra(String path) throws IOException, ParseException {
        List<Trajectory> res = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        int lent = 0;
        int nameIndex = 1;
        for(int i = 0;i < files.length;i++){
            LineIterator it = FileUtils.lineIterator(files[i]);
            List<Point> list = new ArrayList<>();
            while(it.hasNext()){
                String line = it.next();
                String[] split = line.split(",");
                if(Double.parseDouble(split[2]) > 180d || Double.parseDouble(split[2]) < -180d
                     ||Double.parseDouble(split[3]) > 90d || Double.parseDouble(split[3]) < -90d){
                    continue;
                }
                long time = timeToStamp(split[1]);
                Point point = new Point(Double.parseDouble(split[2]), Double.parseDouble(split[3]),stampToSecond(time,1));
                list.add(point);
            }
            int traCount = list.size()/80;
            for(int j = 0;j < traCount - 1;j++){
                Point[] points = new Point[80];
                int temp = 0;
                for(int s = j * 80; s < (j + 1) * 80;s ++){
                    points[temp++] = list.get(s);
                }
                res.add(new Trajectory(nameIndex + "",points));
                nameIndex++;
                lent += 80;
            }
            if(traCount == 0){
                Point[] points = new Point[list.size()];
                for(int j = 0;j < list.size();j++){
                    points[j] = list.get(j);
                }
                res.add(new Trajectory(nameIndex + "",points));
                nameIndex++;
                lent += list.size();
            }else{
                Point[] points = new Point[list.size()%80 + 80];
                int temp = 0;
                for(int j = (traCount - 1) * 80;j < list.size();j++){
                    points[temp ++] = list.get(j);
                }
                res.add(new Trajectory(nameIndex + "",points));
                nameIndex++;
                lent += (list.size()%80 + 80);
            }
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\TraDataSet\\T-drive Taxi Trajectories\\len80"));
        for(int i = 0;i < res.size();i++){
            Trajectory trajectory = res.get(i);
            Point[] points = trajectory.getPoints();
            if(points.length < 1){
                continue;
            }
            bw.write(trajectory.getName()+ "#" + points[0].getTime());
            for(int j = 0;j < points.length;j++){
                bw.write("\t" + points[j].getLon() + "#" + points[j].getLat());
            }
            bw.write("\n");
        }
        bw.flush();
        bw.close();
        System.out.println("轨迹数量为：" + res.size() +  "平均轨迹长度为：" + (lent/ res.size()));
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if(date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if(cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static void newYorkToTra_byDay(String path) throws IOException, ParseException {
        List<Trajectory> res = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        int lent = 0;
        int nameIndex = 1;
        List<Point> list = new ArrayList<>();
        for(int i = 0;i < files.length;i++){
            String preDay = null;
            LineIterator it = FileUtils.lineIterator(files[i]);
            while(it.hasNext()){
                String line = it.next();
                String[] split = line.split(",");
                if(Double.parseDouble(split[2]) > 180d || Double.parseDouble(split[2]) < -180d
                        ||Double.parseDouble(split[3]) > 90d || Double.parseDouble(split[3]) < -90d){
                    continue;
                }
                String[] ddd = split[1].split(" ");
                if(preDay == null){
                    preDay = ddd[0];
                    long time = timeToStamp(split[1]);
                    Point point = new Point(Double.parseDouble(split[2]), Double.parseDouble(split[3]),stampToSecond(time,1));
                    list.add(point);
                }else if(!preDay.equals(ddd[0])){
                    Point[] points = new Point[list.size()];
                    for(int j = 0;j < list.size();j++){
                        points[j] = list.get(j);
                    }
                    res.add(new Trajectory(nameIndex + "",points));
                    nameIndex++;
                    lent += list.size();
                    list.clear();
                    preDay = ddd[0];
                    long time = timeToStamp(split[1]);
                    Point point = new Point(Double.parseDouble(split[2]), Double.parseDouble(split[3]),stampToSecond(time,1));
                    list.add(point);
                }else{
                    long time = timeToStamp(split[1]);
                    Point point = new Point(Double.parseDouble(split[2]), Double.parseDouble(split[3]),stampToSecond(time,1));
                    list.add(point);
                }
            }
            if(list.size() > 0){
                Point[] points = new Point[list.size()];
                for(int j = 0;j < list.size();j++){
                    points[j] = list.get(j);
                }
                res.add(new Trajectory(nameIndex + "",points));
                nameIndex++;
                lent += list.size();
                list.clear();
            }

        }

        BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\TraDataSet\\T-drive Taxi Trajectories\\按日期划分"));
        for(int i = 0;i < res.size();i++){
            Trajectory trajectory = res.get(i);
            bw.write(trajectory.getName());
            Point[] points = trajectory.getPoints();
            for(int j = 0;j < points.length;j++){
                bw.write("\t" + points[j].getLon() + "#" + points[j].getLat() + "#" + points[j].getTime());
            }
            bw.write("\n");
        }
        bw.flush();
        bw.close();
        System.out.println("轨迹数量为：" + res.size() +  "平均轨迹长度为：" + (lent/ res.size()));
    }

    public static List<Trajectory> readFromFile(String path) throws IOException {
        List<Trajectory> res = new ArrayList<>();
        LineIterator it = FileUtils.lineIterator(new File(path));
        int count = 0;
        while(it.hasNext()){
            String line = it.next();
            String[] split = line.split("\t");
            List<Point> list = new ArrayList<>();
            for(int i = 1;i < split.length;i++){
                String[] point_temp = split[i].split("#");
                list.add(new Point(Double.parseDouble(point_temp[0]),Double.parseDouble(point_temp[1]),Integer.parseInt(point_temp[2])));
            }
            Point[] points = new Point[list.size()];
            for(int i = 0;i < points.length;i++){
                points[i] = list.get(i);
            }
            res.add(new Trajectory(split[0],points));

        }
        return  res;
    }
    public static long timeToStamp(String dateStr) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        Date date = dateFormat.parse(dateStr);
        return date.getTime();
    }
    public static void main(String[] args) throws IOException, ParseException {
        /*Map<String, String> map = readToFile("D:\\TraDataSet\\波兰\\train(1).csv");
        BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\TraDataSet\\波兰\\data"));
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, String> entry = it.next();
            String polyline = entry.getValue();
            //System.out.println(polyline);
            String[] split = polyline.split(",");
            bw.write(entry.getKey() + "\t");
            StringBuilder sb = new StringBuilder();
            for(int i = 0;i < split.length;i++){
                split[i] = split[i].replace("[", "");
                split[i] = split[i].replace("]", "");
                if(i % 2 ==0){
                    sb.append(split[i]).append("#");
                }else{
                    sb.append(split[i]);
                    bw.write(sb.toString() + "\t");
                    sb.setLength(0);
                }
            }
            bw.write("\n");
            it.remove();
        }
        bw.flush();
        bw.close();*/

        //didiToTra("D:\\TraDataSet\\滴滴\\gps\\gps_20161101");

        /*LineIterator it = FileUtils.lineIterator(new File("D:\\TraDataSet\\滴滴\\gps\\gps_20161101"));
        int lineCount = 0;
        while(it.hasNext() && lineCount < 20 ){
            String line = it.next();
            System.out.println(line);
            lineCount ++;
        }
        System.out.println(lineCount);*/
        newYorkToTra("D:\\TraDataSet\\T-drive Taxi Trajectories\\release\\taxi_log_2008_by_id");

    }
}