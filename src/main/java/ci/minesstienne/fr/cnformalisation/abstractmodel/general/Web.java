package ci.minesstienne.fr.cnformalisation.abstractmodel.general;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author YoucTagh
 */
public class Web implements IWeb{

    public void addServerData(Resource resource, ServerData serverData) {
        WEB.put(resource, serverData);
    }

    public ServerData findServerData(Identifier resourceIdentifier) {
        return WEB.get(new Resource(resourceIdentifier));
    }

    public Response negotiateResponse(Set<Representation> representations, CNMeasure cnMeasure, Set<Constraint> clientConstraints, Set<Constraint> serverConstraints) {
        Representation bestRepresentation = null;
        float bestQuality = 0F;

        for (Representation representation : representations) {
            float representationQuality = cnMeasure.getRepresentationQuality(representation, clientConstraints, serverConstraints);
            if (representationQuality > bestQuality) {
                bestQuality = representationQuality;
                bestRepresentation = representation;
            }
        }
        return new Response(bestRepresentation, Stream.concat(clientConstraints.stream(), serverConstraints.stream()).collect(Collectors.toSet()));
    }
}
