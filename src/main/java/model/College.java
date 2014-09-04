package model;

import java.io.Serializable;
import java.util.List;

public class College implements Serializable{

	private int id;
	private List<Preference> preferences;
	
	private String attributeone;
	private String attributetwo;
	private String attributethree;
	private String attributefour;
	private String attributefive;
	private String attributesix;
	private String maxPrefId ;
	//private Preference onepref;
	
	
	
	public List<Preference> getPreferences() {
		return preferences;
	}

	public void setPreferences(List<Preference> preferences) {
		this.preferences = preferences;
	}

	
	public String getMaxPrefId() {
		return maxPrefId;
	}

	public void setMaxPrefId(String maxPrefId) {
		this.maxPrefId = maxPrefId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	/*	public String getAttributeone() {
	return attributeone;
}

public void setAttributeone(String attributeone) {
	this.attributeone = attributeone;
}

public String getAttributetwo() {
	return attributetwo;
}

public void setAttributetwo(String attributetwo) {
	this.attributetwo = attributetwo;
}

public String getAttributethree() {
	return attributethree;
}

public void setAttributethree(String attributethree) {
	this.attributethree = attributethree;
}

public String getAttributefour() {
	return attributefour;
}

public void setAttributefour(String attributefour) {
	this.attributefour = attributefour;
}

public String getAttributefive() {
	return attributefive;
}

public void setAttributefive(String attributefive) {
	this.attributefive = attributefive;
}

public String getAttributesix() {
	return attributesix;
}

public void setAttributesix(String attributesix) {
	this.attributesix = attributesix;
}
*/
	/*public Preference getOnepref() {
	return onepref;
}

public void setOnepref(Preference onepref) {
	this.onepref = onepref;
}*/
	
}
