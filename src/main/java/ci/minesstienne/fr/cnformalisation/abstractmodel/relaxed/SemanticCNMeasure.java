package ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed;

import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.Profile;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFRepresentation;

import java.util.Set;

/**
 * @author YoucTagh
 */
public interface SemanticCNMeasure {
    SemanticMeasureResponse getRepresentationQuality(RDFRepresentation rdfRepresentation, Set<Profile> clientProfile, Set<Profile> serverProfile);

}
