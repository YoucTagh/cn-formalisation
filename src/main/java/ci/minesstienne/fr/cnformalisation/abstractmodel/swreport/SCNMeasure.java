package ci.minesstienne.fr.cnformalisation.abstractmodel.swreport;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.CNMeasure;
import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Constraint;
import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Representation;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFConstraint;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFRepresentation;
import ci.minesstienne.fr.cnformalisation.abstractmodel.swreport.SemanticMeasurement;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author YoucTagh
 */
public interface SCNMeasure extends CNMeasure {

    default float getRepresentationQuality(Representation representation, Set<Constraint> clientConstraints, Set<Constraint> serverConstraint) {
        return this.getRepresentationSemanticMeasurement
                (
                        representation,
                        clientConstraints.stream().map(constraint -> (RDFConstraint) constraint).collect(Collectors.toSet()),
                        serverConstraint.stream().map(constraint -> (RDFConstraint) constraint).collect(Collectors.toSet())
                ).qualityValue;
    }

    SemanticMeasurement getRepresentationSemanticMeasurement(Representation representation, Set<Constraint> clientConstraints, Set<Constraint> serverConstraint);
}
