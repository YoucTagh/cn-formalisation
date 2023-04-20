package ci.minesstienne.fr.cnformalisation.abstractmodel.semantic;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Constraint;
import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Representation;

/**
 * @author YoucTagh
 */
public interface RDFConstraint extends Constraint {

    static float mustBeOfRDFRepresentationType(Representation representation, float qualityValue) {
        if (!(representation instanceof RDFRepresentation))
            return 0F;
        return qualityValue;
    }
}
