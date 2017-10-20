package comp3702A1;

import java.io.*;
import java.util.*;

public class a1_navSystem {
	
	private static Set<Road> environment;
	private static Set<String> junctionNames = new HashSet<String>();
	private static Set<Junction> junctions = new HashSet<Junction>();
	private static Comparator<PriorityEntry> comparator = new PrioEntryComparator();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 3) {
			System.err.println("Incorrect Number of Arguments to start program.\nUsage: program environment_file query_file output_file");
			System.exit(1);
		}
		environment = readEnvironment(args[0]);
		List<String> query = readFile(args[1]);
		for (String i : junctionNames) {
			Junction junc = new Junction(i);
			for (Road j : environment) {
				if (j.getJunction1().equals(i)||j.getJunction2().equals(i)) {
					junc.addRoad(j);
				}
			}
			junctions.add(junc);
		}
		try{
		    PrintWriter writer = new PrintWriter(args[2], "UTF-8");
			for(String i : query) {
				String calculatedRoute = calculateRoute(i);
				System.out.println(calculatedRoute);
				writer.println(calculatedRoute);
			}
		    writer.close();
		} catch (IOException e) {
			
		}
	}
	
	private static Set<Road> readEnvironment(String filename)
	{
		Set<Road> records = new HashSet<Road>();
		  	try
		  	{
		  		BufferedReader reader = new BufferedReader(new FileReader(filename));
		  		String line;
		  		while ((line = reader.readLine()) != null)
			    {
				     String[] parts = line.split(";");
				     Road road = new Road(parts[0].trim(), parts[1].trim(), parts[2].trim(), Integer.parseInt(parts[3].trim()), Integer.parseInt(parts[4].trim()));
				     junctionNames.add(parts[1].trim());
				     junctionNames.add(parts[2].trim());
				     records.add(road);				     
			    }
			    reader.close();
			    return records;
		  	}
		  	catch (Exception e)
		  	{
			    System.err.format("Exception occurred trying to read '%s'.", filename);
			    e.printStackTrace();
			    return null;
		  	}
	}

	private static List<String> readFile(String filename)
	{
	  List<String> records = new ArrayList<String>();
	  try
	  {
	    BufferedReader reader = new BufferedReader(new FileReader(filename));
	    String line;
	    while ((line = reader.readLine()) != null)
	    {
	      records.add(line);
	    }
	    reader.close();
	    return records;
	  }
	  catch (Exception e)
	  {
	    System.err.format("Exception occurred trying to read '%s'.", filename);
	    e.printStackTrace();
	    return null;
	  }
	}
	private static String calculateRoute (String query) {
		String retVal = "";
		String[] parts = query.split(";");
		Path path = new Path();
		if (parts[0].trim().equals(parts[1].trim())) {
			retVal = retVal + "0;" + roadExtractor(parts[0]);
		} else {
			if (Character.isDigit(parts[0].trim().charAt(0))) {
				String startingRoadName = roadExtractor(parts[0]);
				int startingLotNum = lotNumberExtractor(parts[0]);
				String endingRoadName = roadExtractor(parts[1]);
				Road startingRoad = getRoadByName(startingRoadName);
				if (startingRoad == null || !lotNumValid(startingRoad, startingLotNum)) {
					return "no-path";
				}
				if (Character.isDigit(parts[1].trim().charAt(0))) {
					Road endingRoad = getRoadByName(endingRoadName);
					int endingLotNum = lotNumberExtractor(parts[1]);
					if (endingRoad == null || !lotNumValid(endingRoad, endingLotNum)) {
						return "no-path";
					}
					//djikstra road to road
					Path endPath = roadToRoad(startingRoad, startingLotNum, endingRoad, endingLotNum);
					if (endPath == null) {
						retVal = "no-path";
					} else {
						retVal = endPath.stringRepresentation();
					}
						
				} else {
					Junction endingJunction = getJunctionByName(endingRoadName);
					//djikstra road to junction
					Path endPath = roadToJunction(startingRoad, startingLotNum, endingJunction);
					if (endPath == null) {
						retVal = "no-path";
					} else {
						retVal = endPath.stringRepresentation();
					}
				}	
			} else {
				String startingJunctionName = roadExtractor(parts[0]);
				Junction startingJunction = getJunctionByName(startingJunctionName);
				if (!Character.isDigit(parts[1].trim().charAt(0))) {
					String endingJunctionName = roadExtractor(parts[1]);
					if (startingJunctionName.equals(endingJunctionName)) {
						//trivial junction to junction
						retVal = retVal + "0 ; " + startingJunctionName;
					} else {
						Junction endingJunction = getJunctionByName(endingJunctionName);
						//djikstra junction to junction
						Path endPath = junctionToJunction(startingJunction, endingJunction);
						if (endPath == null) {
							retVal = "no-path";
						} else {
							retVal = endPath.stringRepresentation();
						}
					}
				} else {
					String endingRoadName = roadExtractor(parts[1]);
					Road endingRoad = getRoadByName(endingRoadName);
					int endingLotNum = lotNumberExtractor(parts[1]);
					if (endingRoad == null || !lotNumValid(endingRoad, endingLotNum)) {
						return "no-path";
					}
					//djikstra junction to road
					Path endPath = junctionToRoad(startingJunction, endingRoad, endingLotNum);
					if (endPath == null) {
						retVal = "no-path";
					} else {
						retVal = endPath.stringRepresentation();
					}
				}
			}
		}
		return retVal;
	}
	
	private static String roadExtractor (String address) {
		int i = 0;
		while (i < address.trim().length() && Character.isDigit(address.trim().charAt(i))) i++;
		return address.trim().substring(i);
	}
	
	private static int lotNumberExtractor (String address) {
		int i = 0;
		while (i < address.trim().length() && Character.isDigit(address.trim().charAt(i))) i++;
		return Integer.parseInt(address.trim().substring(0, i));
	}
	
	private static Road getRoadByName (String name) {
		for (Road i : environment) {
			if (i.getRoadName().equals(name)) {
				return i;
			}
		}
		return null;
	}
	
	private static Junction getJunctionByName (String name) {
		for (Junction i : junctions) {
			if (i.getJunctionName().equals(name)) {
				return i;
			}
		}
		return null;
	}
	
	private static Path roadToRoad (Road start, int startLot, Road end, int endLot) {
		Junction destination = new Junction("destination");
		HashMap<Junction, Path> visited = new HashMap<Junction, Path>();
		PriorityQueue<PriorityEntry> toBeVisited = new PriorityQueue<PriorityEntry>(comparator);
		Junction startingJunction1 = getJunctionByName(start.getJunction1());
		Path path1 = new Path();
		path1.addInitialLeg(startingJunction1, start, startLot);
		Junction startingJunction2 = getJunctionByName(start.getJunction2());
		Path path2 = new Path();
		path2.addInitialLeg(startingJunction2, start, startLot);
		toBeVisited.add(new PriorityEntry(getJunctionByName(start.getJunction1()), path1));
		toBeVisited.add(new PriorityEntry(getJunctionByName(start.getJunction2()), path2));
		if (start.equals(end)) {
			Path path3 = new Path();
			path3.addSingleRoadJourney(start, startLot, endLot);
			toBeVisited.add(new PriorityEntry(destination, path3));
		}
		while (!toBeVisited.isEmpty()&&!visited.containsKey(destination)) {
			PriorityEntry currentLocation = toBeVisited.remove();
			Path treadedPath = currentLocation.getPath();
			Junction currentJunction = currentLocation.getJunction();
			if (!visited.containsKey(currentJunction)) {
				visited.put(currentJunction, treadedPath);
				for (Road i : currentJunction.getConnectedRoads()) {
					Path path = new Path(treadedPath);
					if (i.equals(end)) {
						path.addFinalSprint(i, endLot);
						toBeVisited.add(new PriorityEntry(destination, path));
					} else {
						Junction nextJunction = getJunctionByName(i.getOtherJunction(currentJunction.getJunctionName()));
						path.addLeg(nextJunction, i);
						if (!visited.containsKey(nextJunction)) {
							toBeVisited.add(new PriorityEntry(nextJunction, path));	
						}
					}
				}
			}
		}
		return visited.get(destination);
	}
	
	private static Path roadToJunction (Road start, int startLot, Junction end) {
		HashMap<Junction, Path> visited = new HashMap<Junction, Path>();
		PriorityQueue<PriorityEntry> toBeVisited = new PriorityQueue<PriorityEntry>(comparator);
		Junction startingJunction1 = getJunctionByName(start.getJunction1());
		Path path1 = new Path();
		path1.addInitialLeg(startingJunction1, start, startLot);
		Junction startingJunction2 = getJunctionByName(start.getJunction2());
		Path path2 = new Path();
		path2.addInitialLeg(startingJunction2, start, startLot);
		toBeVisited.add(new PriorityEntry(getJunctionByName(start.getJunction1()), path1));
		toBeVisited.add(new PriorityEntry(getJunctionByName(start.getJunction2()), path2));
		while (!toBeVisited.isEmpty()&&!visited.containsKey(end)) {
			PriorityEntry currentLocation = toBeVisited.remove();
			Path treadedPath = currentLocation.getPath();
			Junction currentJunction = currentLocation.getJunction();
			visited.put(currentJunction, treadedPath);
			for (Road i : currentJunction.getConnectedRoads()) {
				Path path = new Path(treadedPath);
				Junction nextJunction = getJunctionByName(i.getOtherJunction(currentJunction.getJunctionName()));
				path.addLeg(nextJunction, i);
				if (!visited.containsKey(nextJunction)) {
					toBeVisited.add(new PriorityEntry(nextJunction, path));
				}
			}
		}
		return visited.get(end);
	}
	
	private static Path junctionToRoad (Junction start, Road end, int endLot) {
		Junction destination = new Junction("destination");
		HashMap<Junction, Path> visited = new HashMap<Junction, Path>();
		PriorityQueue<PriorityEntry> toBeVisited = new PriorityQueue<PriorityEntry>(comparator);
		Path initPath = new Path();
		initPath.addSingleJunctionJourney(start);
		toBeVisited.add(new PriorityEntry(start, initPath));
		while (!toBeVisited.isEmpty()&&!visited.containsKey(destination)) {
			PriorityEntry currentLocation = toBeVisited.remove();
			Path treadedPath = currentLocation.getPath();
			Junction currentJunction = currentLocation.getJunction();
			visited.put(currentJunction, treadedPath);
			for (Road i : currentJunction.getConnectedRoads()) {
				Path path = new Path(treadedPath);
				if (i.equals(end)) {
					path.addFinalSprint(i, endLot);
					toBeVisited.add(new PriorityEntry(destination, path));
				} else {
					Junction nextJunction = getJunctionByName(i.getOtherJunction(currentJunction.getJunctionName()));
					path.addLeg(nextJunction, i);
					if (!visited.containsKey(nextJunction)) {
						toBeVisited.add(new PriorityEntry(nextJunction, path));
					}
				}
			}
		}
		return visited.get(destination);
	}
	
	private static Path junctionToJunction (Junction start, Junction end) {
		HashMap<Junction, Path> visited = new HashMap<Junction, Path>();
		PriorityQueue<PriorityEntry> toBeVisited = new PriorityQueue<PriorityEntry>(comparator);
		Path initPath = new Path();
		initPath.addSingleJunctionJourney(start);
		toBeVisited.add(new PriorityEntry(start, initPath));
		while (!toBeVisited.isEmpty()&&!visited.containsKey(end)) {
			PriorityEntry currentLocation = toBeVisited.remove();
			Path treadedPath = currentLocation.getPath();
			Junction currentJunction = currentLocation.getJunction();
			visited.put(currentJunction, treadedPath);
			for (Road i : currentJunction.getConnectedRoads()) {
				Path path = new Path(treadedPath);
				Junction nextJunction = getJunctionByName(i.getOtherJunction(currentJunction.getJunctionName()));
				path.addLeg(nextJunction, i);
				if (!visited.containsKey(nextJunction)) {
					toBeVisited.add(new PriorityEntry(nextJunction, path));
				}
			}
		}
		return visited.get(end);
	}
	
	private static boolean lotNumValid (Road road, int lotNum) {
		return lotNum > 0 && lotNum <= road.getnLots();
	}
}

