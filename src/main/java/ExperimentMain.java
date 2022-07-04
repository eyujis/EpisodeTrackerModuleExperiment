import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.unicamp.cst.io.rest.RESTServer;
import environment.Environment;


public class ExperimentMain {
	
	public Logger logger = Logger.getLogger(ExperimentMain.class.getName());
        
        
	public ExperimentMain() throws IOException {

		Logger.getLogger("codelets").setLevel(Level.SEVERE);

		// Create Environment
		Environment env = new Environment();

		// Creates the Agent Mind and start it
		AgentMind mind = new AgentMind(env);

		// Creates RESTServer for visualization
		int port = 4000;
		String GET_URL = "http://localhost:"+port;
		RESTServer rs = new RESTServer(mind,port,true);

            
        }


	public static void main(String[] args) throws IOException {
		ExperimentMain edm = new ExperimentMain();
	}

}
