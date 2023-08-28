package ci.minesstienne.fr.cnformalisation.usecase;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.*;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFConstraint;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFRepresentation;
import ci.minesstienne.fr.cnformalisation.abstractmodel.swreport.SCNMeasure;
import ci.minesstienne.fr.cnformalisation.abstractmodel.swreport.SemanticMeasurement;
import ci.minesstienne.fr.cnformalisation.abstractmodel.swreport.SemanticResponse;
import org.apache.jena.graph.Graph;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;

import java.util.Set;

import static ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFConstraint.mustBeOfRDFRepresentationType;

/**
 * @author YoucTagh
 */
public class ThirdUseCase {

    public static void main(String[] args) {

        Resource abiesNumidica = new Resource(new URI("http://localhost:80/cn-formalisation/3/abies_numidica"));

        SCNMeasure scnMeasure = (representation, clientConstraints, serverConstraint) -> {
            float clientQuality = 0;
            for (Constraint constraint : clientConstraints) {
                clientQuality = Math.max(constraint.getRepresentationQuality(representation), clientQuality);

            }

            float serverQuality = 0;
            for (Constraint constraint : serverConstraint) {
                serverQuality = Math.max(constraint.getRepresentationQuality(representation), serverQuality);
            }
            return new SemanticMeasurement(clientQuality * serverQuality, null);
        };

        Web web = new Web();

        RDFRepresentation abiesNumidicaFoaf = new RDFRepresentation(new URI("http://localhost:80/cn-formalisation/3/abies_numidica/an-foaf.ttl"), new URI("http://localhost:80/cn-formalisation/3/abies_numidica/profile-foaf.ttl"));
        RDFRepresentation abiesNumidicaFoafOrg = new RDFRepresentation(new URI("http://localhost:80/cn-formalisation/3/abies_numidica/an-foaf-org.ttl"), new URI("http://localhost:80/cn-formalisation/3/abies_numidica/profile-foaf-org.ttl"));
        RDFRepresentation abiesNumidicaSchema = new RDFRepresentation(new URI("http://localhost:80/cn-formalisation/3/abies_numidica/an-schema.ttl"), new URI("http://localhost:80/cn-formalisation/3/abies_numidica/profile-schema.ttl"));

        RDFConstraint serverConstraintAbiesNumidicaFoaf = representation -> representation.equals(abiesNumidicaFoaf) ? mustBeOfRDFRepresentationType(representation, 0.7F) : 0F;
        RDFConstraint serverConstraintAbiesNumidicaFoafOrg = representation -> representation.equals(abiesNumidicaFoafOrg) ? mustBeOfRDFRepresentationType(representation, 0.8F) : 0F;
        RDFConstraint serverConstraintAbiesNumidicaSchema = representation -> representation.equals(abiesNumidicaSchema) ? mustBeOfRDFRepresentationType(representation, 0.6F) : 0F;

        web.addWebServer(abiesNumidica, new WebServerResource(
                Set.of(abiesNumidicaFoaf, abiesNumidicaFoafOrg, abiesNumidicaSchema),
                scnMeasure,
                Set.of(serverConstraintAbiesNumidicaFoaf, serverConstraintAbiesNumidicaFoafOrg, serverConstraintAbiesNumidicaSchema)));

        Set<Constraint> clientConstraints = getClientConstraints(10);

        Query query = new Query(abiesNumidica.uri, clientConstraints);

        WebServerResource resourceWebServer = web.findResourceWebServer(query.resourceURI);

        SemanticResponse response = web.negotiateSemanticResponse(resourceWebServer.representations,
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
                        representation -> ((RDFRepresentation) representation).profile.toString().equals("http://localhost:80/cn-formalisation/3/abies_numidica/profile-foaf.ttl") ? 1F : 0F
                );
            case 5:
                return Set.of(
                        representation -> ((RDFRepresentation) representation).profile.toString().equals("http://localhost:80/cn-formalisation/3/abies_numidica/profile-schema.ttl") ? 1F : 0F
                );
            case 6:
                return Set.of(
                        representation -> ((RDFRepresentation) representation).profile.toString().equals("http://localhost:80/cn-formalisation/3/abies_numidica/profile-schema.ttl") ? 1F : 0F,
                        representation -> ((RDFRepresentation) representation).profile.toString().equals("http://localhost:80/cn-formalisation/3/abies_numidica/profile-foaf-org.ttl") ? 0.75F : 0F,
                        representation -> ((RDFRepresentation) representation).profile.toString().equals("http://localhost:80/cn-formalisation/3/abies_numidica/profile-foaf.ttl") ? 0.5F : 0F
                );
            case 7:
                return Set.of(
                        representation -> !(
                                ((RDFRepresentation) representation).profile.toString().equals("http://localhost:80/cn-formalisation/3/abies_numidica/profile-foaf-org.ttl")
                                        || ((RDFRepresentation) representation).profile.toString().equals("http://localhost:80/cn-formalisation/3/abies_numidica/profile-foaf.ttl")
                        ) ? 1F : 0F
                );
            case 8:
                return Set.of(
                        representation -> ((RDFRepresentation) representation).profile.toString().equals("http://localhost:80/cn-formalisation/3/abies_numidica/profile-schema-custom.ttl") ? 1F : 0F
                );
            case 9:
                return Set.of(
                        representation -> isRepresentationValid(representation, new URI("http://localhost:80/cn-formalisation/3/abies_numidica/profile-schema-custom.ttl")) ? 1F : 0F
                );
            default:
                return Set.of(representation -> 1F);
        }

    }

    public static boolean isRepresentationValid(Representation representation, URI profile) {
        try {

            Graph dataGraph = RDFDataMgr.loadGraph(representation.uri.toString());

            Graph shapesGraph = RDFDataMgr.loadGraph(profile.toString());
            Shapes shapes = Shapes.parse(shapesGraph);

            if (dataGraph.isEmpty()) {
                return false;
            }

            ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph);

            return report.conforms();

        } catch (Exception ex) {
            return false;
        }
    }

}
