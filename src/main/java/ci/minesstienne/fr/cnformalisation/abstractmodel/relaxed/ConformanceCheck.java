package ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed;

import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFRepresentation;

/**
 * @author YoucTagh
 */
public interface ConformanceCheck {
    ValidationResult check(RDFRepresentation rdfRepresentation, Shape shape);
}
