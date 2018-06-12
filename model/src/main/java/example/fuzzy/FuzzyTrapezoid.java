package example.fuzzy;


/**
 *      B---C
 *     /     \
 * ---A       D---
 * 
 * @author donato
 *
 */
public class FuzzyTrapezoid extends FuzzyElement{

	private double a;
	private double b;
	private double c;
	private double d;

	public FuzzyTrapezoid(String name, double a,double b,double c,double d) {
		this.setName(name);
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	@Override
	public double gradeOfMembership(double value) {
		if (value <= a || value >= d) return 0;
		if (value >= b && value <= c) return 1;
		if (value > a && value < b) return (value-a)/(b-a);
		
		return (value-d)/(c-d);
	}
	
	
	
	//------------------------------------------------------
	// GETTERS / SETTERS

	
}
