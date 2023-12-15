package ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Constraint;
import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Identifier;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.Profile;

import java.util.Set;

/**
 * @author YoucTagh
 */
public class SemanticRequest {
    public Identifier resourceIdentifier;
    public Set<Profile> profiles;

    public SemanticRequest(Identifier resourceIdentifier, Set<Profile> profiles) {
        this.resourceIdentifier = resourceIdentifier;
        this.profiles = profiles;
    }
}
