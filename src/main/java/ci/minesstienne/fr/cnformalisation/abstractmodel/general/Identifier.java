package ci.minesstienne.fr.cnformalisation.abstractmodel.general;

import java.util.Objects;

/**
 * @author YoucTagh
 */
public class Identifier {
    String id;

    public Identifier(String uri) {
        this.id = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier identifier1 = (Identifier) o;
        return Objects.equals(id, identifier1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
