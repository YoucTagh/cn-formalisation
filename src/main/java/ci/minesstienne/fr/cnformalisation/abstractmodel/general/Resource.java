package ci.minesstienne.fr.cnformalisation.abstractmodel.general;

import java.util.Objects;

/**
 * @author YoucTagh
 */
public class Resource {
    public Identifier identifier;

    public Resource(Identifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(identifier, resource.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}
