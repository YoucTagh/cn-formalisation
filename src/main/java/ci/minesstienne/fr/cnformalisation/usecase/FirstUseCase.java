package ci.minesstienne.fr.cnformalisation.usecase;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Constraint;
import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Representation;
import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Resource;
import ci.minesstienne.fr.cnformalisation.abstractmodel.general.URI;

/**
 * @author YoucTagh
 */
public class FirstUseCase {
    public static void main(String[] args) {
        Resource bob = new Resource(new URI("http://www.bob-server.com/bob"));

        Representation bobJpeg = new Representation(new URI("http://www.bob-server.com/bob.jpeg"));
        Representation bobTextFr = new Representation(new URI("http://www.bob-server.com/bob.fr.txt"));
        Representation bobTextEn = new Representation(new URI("http://www.bob-server.com/bob.en.txt"));
        Representation bobRdfTurtle = new Representation(new URI("http://www.bob-server.com/bob.ttl"));
        Representation bobRdfXml = new Representation(new URI("http://www.bob-server.com/bob.rdf"));

        Constraint serverBobJpeg = representation -> representation.equals(bobJpeg) ? 0.7F : 0F;
        Constraint serverTextFr = representation -> representation.equals(bobTextFr) ? 0.5F : 0F;
        Constraint serverTextEn = representation -> representation.equals(bobTextEn) ? 0.5F : 0F;
        Constraint serverRdfTurtle = representation -> representation.equals(bobRdfTurtle) ? 0.6F : 0F;
        Constraint serverRdfXml = representation -> representation.equals(bobRdfTurtle) ? 0.5F : 0F;


    }
}
