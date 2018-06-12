package example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import example.fuzzy.FuzzySet;
import example.fuzzy.FuzzyTrapezoid;
import example.model.Person;

public class LocalTest {
	KieSession ksession;
	private static KieContainer kieContainer = null;

	
	@Before
	public void before() {
		ksession = getKieContainer().getKieBase("drlRules").newKieSession();
	}
	
	@After
	public void after() {
		ksession.dispose();
	}

	private static synchronized KieContainer getKieContainer() {
		if (kieContainer == null) {
			KieServices kieServices = KieServices.Factory.get();
			kieContainer = kieServices.getKieClasspathContainer();
		}

		return kieContainer;
	}

	@Test
	public void test() {
		FuzzySet ageSet = new FuzzySet();
		ageSet.add(new FuzzyTrapezoid("young", 0, 0, 20, 31));
		ageSet.add(new FuzzyTrapezoid("mature", 14, 24, 60, 66));
		ageSet.add(new FuzzyTrapezoid("old", 53, 64, 150, 150));
		
		ksession.setGlobal("ageSet", ageSet);

		// -------------
		Person person1 = new Person("adam", 22);
		ksession.insert(person1);

		Person person2 = new Person("brian", 23);
		ksession.insert(person2);

		Person person3 = new Person("john", 63);
		ksession.insert(person3);

		// -------------

		ksession.fireAllRules();

		// Collection<FactHandle> factHandles = ksession.getFactHandles();
	}
}
