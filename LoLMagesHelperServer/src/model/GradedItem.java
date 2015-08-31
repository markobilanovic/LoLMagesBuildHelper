package model;

public class GradedItem {

	private Long id;
	private String name;
	private double grade;
	private int boughtCount;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getGrade() {
		return grade;
	}
	public void setGrade(double grade) {
		this.grade = grade;
	}
	public int getBoughtCount() {
		return boughtCount;
	}
	public void setBoughtCount(int boughtCount) {
		this.boughtCount = boughtCount;
	}
	
	public void updateGrade(double newGrade)
	{
		if(boughtCount > 0)
			newGrade = (grade + newGrade) / 2;
		
		grade = newGrade;
		boughtCount++;
	}
	
}
