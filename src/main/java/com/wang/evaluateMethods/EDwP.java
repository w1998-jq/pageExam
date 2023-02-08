package com.wang.evaluateMethods;

import com.wang.tra.Point;
import com.wang.tra.TimeComparator;
import com.wang.tra.Trajectory;

import java.util.*;

/**
 * @author jqwang
 * @version 1.0
 * @description: TODO
 * @date 2022/2/8 14:52
 */
public class EDwP {
    public double getEDwP(List<Point> t1, List<Point> t2) {
		if(t1.size() == 0 && t2.size() == 0){
			return 0.0;
		}
		if(t1.size() == 0 || t2.size() == 0){
			return Double.MAX_VALUE;
		}
		// project T2 onto T1
		List<Point> t1_with_projections = project(t2, t1);
		// project T1 onto T2
		List<Point> t2_with_projections = project(t1, t2);
		// both lists will have same size after projections
		int size = t1_with_projections.size();
		// coverage and replacement
		double coverage, replacement;
		double cost;

		// check which edit was cheaper
		double edwp_cost = 0.0;
		for(int i=0; i<size-1; i++){
			// segment e1
			Point e1p1 = t1_with_projections.get(i);
			Point e1p2 = t1_with_projections.get(i+1);
			// segment e2
			Point e2p1 = t2_with_projections.get(i);
			Point e2p2 = t2_with_projections.get(i+1);

			// check if the segments are not already aligned
			coverage = coverage(e1p1, e1p2, e2p1, e2p2);
			if(coverage == 0.0) continue;

			// test t1 onto t2
			double min_cost_e1 = Double.MAX_VALUE;
			for(int j=0; j<t2.size()-1; j++){
				Point p1 = t2.get(j);
				Point p2 = t2.get(j+1);

				replacement = replacement(e1p1, e1p2, p1, p2);
				coverage    = coverage(e1p1, e1p2, p1, p2);
				cost = coverage * replacement;
				if(cost < min_cost_e1 && cost != 0.0){
					min_cost_e1 = cost;
				}
			}
			// test t2 onto t1
			double min_cost_e2 = Double.MAX_VALUE;
			for(int j=0; j<t1.size()-1; j++){
				Point p1 = t1.get(j);
				Point p2 = t1.get(j+1);

				replacement = replacement(e2p1, e2p2, p1, p2);
				coverage    = coverage(e2p1, e2p2, p1, p2);
				cost = coverage * replacement;
				if(cost < min_cost_e2){
					min_cost_e2 = cost;
				}
			}

			// get the cheapest edit
			edwp_cost += Math.min(min_cost_e1, min_cost_e2);
		}

		return edwp_cost;

        /*double edwp_cost = 0;

        if(t1.size() == 0 && t2.size() == 0){
            return 0;
        }
        if(t1.size() == 0 || t2.size() == 0){
            return Double.MAX_VALUE;
        }

        double replacement, coverage;
        double cost_e1, cost_e2;
        Point e1p1, e1p2, e2p1, e2p2;
        Point proj_e1, proj_e2;
        while(true) {
            // Segments being edited
            if (t2.size() == 1 && t1.size() > 1){
                e1p1 = t1.get(0);
                e1p2 = t1.get(1);
                e2p2 = t2.get(0);

                replacement = replacement(e1p1, e1p2, e2p2, e2p2);
                coverage    = coverage(e1p1, e1p2, e2p2, e2p2);
                edwp_cost += (replacement * coverage);
            }
            else if(t1.size() == 1 && t2.size() > 1){
                e2p1 = t2.get(0);
                e2p2 = t2.get(1);
                e1p2 = t1.get(0);

                replacement = replacement(e2p1, e2p2, e1p2, e1p2);
                coverage    = coverage(e2p1, e2p2, e1p2, e1p2);
                edwp_cost += (replacement * coverage);
            }
            else if(t1.size() > 1 && t2.size() > 1){
                e1p1 = t1.get(0);
                e1p2 = t1.get(1);
                e2p1 = t2.get(0);
                e2p2 = t2.get(1);

                // cost of project e2 onto e1
                proj_e1		= projection(e1p1, e1p2, e2p2);
                replacement = replacement(e1p1, proj_e1, e2p1, e2p2);
                coverage	= coverage(e1p1, proj_e1, e2p1, e2p2);
                cost_e1	    = replacement * coverage;

                // cost of project e1 onto e2
                proj_e2 	= projection(e2p1, e2p2, e1p2);
                replacement = replacement(e2p1, proj_e2, e1p1, e1p2);
                coverage	= coverage(e2p1, proj_e2, e1p1, e1p2);
                cost_e2	    = replacement * coverage;

                // check which edit is cheaper
                if(cost_e1 <= cost_e2){
                    // replacement 1 is better
                    edwp_cost += cost_e1;
                    // update T1
                    t1 = insert(t1, proj_e1);
                } else {
                    // replacement 2 is better
                    edwp_cost += cost_e2;
                    // update T2
                    t2 = insert(t2, proj_e2);
                }
            }
            // end
            else {
                break;
            }
            // move forward
            t1 = rest(t1);
            t2 = rest(t2);
        }


        return edwp_cost;*/
    }

    /**
     * Cost of the operation where the segment e1 is matched with e2.
     */
    private double replacement(Point e1p1, Point e1p2, Point e2p1, Point e2p2) {
        // Euclidean distances between segment points
        double dist_p1 = e1p1.dist(e2p1);
        double dist_p2 = e1p2.dist(e2p2);

        // replacement cost
        double rep_cost = dist_p1 + dist_p2;

        return rep_cost;
    }

    /**
     * Coverage: quantifies how representative the segment being
     * edit are of the overall trajectory. Segment e1 and e2.
     * e1 = [e1.p1, e1.p2], e2 = [e2.p1, e2.p2]
     */
    private double coverage(Point e1p1, Point e1p2, Point e2p1, Point e2p2){
        // segments coverage
        double cover = e1p1.dist(e1p2) + e2p1.dist(e2p2);
        return cover;
    }

    /**
     * Calculate the projection of the point p on to the segment
     * e = [e.p1, e.p2]
     */
    private Point projection(Point e_p1, Point e_p2, Point p){
        // square length of the segment
        double len_2 = dotProduct(e_p1, e_p2, e_p1, e_p2);
        // e.p1 and e.p2 are the same point
        if(len_2 == 0){
            return e_p1;
        }

        // the projection falls where t = [(p-e.p1) . (e.p2-e.p1)] / |e.p2-e.p1|^2
        double t = dotProduct(e_p1, p, e_p1, e_p2) / len_2;

        // "Before" e.p1 on the line
        if (t < 0.0) {
            return e_p1;
        }
        // after e.p2 on the line
        if(t > 1.0){
            return e_p2;
        }
        // projection is "in between" e.p1 and e.p2
        // get projection coordinates
        double x = e_p1.getLon() + t*(e_p2.getLon() - e_p1.getLon());
        double y = e_p1.getLat() + t*(e_p2.getLat() - e_p1.getLat());
        int time = (int) (e_p1.getTime() + t*(e_p2.getTime() - e_p1.getTime()));

        return new Point(x, y, time);
    }

    /**
     * Calculates the dot product between two segment e1 and e2.
     */
    private double dotProduct(Point e1_p1, Point e1_p2, Point e2_p1, Point e2_p2){
        // shift the points to the origin - vector
        double e1_x = e1_p2.getLon() - e1_p1.getLon();
        double e1_y = e1_p2.getLat() - e1_p1.getLat();
        double e2_x = e2_p2.getLon() - e2_p1.getLon();
        double e2_y = e2_p2.getLat() - e2_p1.getLat();

        // calculate the dot product
        double dot_product = (e1_x * e2_x) + (e1_y * e2_y);

        return dot_product;
    }

    /**
     * Returns a sub-trajectory containing all segments of
     * the list except the first one.
     */
    private List<Point> rest(List<Point> list){
        if(!list.isEmpty()){
            list.remove(0);
        }
        return list;
    }

    /**
     * Insert the projection p_ins on to the first segment of
     * the trajectory t1.
     *
     * @return The new updates trajectory t1.
     */
    private List<Point> insert(List<Point> t, Point p_ins){
        Point e1_p1 = t.get(0);
        Point e1_p2 = t.get(1);
        // if the projection is not already there,
        // then add the new point
        if(!p_ins.isSamePosition(e1_p1) &&
                !p_ins.isSamePosition(e1_p2)){
            t.add(1, p_ins);
        }
        return t;
    }
    private List<Point> project(List<Point> t1, List<Point> t2){
        List<Point> projList =
                new ArrayList<Point>();
        // find the best projection of every point in t1 onto t2
        double dist;
        Point ep1, ep2, proj;
        for(int i=0; i<t1.size(); i++){
            Point p = t1.get(i);
            double minDist = Double.MAX_VALUE;
            Point bestProj = new Point();
            for(int j=0; j<t2.size()-1; j++){
                // t2 segment
                ep1 = t2.get(j);
                ep2 = t2.get(j+1);
                // projection of p onto e
                proj = projection(ep1, ep2, p);
                // find the best pro
                dist = p.dist(proj);
                if(dist < minDist){
                    minDist = dist;
                    bestProj = proj;
                }
            }
            projList.add(bestProj);
        }
        // update and sort t2
        for(Point p : t2){
            projList.add(p);
        }
        Collections.sort(projList, new TimeComparator<Point>());

        return projList;
    }

    public static void hit(List<Trajectory> trajectories, List<Trajectory> abandon){
        EDwP eDwP = new EDwP();
        int rightCount = 0;

        List<List<Point>> trajectories_list = new ArrayList<>();
        List<List<Point>> abandon_list = new ArrayList<>();

        for(int i = 0;i < trajectories.size();i++){
            Trajectory trajectory = trajectories.get(i);
            Trajectory trajectory_aba = abandon.get(i);

            List<Point> list_tra = new ArrayList<>();
            for(int j = 0;j < trajectory.getPoints().length;j++){
                list_tra.add(trajectory.getPoints()[j]);
            }
            trajectories_list.add(list_tra);

            List<Point> list_aba = new ArrayList<>();
            for(int j = 0;j < trajectory_aba.getPoints().length;j++){
                list_aba.add(trajectory_aba.getPoints()[j]);
            }
            abandon_list.add(list_aba);
            //System.out.println(list_tra.size() + "   " + list_aba.size());
        }
        //System.out.println(abandon_list.size() + "  " + trajectories_list.size());
        long startTime = System.currentTimeMillis();
        for(int i = 0;i < abandon.size();i++){

            if(i % 10 == 0 && i > 0){
                //System.out.print(abandon.get(i).getName() + "  ");
                System.out.println("EDwP 已经计算" + (i) / 10 + "%  命中数量" + rightCount);
            }
            double min = Double.MAX_VALUE;
            int index = -1;
            for(int j = 0;j < trajectories_list.size();j++){
                double eDwP_dis = eDwP.getEDwP(abandon_list.get(i), trajectories_list.get(j));
                if(min > eDwP_dis){
                    min = eDwP_dis;
                    index = j;
                }
            }
            if(trajectories.get(index).getName().equals(abandon.get(i).getName())){
                rightCount++;
            }
        }
        double timeCount =  (System.currentTimeMillis() - startTime)/ 1000;
        System.out.println("EDwP　计算完毕，总耗时：" + timeCount + "s, 平均百条耗时：" + (timeCount)/(trajectories.size()/100) +"s");
        System.out.println("EDwP 命中率为：" + (rightCount / (double)trajectories.size()) + " 命中数量为：" + rightCount);
        System.out.println("==========================================================================");

    }

    public static void main(String[] args) {
        List<Point> t1 = new ArrayList<>();
        t1.add(new Point(0,0,0));
        t1.add(new Point(0,10,30));
        t1.add(new Point(12,0,35));
        List<Point> t2 = new ArrayList<>();
        t2.add(new Point(0,0,0));
        t2.add(new Point(2,10,14));
        t2.add(new Point(12,5,17));



        System.out.println(new EDwP().getEDwP(t1,t2));
    }
    
}