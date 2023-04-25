package ci.minesstienne.fr.cnformalisation.abstractmodel.swreport;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Representation;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFConstraint;

/**
 * @author YoucTagh
 */
public interface RDFConstraintWithReport extends RDFConstraint {
    SemanticMeasurement getRepresentationSemanticMeasurement(Representation representation);

    default float getRepresentationQuality(Representation representation) {
        return this.getRepresentationSemanticMeasurement(representation).qualityValue;
    }
}
