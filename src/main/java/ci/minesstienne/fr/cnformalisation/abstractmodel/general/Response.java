package ci.minesstienne.fr.cnformalisation.abstractmodel.general;

import java.util.Set;

/**
 * @author YoucTagh
 */
public class Response {
    public Representation representation;
    public Set<Constraint> constraints;

    public Response(Representation representation, Set<Constraint> constraints) {
        this.representation = representation;
        this.constraints = constraints;
    }

    @Override
    public String toString() {
        return "Response:" +
                "\n" + "representation = " + representation;
    }
}
