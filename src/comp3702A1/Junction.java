package comp3702A1;

import java.util.*;

public class Junction {
	private Set<Road> connectedRoads = new HashSet<Road>();
	private Set<String> connectedRoadName = new HashSet<String>();
	private String junctionName;
	
	Junction (String name) {
		this.junctionName = name;
	}
	
	public void addRoad(Road road) {
		connectedRoads.add(road);
		connectedRoadName.add(road.getRoadName());
	}
	
	public boolean containRoad(Road road) {
		return connectedRoads.contains(road);
	}
	
	public Set<Road> getConnectedRoads() {
		return connectedRoads;
	}
	
	public Set<String> getConnectedRoadNames() {
		return connectedRoadName;
	}
	
	public String getJunctionName() {
		return junctionName;
	}
	
	public Road getRoad (String name) {
		for (Road i : connectedRoads) {
			if (i.getRoadName().equals(name)) {
				return i;
			}
		}
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Junction)) {
			return false;
		}
		return ((Junction) obj).getJunctionName().equals(junctionName);
	}
}