package ci.minesstienne.fr.cnformalisation.abstractmodel.semantic;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Identifier;

/**
 * @author YoucTagh
 */
public class Profile {
    public Identifier identifier;

    public Profile(Identifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return identifier.toString();
    }
}
