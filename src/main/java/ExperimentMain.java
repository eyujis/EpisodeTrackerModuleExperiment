import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import environment.Environment;


public class ExperimentMain {
	
	public Logger logger = Logger.getLogger(ExperimentMain.class.getName());
        
        
	public ExperimentMain() throws IOException {

		Logger.getLogger("codelets").setLevel(Level.SEVERE);

		// Create Environment
		Environment env = new Environment();

		// Creates the Agent Mind and start it
		AgentMind a = new AgentMind(env);
            
        }


	public static void main(String[] args) throws IOException {
		ExperimentMain edm = new ExperimentMain();
	}

}
