package service;

import java.util.Comparator;

import model.Distance;
import model.Record;

public class DistanceComparator implements Comparator<Distance>{

	@Override
	public int compare(Distance o1, Distance o2) {
		
		if (o1.getDistancesquare()==(o2.getDistancesquare()))
			return 0;
		else if (o1.getDistancesquare()<(o2.getDistancesquare()))
			return -1;
		else //if (o1.getError()>(o2.getError()))
			return 1;
	}

}
