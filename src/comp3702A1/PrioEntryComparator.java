package comp3702A1;

import java.util.Comparator;

public class PrioEntryComparator implements Comparator<PriorityEntry> {

	@Override
	public int compare(PriorityEntry arg0, PriorityEntry arg1) {
		// TODO Auto-generated method stub
		if (arg0.getDistance() < arg1.getDistance())
        {
            return -1;
        }
        if (arg0.getDistance() > arg1.getDistance())
        {
            return 1;
        }
		return 0;
	}

}
