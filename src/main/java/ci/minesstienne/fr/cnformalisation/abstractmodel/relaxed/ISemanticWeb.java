package ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Identifier;
import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Resource;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.Profile;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFRepresentation;

import java.util.HashMap;
import java.util.Set;

/**
 * @author YoucTagh
 */
public interface ISemanticWeb {

    HashMap<Resource, SemanticServerData> SEMANTIC_WEB = new HashMap<>();

    void addSemanticServerData(Resource resource, SemanticServerData semanticServerData) ;
    SemanticServerData findSemanticServerData(Identifier resourceIdentifier) ;
    SemanticResponse negotiateSemanticResponse(Set<RDFRepresentation> rdfRepresentations, SemanticCNMeasure semanticCNMeasure, Set<Profile> clientProfiles, Set<Profile> serverProfiles) ;
}