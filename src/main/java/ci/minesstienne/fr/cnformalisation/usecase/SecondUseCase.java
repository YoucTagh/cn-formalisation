package ci.minesstienne.fr.cnformalisation.usecase;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.*;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFConstraint;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFRepresentation;

import java.util.Set;

import static ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFConstraint.mustBeOfRDFRepresentationType;

/**
 * @author YoucTagh
 */
public class SecondUseCase {

    public static void main(String[] args) {

        Resource bob = new Resource(new URI("http://www.bob-server.com/bob"));

        CNMeasure cnMeasure = (representation, clientConstraints, serverConstraint) -> {
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

        RDFRepresentation bobFoaf = new RDFRepresentation(new URI("http://www.bob-server.com/bob-foaf.ttl"), new URI("http://www.profile-server.com/profile-foaf.ttl"));
        RDFRepresentation bobFoafOrg = new RDFRepresentation(new URI("http://www.bob-server.com/bob-foaf-org.ttl"), new URI("http://www.profile-server.com/profile-foaf-org.ttl"));
        RDFRepresentation bobSchema = new RDFRepresentation(new URI("http://www.bob-server.com/bob-schema.ttl"), new URI("http://www.profile-server.com/profile-schema.ttl"));

        RDFConstraint serverConstraintBobFoaf = representation -> representation.equals(bobFoaf) ? mustBeOfRDFRepresentationType(representation, 0.7F) : 0F;
        RDFConstraint serverConstraintBobFoafOrg = representation -> representation.equals(bobFoafOrg) ? mustBeOfRDFRepresentationType(representation, 0.8F) : 0F;
        RDFConstraint serverConstraintBobSchema = representation -> representation.equals(bobSchema) ? mustBeOfRDFRepresentationType(representation, 0.6F) : 0F;

        web.addWebServer(bob, new WebServerResource(
                Set.of(bobFoaf, bobFoafOrg, bobSchema),
                cnMeasure,
                Set.of(serverConstraintBobFoaf, serverConstraintBobFoafOrg, serverConstraintBobSchema)));

        Set<Constraint> clientConstraints = getClientConstraints(6);

        Query query = new Query(bob.uri, clientConstraints);

        WebServerResource resourceWebServer = web.findResourceWebServer(query.resourceURI);

        Response response = web.negotiateResponse(resourceWebServer.representations,
                resourceWebServer.cnMeasure,
                query.clientConstraints,
                resourceWebServer.serverConstraints);

        System.out.println(response);

    }

    @SuppressWarnings("SameParameterValue")
    public static Set<Constraint> getClientConstraints(int useCase) {
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
                        representation -> ((RDFRepresentation) representation).profile.toString().equals("http://www.profile-server.com/profile-foaf.ttl") ? 1F : 0F
                );
            case 5:
                return Set.of(
                        representation -> ((RDFRepresentation) representation).profile.toString().equals("http://www.profile-server.com/profile-schema.ttl") ? 1F : 0F
                );
            case 6:
                return Set.of(
                        representation -> ((RDFRepresentation) representation).profile.toString().equals("http://www.profile-server.com/profile-schema.ttl") ? 0.9F : 0F,
                        representation -> ((RDFRepresentation) representation).profile.toString().equals("http://www.profile-server.com/profile-foaf-org.ttl") ? 0.75F : 0F,
                        representation -> ((RDFRepresentation) representation).profile.toString().equals("http://www.profile-server.com/profile-foaf.ttl") ? 0.5F : 0F
                );
            case 7:
                return Set.of(
                        representation -> !(
                                ((RDFRepresentation) representation).profile.toString().equals("http://www.profile-server.com/profile-foaf-org.ttl")
                                        || ((RDFRepresentation) representation).profile.toString().equals("http://www.profile-server.com/profile-foaf.ttl")
                        ) ? 1F : 0F
                );
            case 8:
                return Set.of(
                        representation -> ((RDFRepresentation) representation).profile.toString().equals("http://www.profile-server.com/profile-schema-custom.ttl") ? 1F : 0F
                );
            default:
                return Set.of(representation -> 1F);
        }

    }

}
