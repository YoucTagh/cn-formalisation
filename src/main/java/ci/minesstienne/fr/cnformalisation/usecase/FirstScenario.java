package ci.minesstienne.fr.cnformalisation.usecase;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.*;

import java.util.Set;

/**
 * @author YoucTagh
 */
public class FirstScenario {
    public static void main(String[] args) {

        Resource abiesNumidica = new Resource(new Identifier("http://youctagh.com/species/abies_numidica"));

        Representation abiesNumidicaHTML = new Representation(new Identifier("http://youctagh.com/species/abies_numidica.html"));
        Representation abiesNumidicaPDF = new Representation(new Identifier("http://youctagh.com/species/abies_numidica.pdf"));
        Representation abiesNumidicaTextFr = new Representation(new Identifier("http://youctagh.com/species/abies_numidica.fr.txt"));
        Representation abiesNumidicaTextEn = new Representation(new Identifier("http://youctagh.com/species/abies_numidica.en.txt"));
        Representation abiesNumidicaRdfXml = new Representation(new Identifier("http://youctagh.com/species/abies_numidica.rdf"));
        Representation abiesNumidicaRdfTurtle = new Representation(new Identifier("http://youctagh.com/species/abies_numidica.ttl"));

        Constraint serverConstraintAbiesNumidicaHTML = representation -> representation.equals(abiesNumidicaHTML) ? 1F : 0F;
        Constraint serverConstraintAbiesNumidicaPDF = representation -> representation.equals(abiesNumidicaPDF) ? 0.8F : 0F;
        Constraint serverConstraintAbiesNumidicaTextFr = representation -> representation.equals(abiesNumidicaTextFr) ? 0.6F : 0F;
        Constraint serverConstraintAbiesNumidicaTextEn = representation -> representation.equals(abiesNumidicaTextEn) ? 0.7F : 0F;
        Constraint serverConstraintAbiesNumidicaRdfXml = representation -> representation.equals(abiesNumidicaRdfXml) ? 0.75F : 0F;
        Constraint serverConstraintAbiesNumidicaRdfTurtle = representation -> representation.equals(abiesNumidicaRdfTurtle) ? 0.85F : 0F;

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
        web.addServerData(abiesNumidica, new ServerData(
                Set.of(abiesNumidicaHTML,abiesNumidicaPDF, abiesNumidicaTextEn, abiesNumidicaTextFr, abiesNumidicaRdfTurtle, abiesNumidicaRdfXml),
                serverCNMeasure,
                Set.of(serverConstraintAbiesNumidicaHTML,serverConstraintAbiesNumidicaPDF, serverConstraintAbiesNumidicaTextEn, serverConstraintAbiesNumidicaTextFr, serverConstraintAbiesNumidicaRdfTurtle, serverConstraintAbiesNumidicaRdfXml)));

        Set<Constraint> clientConstraints = getClientConstraints(7);

        Request request = new Request(abiesNumidica.identifier, clientConstraints);

        ServerData serverData = web.findServerData(request.resourceIdentifier);

        Response response = web.negotiateResponse(
                serverData.representations,
                serverData.cnMeasure,
                request.clientConstraints,
                serverData.serverConstraints
        );

        System.out.println(response);

    }

    @SuppressWarnings("SameParameterValue")
    private static Set<Constraint> getClientConstraints(int useCase) {
        switch (useCase) {
            case 1:
                return Set.of(representation -> representation.identifier.toString().endsWith(".rdf") ? 1F : 0F);
            case 2:
                return Set.of(
                        representation -> representation.identifier.toString().endsWith(".jsonld") ? 1F : 0F,
                        representation -> representation.identifier.toString().endsWith(".nq") ? 1F : 0F,
                        representation -> representation.identifier.toString().endsWith(".trig") ? 1F : 0F,
                        representation -> representation.identifier.toString().endsWith(".rdf") ? 1F : 0F,
                        representation -> representation.identifier.toString().endsWith(".nt") ? 1F : 0F,
                        representation -> representation.identifier.toString().endsWith(".ttl") ? 1F : 0F
                );
            case 3:
                return Set.of(
                        representation -> representation.identifier.toString().endsWith(".jpeg") ? 1F : 0F,
                        representation -> representation.identifier.toString().endsWith(".png") ? 1F : 0F,
                        representation -> representation.identifier.toString().endsWith(".jpg") ? 1F : 0F,
                        representation -> representation.identifier.toString().endsWith(".svg") ? 1F : 0F,
                        representation -> representation.identifier.toString().endsWith(".tiff") ? 1F : 0F,
                        representation -> representation.identifier.toString().endsWith(".txt") ? 0.8F : 0F
                );
            case 4:
                return Set.of(
                        representation -> representation.identifier.toString().contains(".fr") ? 1F : 0F
                );
            case 5:
                return Set.of(
                        representation -> representation.identifier.toString().contains(".ar") ? 1F : 0F,
                        representation -> representation.identifier.toString().contains(".en") ? 0.8F : 0F
                );
            case 6:
                return Set.of(
                        representation -> representation.identifier.toString().contains(".txt") ? 0.2F : 0F,
                        representation -> representation.identifier.toString().contains(".css") ? 0.2F : 0F,
                        representation -> representation.identifier.toString().contains(".csv") ? 0.2F : 0F,
                        representation -> representation.identifier.toString().contains(".n3") ? 0.2F : 0F,
                        representation -> representation.identifier.toString().contains(".xml") ? 0.2F : 0F,
                        representation -> representation.identifier.toString().contains(".ttl") ? 1F : 0F
                );
            case 8:
                return Set.of(
                        representation -> representation.identifier.toString().contains(".txt") ? 1F : 0F,
                        representation -> representation.identifier.toString().contains(".css") ? 1F : 0F,
                        representation -> representation.identifier.toString().contains(".csv") ? 1F : 0F,
                        representation -> representation.identifier.toString().contains(".n3") ? 1F : 0F,
                        representation -> representation.identifier.toString().contains(".xml") ? 1F : 0F,
                        representation -> representation.identifier.toString().contains(".ttl") ? 0.2F : 0F
                );
            default:
                return Set.of(representation -> 1F);
        }

    }


}
