package entities;
import br.unicamp.cst.representation.idea.Idea;



public interface PropertyCategory {
    public abstract boolean belongsToCategory(Object concept);
    public abstract String getName();
}
