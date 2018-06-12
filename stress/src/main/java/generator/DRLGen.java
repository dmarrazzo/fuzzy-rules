package generator;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class DRLGen {

	public static void main(String[] args) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/rules-5000.drl"), "utf-8"));
			writer.write("package example;\n\n");
			
			String drl;
			for (int i = 1; i<=15000; i++) {
				drl = "rule \"4_2_1_Conditional_logic_"+i+"\"\n" + 
						"when\n" + 
							"\tBOM( $comps : components)\n" +
							"\t$x : Component( parameterA == "+ i + " , parameterB > "+ i*5 + " ) from $comps\n" + 
						"then\n" +
							"\t$x.setParameterC( "+ i*10 + " );\n" + 
						"end\n";
				writer.write(drl);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
