package example.fuzzy;

public abstract class FuzzyElement {
	private String name;
	
	public abstract double gradeOfMembership(double value);

	//------------------------------------------------------
	// GETTERS / SETTERS
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
