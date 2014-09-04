package model;

import java.util.ArrayList;
import java.util.List;

public class Attribute {
	//private int id;
	//private int level;
	private String category; //activity, academic performance, constraints/etc
	private int maxno;
	private int weight;
	//private List<Attribute> attributelist = new ArrayList<Attribute>();

	
	public Attribute(){
		
	}
	
	public Attribute(String category, int weight) {
		this.category = category;
		this.maxno = weight;
		}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getMaxno() {
		return maxno;
	}
	public void setMaxno(int maxno) {
		this.maxno = maxno;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}


	
	
}
