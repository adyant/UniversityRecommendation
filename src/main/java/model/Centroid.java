package model;

public class Centroid implements Comparable{

	private double studentid;
	private double weight;
	
	
	public Centroid(double studentid, double weight) {
		super();
		this.studentid = studentid;
		this.weight = weight;
	}

	public double getStudentid() {
		return studentid;
	}

	public void setStudentid(double studentid) {
		this.studentid = studentid;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public int compareTo(Object o) {
		if (this.weight<((Centroid)o).getWeight())
			return 1;
		else if (this.weight>((Centroid)o).getWeight())
			return -1;
		else
			return 0;
	}

}
