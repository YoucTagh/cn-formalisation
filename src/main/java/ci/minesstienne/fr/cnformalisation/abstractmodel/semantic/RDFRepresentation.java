package ci.minesstienne.fr.cnformalisation.abstractmodel.semantic;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Representation;
import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Identifier;

/**
 * @author YoucTagh
 */
public class RDFRepresentation extends Representation {

    public Profile profile;

    public RDFRepresentation(Identifier identifier, Profile profile) {
        super(identifier);
        this.profile = profile;
    }
}
