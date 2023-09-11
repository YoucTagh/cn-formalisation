package ci.minesstienne.fr.cnformalisation.abstractmodel.general;

import java.util.Set;

/**
 * @author YoucTagh
 */

public class ServerData {
    public Set<Representation> representations;
    public CNMeasure cnMeasure;
    public Set<Constraint> serverConstraints;

    public ServerData(Set<Representation> representations, CNMeasure cnMeasure, Set<Constraint> serverConstraints) {
        this.representations = representations;
        this.cnMeasure = cnMeasure;
        this.serverConstraints = serverConstraints;
    }
}
