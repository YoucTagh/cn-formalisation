package ci.minesstienne.fr.cnformalisation.usecase;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.*;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.Profile;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFConstraint;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFRepresentation;

import java.util.Set;

import static ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFConstraint.mustBeOfRDFRepresentationType;

/**
 * @author YoucTagh
 */
public class SecondScenario {

    public static void main(String[] args) {

        Resource youctagh = new Resource(new Identifier("http://youctagh.com/me/"));

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

        RDFRepresentation youctaghFoaf = new RDFRepresentation(new Identifier("http://youctagh.com/me/yt-foaf.ttl"), new Profile(new Identifier("http://youctagh.com/profiles/prof-foaf.ttl")));
        RDFRepresentation youctaghFoafOrg = new RDFRepresentation(new Identifier("http://youctagh.com/me/yt-foaf-org.ttl"), new Profile(new Identifier("http://youctagh.com/profiles/prof-foaf-org.ttl")));
        RDFRepresentation youctaghSchema = new RDFRepresentation(new Identifier("http://youctagh.com/me/yt-schema.ttl"), new Profile(new Identifier("http://youctagh.com/profiles/prof-schema.ttl")));

        RDFConstraint serverConstraintYoucTaghFoaf = representation -> representation.equals(youctaghFoaf) ? mustBeOfRDFRepresentationType(representation, 0.7F) : 0F;
        RDFConstraint serverConstraintYoucTaghFoafOrg = representation -> representation.equals(youctaghFoafOrg) ? mustBeOfRDFRepresentationType(representation, 0.9F) : 0F;
        RDFConstraint serverConstraintYoucTaghSchema = representation -> representation.equals(youctaghSchema) ? mustBeOfRDFRepresentationType(representation, 0.8F) : 0F;

        web.addServerData(youctagh, new ServerData(
                Set.of(youctaghFoaf, youctaghFoafOrg, youctaghSchema),
                cnMeasure,
                Set.of(serverConstraintYoucTaghFoaf, serverConstraintYoucTaghFoafOrg, serverConstraintYoucTaghSchema)));

        Set<Constraint> clientConstraints = getClientConstraints(7);

        Request request = new Request(youctagh.identifier, clientConstraints);

        ServerData serverData = web.findServerData(request.resourceIdentifier);

        Response response = web.negotiateResponse(serverData.representations,
                serverData.cnMeasure,
                request.clientConstraints,
                serverData.serverConstraints);

        System.out.println(response);

    }

    @SuppressWarnings("SameParameterValue")
    public static Set<Constraint> getClientConstraints(int useCase) {
        switch (useCase) {
            case 1:
                return Set.of(representation -> representation.identifier.toString().endsWith(".ttl") ? 1F : 0F);
            case 2:
                return Set.of(representation -> representation.identifier.toString().endsWith(".rdf") ? 1F : 0F);
            case 3:
                return Set.of(
                        representation -> representation.identifier.toString().endsWith(".jsonld") ? 1F : 0F,
                        representation -> representation.identifier.toString().endsWith(".nq") ? 1F : 0F,
                        representation -> representation.identifier.toString().endsWith(".trig") ? 1F : 0F,
                        representation -> representation.identifier.toString().endsWith(".rdf") ? 1F : 0F,
                        representation -> representation.identifier.toString().endsWith(".nt") ? 1F : 0F,
                        representation -> representation.identifier.toString().endsWith(".ttl") ? 1F : 0F
                );
            case 4:
                return Set.of(
                        representation -> ((RDFRepresentation) representation).profile.identifier.toString().equals("http://youctagh.com/profiles/prof-foaf.ttl") ? 1F : 0F
                );
            case 5:
                return Set.of(
                        representation -> ((RDFRepresentation) representation).profile.identifier.toString().equals("http://youctagh.com/profiles/prof-schema.ttl") ? 1F : 0F
                );
            case 6:
                return Set.of(
                        representation -> ((RDFRepresentation) representation).profile.identifier.toString().equals("http://youctagh.com/profiles/prof-schema.ttl") ? 0.9F : 0F,
                        representation -> ((RDFRepresentation) representation).profile.identifier.toString().equals("http://youctagh.com/profiles/prof-foaf-org.ttl") ? 0.75F : 0F,
                        representation -> ((RDFRepresentation) representation).profile.identifier.toString().equals("http://youctagh.com/profiles/prof-foaf.ttl") ? 0.5F : 0F
                );
            case 7:
                return Set.of(
                        representation -> !(
                                ((RDFRepresentation) representation).profile.identifier.toString().equals("http://youctagh.com/profiles/prof-foaf-org.ttl")
                                        || ((RDFRepresentation) representation).profile.identifier.toString().equals("http://youctagh.com/profiles/prof-foaf.ttl")
                        ) ? 1F : 0F
                );
            case 8:
                return Set.of(
                        representation -> ((RDFRepresentation) representation).profile.identifier.toString().equals("http://youctagh.com/profiles/prof-schema-custom.ttl") ? 1F : 0F
                );
            default:
                return Set.of(representation -> 1F);
        }

    }

}
