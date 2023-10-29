import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.unicamp.cst.io.rest.RESTServer;
import environment.Environment;


public class ExperimentMain {

        
	public ExperimentMain() throws IOException {

		Logger.getLogger("codelets").setLevel(Level.SEVERE);

		// Create Environment
		Environment env = new Environment();

		// Creates the Agent Mind and start it
		AgentMind mind = new AgentMind(env);

		// Start Cognitive Cycle
		mind.start();
        }


	public static void main(String[] args) throws IOException {
		ExperimentMain ex = new ExperimentMain();
	}

}
