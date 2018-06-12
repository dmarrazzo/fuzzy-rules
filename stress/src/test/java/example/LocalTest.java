package example;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class LocalTest {
	
	private KieSession ksession;
	private static KieContainer kieContainer = null;

	
	@Before
	public void before() {
		long start = System.currentTimeMillis();
		ksession = getKieContainer().getKieBase("drlRules").newKieSession();
		long end = System.currentTimeMillis();
		System.out.println("ksession init "+ (end-start));
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
	public void test_4_2_1() {
		long start = System.currentTimeMillis();
	
		// -------------
		BOM bomA = new BOM("BOM A");
		List<Component> componentsA = new ArrayList<Component>();
		Component component0 = new Component("X", 5,120,0,0);
		Component component1 = new Component("Y", 1,2,0,0);
		componentsA.add(component0);
		componentsA.add(component1);
		bomA.setComponents(componentsA);
		ksession.insert(bomA);
		
		ksession.fireAllRules();
		long end = System.currentTimeMillis();
		System.out.println("rule execution time "+ (end-start));

		Assert.assertEquals(50, component0.getParameterC());

	}

	@Test
	public void test_4_2_1_2() {
		long start = System.currentTimeMillis();
	
		// -------------
		BOM bomA = new BOM("BOM A");
		List<Component> componentsA = new ArrayList<Component>();
		Component component0 = new Component("X", 6,110,0,0);
		Component component1 = new Component("Y", 9,20,0,0);
		Component component2 = new Component("Z", 12,110,0,0);
		Component component3 = new Component("A", 13,20,0,0);
		Component component4 = new Component("B", 17,110,0,0);
		Component component5 = new Component("C", 20,20,0,0);
		componentsA.add(component0);
		componentsA.add(component1);
		componentsA.add(component2);
		componentsA.add(component3);
		componentsA.add(component4);
		componentsA.add(component5);
		bomA.setComponents(componentsA);
		ksession.insert(bomA);
		
		ksession.fireAllRules();
		long end = System.currentTimeMillis();
		System.out.println("rule execution time "+ (end-start));

		Assert.assertEquals(50, component0.getParameterC());

	}

}
