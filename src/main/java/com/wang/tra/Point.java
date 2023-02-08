package com.wang.tra;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/1/10 12:56
 */
public class Point {
    private double lon; //经度
    private double lat; //纬度
    private int time; //当前时间戳
    private static double EARTH_RADIUS = 6371000; // 赤道半径(单位m)

    /**
     * 转化为弧度(rad)
     */
    private double rad(double d) {
        return d * Math.PI / 180.0;
    }
    public int getTimeDif(Point point){
        return Math.abs(this.time - point.getTime());
    }

    /**
     * @description: 计算两个点之间的空间距离
     * @param: point
     * @return: double
     * @author jqwang
     * @date: 2022/1/10 16:26
     */
    public double getSpaceDif(Point point){
        double radLat1 = rad(this.lat);
        double radLat2 = rad(point.getLat());
        double a = radLat1 - radLat2;
        double b = rad(this.lon) - rad(point.getLon());
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public double dist(Point point){
        double dis = (point.getLat() - this.lat) * (point.getLat() - this.lat) +
                (point.getLon() - this.lon) * (point.getLon() - this.lon);
        return Math.sqrt(dis);
    }

    public Point() {
    }

    public Point(double lon, double lat ,int time) {
        this.lon = lon;
        this.lat = lat;
        this.time = time;
    }

    public boolean isSamePosition(Point point){
        return (point.getLat() == this.lat && point.getLon() == this.lon) ? true : false;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}