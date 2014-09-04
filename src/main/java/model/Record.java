package model;

public class Record{

	private double studentid;
	private double weight;
	private double clusterid;
	private double error;
	
	
	public Record(double studentid, double weight, double clusterid,
			double error) {
		super();
		this.studentid = studentid;
		this.weight = weight;
		this.clusterid = clusterid;
		this.error = error;
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
	
	public double getClusterid() {
		return clusterid;
	}
	
	public void setClusterid(double clusterid) {
		this.clusterid = clusterid;
	}
	
	public double getError() {
		return error;
	}
	
	public void setError(double error) {
		this.error = error;
	}

	

}
