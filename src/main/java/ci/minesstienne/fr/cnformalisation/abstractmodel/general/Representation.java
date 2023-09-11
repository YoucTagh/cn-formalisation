package ci.minesstienne.fr.cnformalisation.abstractmodel.general;

/**
 * @author YoucTagh
 */
public class Representation {
    public Identifier identifier;

    public Representation(Identifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return identifier.toString();
    }
}
