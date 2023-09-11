package ci.minesstienne.fr.cnformalisation.abstractmodel.general;

import java.util.Set;

/**
 * @author YoucTagh
 */
public class Request {
    public Identifier resourceIdentifier;
    public Set<Constraint> clientConstraints;

    public Request(Identifier resourceIdentifier, Set<Constraint> clientConstraints) {
        this.resourceIdentifier = resourceIdentifier;
        this.clientConstraints = clientConstraints;
    }
}
