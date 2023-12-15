package ci.minesstienne.fr.cnformalisation.abstractmodel.semantic;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Identifier;
import ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed.Shape;

import java.util.Set;

/**
 * @author YoucTagh
 */
public class Profile {
    public Identifier identifier;
    public Set<Shape> shapes;

    public Profile(Identifier identifier) {
        this.identifier = identifier;
    }

    public Profile(Identifier identifier, Set<Shape> shapes) {
        this.identifier = identifier;
        this.shapes = shapes;
    }

    @Override
    public String toString() {
        return identifier.toString();
    }
}
