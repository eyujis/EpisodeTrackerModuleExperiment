package codelets;

import br.unicamp.cst.core.entities.Codelet;
import environment.Environment;

import java.io.IOException;

public class DataSimulatorCodelet extends Codelet {
    private Environment e;
    private double firstTimestamp;
    private boolean firstCall = true;
    String[] rawData = null;
    private double actualTimestamp = 0;
    private double lastStepTimestamp = 0;
    private double timeStepDuration;

    public DataSimulatorCodelet(Environment env, double timeStepDuration) {
        this.e = env;
        this.timeStepDuration = timeStepDuration;
    }

    @Override
    public void accessMemoryObjects() {

    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        // Get next line from the environment.
        // Each line corresponds to a frame.
        try {
            this.rawData = e.step();
        } catch(IOException ex){
            System.out.println (ex.toString());
        }
        setsFirstTimestamp();
        this.actualTimestamp = Double.valueOf(this.rawData[0]);

        if (surpassedTimeStepDuration())    {
            this.lastStepTimestamp = this.actualTimestamp;
            rawData[0] = String.valueOf(this.actualTimestamp-this.firstTimestamp);

        }
    }

    private void setsFirstTimestamp() {
        if (firstCall == true) {
            firstTimestamp = Double.valueOf(this.rawData[0]);
            firstCall = false;
        }
    }

    private boolean surpassedTimeStepDuration() {
        double timeElapsed = this.actualTimestamp - this.lastStepTimestamp;

        if(timeElapsed>=this.timeStepDuration)   {
            return true;
        }
        return false;
    }

}
