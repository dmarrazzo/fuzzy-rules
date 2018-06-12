package example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.core.event.DefaultAgendaEventListener;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

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
	public void test_4_1() {

		// -------------
		BOM bomA = new BOM("BOM A");
		List<Component> componentsA = new ArrayList<Component>();
		Component component0 = new Component("X", 1,2,3,4);
		componentsA.add(component0);
		bomA.setComponents(componentsA);
		ksession.insert(bomA);

		// -------------
		BOM bomB = new BOM("BOM B");
		List<Component> componentsB = new ArrayList<Component>();
		Component component1 = new Component("Y", 1,2,3,4);
		componentsB.add(component0);
		componentsB.add(component1);
		bomB.setComponents(componentsB);
		
		ksession.insert(bomB);

		ksession.getAgenda().getAgendaGroup("validation").setFocus();
		ksession.fireAllRules();

		boolean ok = false;

		Collection<FactHandle> factHandles = ksession.getFactHandles();
		for (FactHandle factHandle : factHandles) {
			Object object = ksession.getObject(factHandle);
			System.out.println(object);
			if (object instanceof ProductConfigurationError) {
				ok = true;
			}
		}
		Assert.assertTrue("X and Y cannot be in the same BOM", ok);
	}

	@Test
	public void test_4_2_1() {
		// -------------
		BOM bomA = new BOM("BOM A");
		List<Component> componentsA = new ArrayList<Component>();
		Component component0 = new Component("X", 5,12,1,0);
		Component component1 = new Component("Y", 1,2,0,0);
		componentsA.add(component0);
		componentsA.add(component1);
		bomA.setComponents(componentsA);
		ksession.insert(bomA);
		
		ksession.getAgenda().getAgendaGroup("conditional").setFocus();

		ksession.fireAllRules();

		Assert.assertEquals(2,component0.getParameterC());

	}

	@Test
	public void test_4_2_2_generation() {
		// -------------
		BOM bomA = new BOM("BOM A");
		List<Component> componentsA = new ArrayList<Component>();
		Component component0 = new Component("X", 6,24,0,0);
		Component component1 = new Component("Y", 4,20,0,0);
		componentsA.add(component0);
		componentsA.add(component1);
		bomA.setComponents(componentsA);
		ksession.insert(bomA);
		ksession.getAgenda().getAgendaGroup("generation").setFocus();
		ksession.fireAllRules();

		Assert.assertEquals(4,component0.getParameterC());

	}
	
	@Test
	public void test_4_2_3() {
		// -------------
		BOM bomA = new BOM("BOM A");
		List<Component> componentsA = new ArrayList<Component>();
		Component component0 = new Component("X", 6,24,0,0);
		Component component1 = new Component("Y", 4,20,0,0);
		componentsA.add(component0);
		componentsA.add(component1);
		bomA.setComponents(componentsA);
		ksession.insert(bomA);

		ksession.fireAllRules();
		boolean ok = false;
		
		Collection<FactHandle> factHandles = ksession.getFactHandles();
		for (FactHandle factHandle : factHandles) {
			Object object = ksession.getObject(factHandle);
			System.out.println(object);
			if (object instanceof Range) {
				ok = true;
			}
		}
		Assert.assertTrue("Range present", ok);

	}

	@Test
	public void test_4_2_4() {
		// -------------
		BOM bomA = new BOM("BOM A");
		List<Component> componentsA = new ArrayList<Component>();
		Component component0 = new Component("X");
		
		component0.addParameter(new MemoryPosition("A", 5));
		component0.addParameter(new PeripheralParameter("B", 25));
		component0.addParameter(new Capacity("C", 0));
		
		Component component1 = new Component("Y");
		component1.addParameter(new MemoryPosition("M", 6));
		component1.addParameter(new PeripheralParameter("N", 4));
		component1.addParameter(new Capacity("P", 0));
		
		componentsA.add(component0);
		componentsA.add(component1);
		bomA.setComponents(componentsA);
		ksession.insert(bomA);
		ksession.getAgenda().getAgendaGroup("reuse").setFocus();
		ksession.fireAllRules();

		Assert.assertEquals(30, component0.getParameter("C").getValue());
		Assert.assertEquals(10, component1.getParameter("P").getValue());
	}
	
	@Test
	public void test_3_3_4_Component_tree() {
		Component component0 = new Component("X", 5,12,1,0);
		Component component1 = new Component("Y", 7,120,0,0);
		ksession.insert(component0);
		ksession.insert(component1);
		
		ksession.fireAllRules();

		Assert.assertEquals(2,component0.getParameterC());
		Assert.assertEquals(9,component1.getParameterC());
		
	}

	@Test
	public void test_3_3_1_rule_flow() {

		// -------------
		BOM bomA = new BOM("flow");
		List<Component> componentsA = new ArrayList<Component>();
		Component component0 = new Component("X", 6,24,0,0);
		componentsA.add(component0);
		bomA.setComponents(componentsA);
		ksession.insert(bomA);

		//ksession.fireAllRules();
		ksession.startProcess("infineon-rules.33_3_1_rule_flow");
		
		Assert.assertEquals(4,component0.getParameterC());
		
	}
	
	@Test
	public void test_3_3_1_rule_flow_invalid() {
		ksession.addEventListener(new DefaultAgendaEventListener() {
			@Override
			public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
				System.out.println("**************Before " + event.getRuleFlowGroup().getName());
			}
			
			@Override
			public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
				System.out.println("**************After " + event.getRuleFlowGroup().getName());
			}
			
			@Override
			public void beforeMatchFired(BeforeMatchFiredEvent event) {
				System.out.println("---------Before " + event.getMatch().getRule().getName());
			}
			
			@Override
			public void afterMatchFired(AfterMatchFiredEvent event) {
				System.out.println("---------After " + event.getMatch().getRule().getName());
			}
		});
		// -------------
		BOM bomA = new BOM("flow");
		List<Component> componentsA = new ArrayList<Component>();
		Component component0 = new Component("X", 6,24,0,0);
		componentsA.add(component0);
		Component component1 = new Component("Y", 6,24,0,0);
		componentsA.add(component1);
		bomA.setComponents(componentsA);
		ksession.insert(bomA);

		//ksession.fireAllRules();
		ksession.startProcess("infineon-rules.33_3_1_rule_flow");

		System.out.println(component0);
		System.out.println(component0.getParameters());
		Assert.assertEquals(0,component0.getParameterC());
		
		
	}
	
}
