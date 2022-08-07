package codelets;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;

import java.util.ArrayList;
import java.util.List;

public class EpisodeBufferizerCodelet extends Codelet {

    private Memory episodeMO;
    private Memory episodeBufferMO;
    private int buffer_size = 5;
    private Idea episodeBuffer = initializeBuffer();

    @Override
    public void accessMemoryObjects() {
        this.episodeMO=(Memory)this.getInput("EPISODE");
        this.episodeBufferMO=(MemoryObject)this.getOutput("EPISODE_BUFFER");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        if (this.episodeMO.getI()=="" || this.episodeMO.getI()==null)    {
            return;
        }

        Idea episodeFrame = (Idea) this.episodeMO.getI();
        List<Idea> episodeFrames = (List<Idea>) ((Idea) this.episodeBuffer).getValue();
        addFrame(episodeFrames, episodeFrame);
        this.episodeBufferMO.setI((Idea) episodeBuffer);

        System.out.println(((Idea) episodeBufferMO.getI()).getValue());

    }

    public Idea initializeBuffer() {
        Idea episodeBuffer = new Idea("episodeBuffer","",0);
        List<Idea> episodes = new ArrayList<Idea>();
        episodeBuffer.setValue(episodes);
        return episodeBuffer;
    }


    private void addFrame(List<Idea> episodes, Idea episode) {
        if (episodes.size()<this.buffer_size) {
            episodes.add(episode);
        } else {
            episodes.remove(0);
            episodes.add(episode);
        }

    }
}
