package ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed;

import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFRepresentation;

import java.util.Set;

/**
 * @author YoucTagh
 */
public class SemanticResponse {
    public RDFRepresentation rdfRepresentation;
    public Set<ValidationResult> validationResultSet;

    public SemanticResponse(RDFRepresentation rdfRepresentation, Set<ValidationResult> validationResultSet) {
        this.rdfRepresentation = rdfRepresentation;
        this.validationResultSet = validationResultSet;
    }

    @Override
    public String toString() {
        return "SemanticResponse_2{" +
                "rdfRepresentation=" + rdfRepresentation +
                ", validationResultSet=" + validationResultSet +
                '}';
    }
}
