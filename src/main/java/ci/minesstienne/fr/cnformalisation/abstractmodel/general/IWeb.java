package ci.minesstienne.fr.cnformalisation.abstractmodel.general;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author YoucTagh
 */
public interface IWeb {

    HashMap<Resource, ServerData> WEB = new HashMap<>();

    void addServerData(Resource resource, ServerData serverData);

    ServerData findServerData(Identifier resourceIdentifier);

    Response negotiateResponse(Set<Representation> representations, CNMeasure cnMeasure, Set<Constraint> clientConstraints, Set<Constraint> serverConstraints);
}
