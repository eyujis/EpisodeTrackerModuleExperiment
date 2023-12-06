package codelets;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;
import memory_storage.MemoryInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EpisodeBufferizerCodelet extends Codelet {

    private Memory episodeMO;
    private Memory episodeBufferMO;
    MemoryInstance episodeMI;
    MemoryInstance episodeBufferMI;

    private int buffer_size = 5;
    private Idea episodeBuffer = new Idea("episodeBuffer","",0);

    public EpisodeBufferizerCodelet(MemoryInstance episodeMI, MemoryInstance episodeBufferMI) {
        this.episodeMI = episodeMI;
        this.episodeBufferMI = episodeBufferMI;
    }

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

        Idea episodeFrame = this.episodeMI.getIdea();
        addFrame(episodeFrame);
        this.episodeBufferMI.postIdea(episodeBuffer);
        System.out.println(this.episodeBufferMI.getIdea().getL()
                .stream()
                .map(event->event.getName())
                .collect(Collectors.toList()));

    }


    private void addFrame(Idea episode) {
        if (episodeBuffer.getL().size()<this.buffer_size) {
            episodeBuffer.getL().add(episode);
        } else {
            episodeBuffer.getL().remove(0);
            episodeBuffer.getL().add(episode);
        }
    }
}
