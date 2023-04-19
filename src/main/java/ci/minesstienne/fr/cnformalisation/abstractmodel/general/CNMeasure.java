package ci.minesstienne.fr.cnformalisation.abstractmodel.general;

import java.util.Set;

/**
 * @author YoucTagh
 */
public interface CNMeasure {
    float getRepresentationQuality(Representation representation, Set<Constraint> clientConstraints,Set<Constraint> serverConstraint);
}
