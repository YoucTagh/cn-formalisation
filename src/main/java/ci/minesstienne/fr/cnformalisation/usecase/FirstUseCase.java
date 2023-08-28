package ci.minesstienne.fr.cnformalisation.usecase;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.*;

import java.util.Set;

/**
 * @author YoucTagh
 */
public class FirstUseCase {
    public static void main(String[] args) {
        Resource abiesNumidica = new Resource(new URI("http://youctagh.com/species/abies_numidica"));

        Representation abiesNumidicaJpeg = new Representation(new URI("http://youctagh.com/species/abies_numidica.jpeg"));
        Representation abiesNumidicaTextFr = new Representation(new URI("http://youctagh.com/species/abies_numidica.fr.txt"));
        Representation abiesNumidicaTextEn = new Representation(new URI("http://youctagh.com/species/abies_numidica.en.txt"));
        Representation abiesNumidicaRdfTurtle = new Representation(new URI("http://youctagh.com/species/abies_numidica.ttl"));
        Representation abiesNumidicaRdfXml = new Representation(new URI("http://youctagh.com/species/abies_numidica.rdf"));

        Constraint serverConstraintAbiesNumidicaJpeg = representation -> representation.equals(abiesNumidicaJpeg) ? 0.7F : 0F;
        Constraint serverConstraintAbiesNumidicaTextFr = representation -> representation.equals(abiesNumidicaTextFr) ? 0.6F : 0F;
        Constraint serverConstraintAbiesNumidicaTextEn = representation -> representation.equals(abiesNumidicaTextEn) ? 0.5F : 0F;
        Constraint serverConstraintAbiesNumidicaRdfTurtle = representation -> representation.equals(abiesNumidicaRdfTurtle) ? 0.6F : 0F;
        Constraint serverConstraintAbiesNumidicaRdfXml = representation -> representation.equals(abiesNumidicaRdfXml) ? 0.5F : 0F;

        CNMeasure serverCNMeasure = (representation, clientConstraints, serverConstraint) -> {
            float clientQuality = 0;
            for (Constraint constraint : clientConstraints) {
                clientQuality = Math.max(constraint.getRepresentationQuality(representation), clientQuality);
            }

            float serverQuality = 0;
            for (Constraint constraint : serverConstraint) {
                serverQuality = Math.max(constraint.getRepresentationQuality(representation), serverQuality);
            }
            return clientQuality * serverQuality;
        };

        Web web = new Web();
        web.addWebServer(abiesNumidica, new WebServerResource(
                Set.of(abiesNumidicaJpeg, abiesNumidicaTextEn, abiesNumidicaTextFr, abiesNumidicaRdfTurtle, abiesNumidicaRdfXml),
                serverCNMeasure,
                Set.of(serverConstraintAbiesNumidicaJpeg, serverConstraintAbiesNumidicaTextEn, serverConstraintAbiesNumidicaTextFr, serverConstraintAbiesNumidicaRdfTurtle, serverConstraintAbiesNumidicaRdfXml)));

        Set<Constraint> clientConstraints = getClientConstraints(7);

        Query query = new Query(abiesNumidica.uri, clientConstraints);

        WebServerResource resourceWebServer = web.findResourceWebServer(query.resourceURI);

        Response response = web.negotiateResponse(
                resourceWebServer.representations,
                resourceWebServer.cnMeasure,
                query.clientConstraints,
                resourceWebServer.serverConstraints
        );

        System.out.println(response);

    }

    @SuppressWarnings("SameParameterValue")
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
