package comp3702A1;

public class PriorityEntry implements Comparable<PriorityEntry>{
	
	private Junction junction;
	private Path path;
	
	public PriorityEntry (Junction junction, Path path) {
		this.junction = junction;
		this.path = path;
	}
	
	public double getDistance () {
		return path.getTraversedDistance();
	}
	
	public Path getPath () {
		return path;
	}
	
	public Junction getJunction () {
		return junction;
	}
	
	@Override
	public int compareTo(PriorityEntry other) {
		if (this.getDistance() < other.getDistance()) {
			return -1;
		} else if (this.getDistance() > other.getDistance()) {
			return 1;
		} else {
			return 0;
		}
	}
}
