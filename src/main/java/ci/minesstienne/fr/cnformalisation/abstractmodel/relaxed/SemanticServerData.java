package ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.CNMeasure;
import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Constraint;
import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Representation;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.Profile;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFRepresentation;

import java.util.Set;

/**
 * @author YoucTagh
 */

public class SemanticServerData {
    public Set<RDFRepresentation> rdfRepresentations;
    public SemanticCNMeasure semanticCNMeasure;
    public Set<Profile> serverProfiles;

    public SemanticServerData(Set<RDFRepresentation> rdfRepresentations, SemanticCNMeasure semanticCNMeasure, Set<Profile> serverProfiles) {
        this.rdfRepresentations = rdfRepresentations;
        this.semanticCNMeasure = semanticCNMeasure;
        this.serverProfiles = serverProfiles;
    }
}
