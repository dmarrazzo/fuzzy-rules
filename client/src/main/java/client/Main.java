package client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import example.BOM;
import example.Capacity;
import example.Component;
import example.MemoryPosition;
import example.Parameter;
import example.PeripheralParameter;
import example.ProductConfigurationError;
import example.Range;
import utils.Result;


public class Main {
	final static Logger log =  LoggerFactory.getLogger(Main.class);

	private static final String URL = "http://localhost:8081/kie-server/services/rest/server";
	private static final String user = "donato";
	private static final String password = "donato";
	private static final String CONTAINER = "example:infineon-rules:1.0-SNAPSHOT";

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		ksFireAllRule();
		//ksStartRuleFlow();
		long end = System.currentTimeMillis();
		System.out.println("time elapsed: "+ (end-start));
	}

	@SuppressWarnings("rawtypes")
	public static void ksFireAllRule() {
		try {

			KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(URL, user, password);
			// Marshalling
			Set<Class<?>> extraClasses = new HashSet<Class<?>>();
			extraClasses.add(BOM.class);
			extraClasses.add(Capacity.class);
			extraClasses.add(Component.class);
			extraClasses.add(MemoryPosition.class);
			extraClasses.add(Parameter.class);
			extraClasses.add(PeripheralParameter.class);
			extraClasses.add(ProductConfigurationError.class);
			extraClasses.add(Range.class);
			extraClasses.add(Result.class);
			config.addExtraClasses(extraClasses);
			config.setMarshallingFormat(MarshallingFormat.JSON);
			Map<String, String> headers = null;
			config.setHeaders(headers);

			KieServicesClient client = KieServicesFactory.newKieServicesClient(config);
			RuleServicesClient ruleClient = client.getServicesClient(RuleServicesClient.class);


			KieCommands cmdFactory = KieServices.Factory.get().getCommands();
			List<Command> commands = new ArrayList<Command>();

			// -------------
			BOM bomA = new BOM("BOM A");
			List<Component> componentsA = new ArrayList<Component>();
			Component component0 = new Component("X", 6,24,0,0);
			Component component1 = new Component("Y", 4,20,0,0);
			componentsA.add(component0);
			componentsA.add(component1);
			bomA.setComponents(componentsA);

			// INSERT WM
			commands.add(cmdFactory.newInsert(bomA, "bomA"));

			// SET FOCUS
			commands.add(cmdFactory.newAgendaGroupSetFocus("generation"));

			// GET ALL THE VM
		  commands.add(cmdFactory.newGetObjects("objs"));

			// FIRE RULES
			commands.add(cmdFactory.newFireAllRules());

			BatchExecutionCommand command = cmdFactory.newBatchExecution(commands, "ksessionDrlRules");

			ServiceResponse<ExecutionResults> response = ruleClient.executeCommandsWithResults(CONTAINER, command);
			ExecutionResults results = response.getResult();

			if (results==null)
				throw new Exception(response.toString());

			Collection<String> identifiers = results.getIdentifiers();
			for (String identifier : identifiers) {
				Object object = results.getValue(identifier);

				log.info("WM object: {} {}", identifier, object);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
