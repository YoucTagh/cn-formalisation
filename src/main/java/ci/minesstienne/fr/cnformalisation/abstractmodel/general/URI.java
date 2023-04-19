package ci.minesstienne.fr.cnformalisation.abstractmodel.general;

import java.util.Objects;

/**
 * @author YoucTagh
 */
public class URI {
    String uri;

    public URI(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URI uri1 = (URI) o;
        return Objects.equals(uri, uri1.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri);
    }

    @Override
    public String toString() {
        return uri;
    }
}
