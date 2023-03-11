package com.wang.tra;

/**
 * @ClassName Trajectory_shi
 * Description
 * @Author jqWang
 * Date 2023/2/12 11:06
 **/
public class Trajectory_shi {
    private String name;
    private Point_shi[] points;

    public Trajectory_shi() {
    }

    public Trajectory_shi(String name, Point_shi[] points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point_shi[] getPoints() {
        return points;
    }

    public void setPoints(Point_shi[] points) {
        this.points = points;
    }
}
