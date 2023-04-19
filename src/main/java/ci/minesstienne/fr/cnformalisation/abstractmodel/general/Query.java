package ci.minesstienne.fr.cnformalisation.abstractmodel.general;

import java.util.Set;

/**
 * @author YoucTagh
 */
public class Query {
    public URI resourceURI;
    public Set<Constraint> clientConstraints;

    public Query(URI resourceURI, Set<Constraint> clientConstraints) {
        this.resourceURI = resourceURI;
        this.clientConstraints = clientConstraints;
    }
}
