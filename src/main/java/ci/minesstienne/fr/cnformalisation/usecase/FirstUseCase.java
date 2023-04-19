package ci.minesstienne.fr.cnformalisation.usecase;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.*;

import java.util.Set;

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

        Constraint serverConstraintBobJpeg = representation -> representation.equals(bobJpeg) ? 0.7F : 0F;
        Constraint serverConstraintTextFr = representation -> representation.equals(bobTextFr) ? 0.5F : 0F;
        Constraint serverConstraintTextEn = representation -> representation.equals(bobTextEn) ? 0.5F : 0F;
        Constraint serverConstraintRdfTurtle = representation -> representation.equals(bobRdfTurtle) ? 0.6F : 0F;
        Constraint serverConstraintRdfXml = representation -> representation.equals(bobRdfXml) ? 0.5F : 0F;

        CNMeasure serverCNMeasure = (representation, clientConstraints, serverConstraint) -> {
            float clientQuality = 0;
            for (Constraint constraint : clientConstraints) {
                clientQuality = Math.max(constraint.getRepresentationQuality(representation), clientQuality);
            }

            float serverQuality = 0;
            for (Constraint constraint : serverConstraint) {
                serverQuality = Math.max(constraint.getRepresentationQuality(representation), serverQuality);
            }
            return Math.min(clientQuality, serverQuality);
        };

        Web web = new Web();
        web.addWebServer(bob, new WebServerResource(
                Set.of(bobJpeg, bobTextEn, bobTextFr, bobRdfTurtle, bobRdfXml),
                serverCNMeasure,
                Set.of(serverConstraintBobJpeg, serverConstraintTextEn, serverConstraintTextFr, serverConstraintRdfTurtle, serverConstraintRdfXml)));

        Set<Constraint> clientConstraints = getClientConstraints(0);


        Query query = new Query(bob.uri, clientConstraints);

        WebServerResource resourceWebServer = web.findResourceWebServer(query.resourceURI);

        Representation representation = web.negotiateRepresentation(resourceWebServer.representations,
                resourceWebServer.cnMeasure,
                query.clientConstraints,
                resourceWebServer.serverConstraints);

        System.out.println(representation);

    }

    private static Set<Constraint> getClientConstraints(int useCase) {
        switch (useCase) {
            case 1:
                return Set.of(representation -> representation.uri.toString().endsWith(".ttl") ? 1F : 0F);
            case 2:
                return Set.of(representation -> representation.uri.toString().endsWith(".rdf") ? 1F : 0F);
            case 3:
                return Set.of(
                        representation -> representation.uri.toString().endsWith(".jsonld") ? 1F : 0F,
                        representation -> representation.uri.toString().endsWith(".nq") ? 1F : 0F,
                        representation -> representation.uri.toString().endsWith(".trig") ? 1F : 0F,
                        representation -> representation.uri.toString().endsWith(".rdf") ? 1F : 0F,
                        representation -> representation.uri.toString().endsWith(".nt") ? 1F : 0F,
                        representation -> representation.uri.toString().endsWith(".ttl") ? 1F : 0F
                );
            case 4:
                return Set.of(
                        representation -> representation.uri.toString().endsWith(".jpeg") ? 1F : 0F,
                        representation -> representation.uri.toString().endsWith(".png") ? 1F : 0F,
                        representation -> representation.uri.toString().endsWith(".jpg") ? 1F : 0F,
                        representation -> representation.uri.toString().endsWith(".svg") ? 1F : 0F,
                        representation -> representation.uri.toString().endsWith(".tiff") ? 1F : 0F,
                        representation -> representation.uri.toString().endsWith(".txt") ? 0.8F : 0F
                );
            case 5:
                return Set.of(
                        representation -> representation.uri.toString().contains(".fr") ? 1F : 0F
                );
            case 6:
                return Set.of(
                        representation -> representation.uri.toString().contains(".es") ? 1F : 0F,
                        representation -> representation.uri.toString().contains(".en") ? 0.8F : 0F
                );
            case 7:
                return Set.of(
                        representation -> representation.uri.toString().contains(".txt") ? 0.2F : 0F,
//                        representation -> representation.uri.toString().contains(".ttl") ? 0.2F : 0F,
                        representation -> representation.uri.toString().contains(".css") ? 0.2F : 0F,
                        representation -> representation.uri.toString().contains(".csv") ? 0.2F : 0F,
                        representation -> representation.uri.toString().contains(".n3") ? 0.2F : 0F,
                        representation -> representation.uri.toString().contains(".xml") ? 0.2F : 0F,
                        representation -> representation.uri.toString().contains(".ttl") ? 1F : 0F
                );
            case 8:
                return Set.of(
                        representation -> representation.uri.toString().contains(".txt") ? 1F : 0F,
//                        representation -> representation.uri.toString().contains(".ttl") ? 1F : 0F,
                        representation -> representation.uri.toString().contains(".css") ? 1F : 0F,
                        representation -> representation.uri.toString().contains(".csv") ? 1F : 0F,
                        representation -> representation.uri.toString().contains(".n3") ? 1F : 0F,
                        representation -> representation.uri.toString().contains(".xml") ? 1F : 0F,
                        representation -> representation.uri.toString().contains(".ttl") ? 0.2F : 0F
                );

            default:
                return Set.of(representation -> 1F);
        }

    }


}
