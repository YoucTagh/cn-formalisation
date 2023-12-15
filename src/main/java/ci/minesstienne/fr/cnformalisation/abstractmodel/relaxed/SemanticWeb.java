package ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Identifier;
import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Resource;
import ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed.SemanticCNMeasure;
import ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed.SemanticMeasureResponse;
import ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed.SemanticResponse;
import ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed.SemanticServerData;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.Profile;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFRepresentation;

import java.util.HashMap;
import java.util.Set;

/**
 * @author YoucTagh
 */
public class SemanticWeb implements ISemanticWeb{

    private final HashMap<Resource, SemanticServerData> SEMANTIC_WEB = new HashMap<>();

    public void addSemanticServerData(Resource resource, SemanticServerData semanticServerData) {
        SEMANTIC_WEB.put(resource, semanticServerData);
    }

    public SemanticServerData findSemanticServerData(Identifier resourceIdentifier) {
        return SEMANTIC_WEB.get(new Resource(resourceIdentifier));
    }

    public SemanticResponse negotiateSemanticResponse(Set<RDFRepresentation> rdfRepresentations, SemanticCNMeasure semanticCNMeasure, Set<Profile> clientProfiles, Set<Profile> serverProfiles) {
        RDFRepresentation bestRepresentation = null;
        SemanticMeasureResponse bestSemanticMeasureResponse = new SemanticMeasureResponse(0, null);

        for (RDFRepresentation rdfRepresentation : rdfRepresentations) {

            SemanticMeasureResponse semanticMeasureResponse = semanticCNMeasure.getRepresentationQuality(
                    rdfRepresentation,
                    clientProfiles,
                    serverProfiles);

            if (semanticMeasureResponse.quality > bestSemanticMeasureResponse.quality) {
                bestSemanticMeasureResponse = semanticMeasureResponse;
                bestRepresentation = rdfRepresentation;
            }
        }

        return new SemanticResponse(bestRepresentation, bestSemanticMeasureResponse.reportSet);
    }
}
