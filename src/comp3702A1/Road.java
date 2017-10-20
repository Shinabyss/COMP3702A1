package comp3702A1;

public class Road {
	private String roadName;
	private String junction1;
	private String junction2;
	private int nLots;
	private int roadLength;
	private double lotWidth;
	
	Road(String name, String intersection1, String intersection2, int length, int lots ) {
		
		this.roadName = name;
		this.junction1 = intersection1;
		this.junction2 = intersection2;
		this.roadLength = length;
		this.nLots = lots;
		this.lotWidth = roadLength / ((double) nLots/2);
		
	}
	
	public void setRoadName (String name) {
		roadName = name;
	}
	
	public void setJunction1 (String intersection1) {
		junction1 = intersection1;
	}
	
	public void setJunction2 (String intersection2) {
		junction2 = intersection2;
	}
	
	public void setRoadLength (int length) {
		roadLength = length;
	}
	
	public void setnLots (int lots) {
		nLots = lots;
	}
	
	public String getRoadName () {
		return roadName;
	}
	
	public String getJunction1 () {
		return junction1;
	}
	
	public String getJunction2 () {
		return junction2;
	}
	
	public int getRoadLength () {
		return roadLength;
	}
	
	public int getnLots () {
		return nLots;
	}
	
	public double getLotWidth () {
		return lotWidth;
	}
	
	public double getLotToJunctionDistance (int lotNumber, String junction) {
		if (junction.equals(junction1)) {
			return ((0.5 + (lotNumber-1)/2) * lotWidth);
		} else {
			return ((nLots/2 - (0.5 + (lotNumber-1)/2)) * lotWidth);
		}
		
	}
	
	public String getOtherJunction(String junction) {
		if (junction.equals(junction1)) {
			return junction2;
		} else {
			return junction1;
		}
	}
}
