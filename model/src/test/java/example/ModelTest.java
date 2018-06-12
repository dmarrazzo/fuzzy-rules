package example;

import org.junit.Assert;
import org.junit.Test;

import example.fuzzy.FuzzySet;
import example.fuzzy.FuzzyTrapezoid;

public class ModelTest {

	@Test
	public void testLimit() {
		FuzzySet ageSet = new FuzzySet();
		ageSet.add(new FuzzyTrapezoid("young", 0, 0, 20, 31));
		ageSet.add(new FuzzyTrapezoid("mature", 14, 24, 60, 66));
		ageSet.add(new FuzzyTrapezoid("old", 53, 64, 150, 150));
		Assert.assertTrue(ageSet.is(22, "young"));
		Assert.assertTrue(ageSet.is(23, "mature"));
		Assert.assertTrue(ageSet.is(61, "mature"));
		Assert.assertTrue(ageSet.is(62, "old"));
	}
}
