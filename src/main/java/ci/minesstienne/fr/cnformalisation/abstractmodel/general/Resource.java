package ci.minesstienne.fr.cnformalisation.abstractmodel.general;

import java.util.Objects;

/**
 * @author YoucTagh
 */
public class Resource {
    public URI uri;

    public Resource(URI uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(uri, resource.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri);
    }
}
