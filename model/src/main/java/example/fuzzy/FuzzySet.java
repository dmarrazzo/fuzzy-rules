package example.fuzzy;

import java.util.HashMap;

public class FuzzySet {
	private HashMap<String,FuzzyElement> set = new HashMap<>();

	public void add(FuzzyElement element) {
		set.put(element.getName(), element);
	}
	
	public boolean is(double value, String elementName) {
		return set.get(elementName).gradeOfMembership(value)>0.8;
	}
}
