package comp3702A1;

import java.util.*;

public class Path{
	
	private List<Junction> junctions = new ArrayList<Junction>();
	private List<Road> roads = new ArrayList<Road>();
	private double distance;
	private int startingLotNumber;
	private int endingLotNumber;
	
	Path() {
		distance = 0;
		startingLotNumber = 0;
		endingLotNumber = 0;
	}
	
	Path(Path treadedPath) {
		junctions.addAll(treadedPath.getTraversedJunctions());
		roads.addAll(treadedPath.getTraversedRoads());
		distance = treadedPath.getTraversedDistance();
		startingLotNumber = treadedPath.getStartingLotNum();
	}
	
	Path(Junction junc, Road road, int lotNum) {
		junctions.add(junc);
		roads.add(road);
		startingLotNumber = lotNum;
		distance = road.getLotToJunctionDistance(startingLotNumber, junc.getJunctionName());
	}
	
	public List<Junction> getTraversedJunctions() {
		return junctions;
	}
	
	public List<Road> getTraversedRoads() {
		return roads;
	}
	
	public double getTraversedDistance() {
		return distance;
	}
	
	public int getStartingLotNum() {
		return startingLotNumber;
	}
	
	public int getEndingLotNum() {
		return endingLotNumber;
	}
	
	public void addDistance (double dist) {
		distance = distance + dist;
	}
	
	public void addLeg (Junction junc, Road road) {
		junctions.add(junc);
		roads.add(road);
		distance = distance + road.getRoadLength();
	}
	
	public void addInitialLeg (Junction junc, Road road, int lotNum) {
		junctions.add(junc);
		roads.add(road);
		startingLotNumber = lotNum;
		distance = distance + road.getLotToJunctionDistance(startingLotNumber, junc.getJunctionName());
	}
	
	public void addFinalSprint (Road road, int lotNum) {
		roads.add(road);
		endingLotNumber = lotNum;
		distance = distance + road.getLotToJunctionDistance(endingLotNumber, junctions.get(junctions.size()-1).getJunctionName());
	}
	
	public void addSingleRoadJourney (Road road, int startLot, int endLot) {
		roads.add(road);
		startingLotNumber = startLot;
		endingLotNumber = endLot;
		distance = distance + (Math.abs(((endLot+1)/2 - (startLot+1)/2)) * road.getLotWidth());
	}
	
	public void addSingleJunctionJourney (Junction junc) {
		junctions.add(junc);
	}
	
	public String stringRepresentation () {
		String retVal = Double.toString(distance) + " ; ";
		if ((!roads.isEmpty())||(!junctions.isEmpty())) {
			if (roads.size() > junctions.size()) {
				retVal = retVal + roads.get(0).getRoadName();
				for (int i = 0; i < junctions.size(); i++) {
					retVal = retVal + "-" + junctions.get(i).getJunctionName() + "-" + roads.get(i+1).getRoadName();
				}
			} else if (roads.size() < junctions.size()) {
				retVal = retVal + junctions.get(0).getJunctionName();
				for (int i = 0; i < roads.size(); i++) {
					retVal = retVal + "-" + roads.get(i).getRoadName() + "-" + junctions.get(i+1).getJunctionName();
				}
			} else {
				if (startingLotNumber == 0) {
					for (int i = 0; i < junctions.size(); i++) {
						retVal = retVal + junctions.get(i).getJunctionName() + "-" + roads.get(i).getRoadName() + "-";
					}
					retVal = retVal.substring(0, retVal.length()-1);
				} else {
					for (int i = 0; i < roads.size(); i++) {
						retVal = retVal + roads.get(i).getRoadName() + "-" + junctions.get(i).getJunctionName() + "-";
					}
					retVal = retVal.substring(0, retVal.length()-1);
				}
			}
		}
		return retVal;
	}
}
