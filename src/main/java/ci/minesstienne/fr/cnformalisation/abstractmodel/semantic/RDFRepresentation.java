package ci.minesstienne.fr.cnformalisation.abstractmodel.semantic;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Representation;
import ci.minesstienne.fr.cnformalisation.abstractmodel.general.URI;

/**
 * @author YoucTagh
 */
public class RDFRepresentation extends Representation {

    public URI profile;

    public RDFRepresentation(URI uri,URI profile) {
        super(uri);
        this.profile = profile;
    }
}
