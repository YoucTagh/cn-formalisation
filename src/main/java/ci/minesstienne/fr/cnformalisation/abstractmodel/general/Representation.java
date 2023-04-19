package ci.minesstienne.fr.cnformalisation.abstractmodel.general;

/**
 * @author YoucTagh
 */
public class Representation {
    public URI uri;

    public Representation(URI uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return uri.toString();
    }
}
