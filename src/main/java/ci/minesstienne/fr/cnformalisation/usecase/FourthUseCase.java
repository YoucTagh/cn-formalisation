package ci.minesstienne.fr.cnformalisation.usecase;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.*;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFConstraint;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFRepresentation;
import ci.minesstienne.fr.cnformalisation.abstractmodel.swreport.RDFConstraintWithReport;
import ci.minesstienne.fr.cnformalisation.abstractmodel.swreport.SCNMeasure;
import ci.minesstienne.fr.cnformalisation.abstractmodel.swreport.SemanticMeasurement;
import ci.minesstienne.fr.cnformalisation.abstractmodel.swreport.SemanticResponse;
import org.apache.jena.graph.Graph;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.validation.ReportEntry;
import org.apache.jena.shacl.validation.Severity;

import java.io.StringWriter;
import java.util.Set;
import java.util.stream.Collectors;

import static ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFConstraint.mustBeOfRDFRepresentationType;

/**
 * @author YoucTagh
 */
public class FourthUseCase {

    public static void main(String[] args) {

        Resource bob = new Resource(new URI("http://localhost:80/cn-formalisation/3/bob"));

        SCNMeasure scnMeasure = (representation, clientConstraints, serverConstraint) -> {

            SemanticMeasurement clientSM = new SemanticMeasurement(0F, null);

            for (Constraint constraint : clientConstraints) {
                SemanticMeasurement semanticMeasurement = ((RDFConstraintWithReport) constraint).getRepresentationSemanticMeasurement(representation);
                if (clientSM.qualityValue < semanticMeasurement.qualityValue)
                    clientSM = semanticMeasurement;
            }

            float serverQuality = 0;
            for (Constraint constraint : serverConstraint) {
                serverQuality = Math.max(constraint.getRepresentationQuality(representation), serverQuality);
            }
            return new SemanticMeasurement(clientSM.qualityValue * serverQuality, clientSM.report);
        };

        Web web = new Web();

        RDFRepresentation bobFoaf = new RDFRepresentation(new URI("http://localhost:80/cn-formalisation/3/bob/bob-foaf.ttl"), new URI("http://localhost:80/cn-formalisation/3/bob/profile-foaf.ttl"));
        RDFRepresentation bobFoafOrg = new RDFRepresentation(new URI("http://localhost:80/cn-formalisation/3/bob/bob-foaf-org.ttl"), new URI("http://localhost:80/cn-formalisation/3/bob/profile-foaf-org.ttl"));
        RDFRepresentation bobSchema = new RDFRepresentation(new URI("http://localhost:80/cn-formalisation/3/bob/bob-schema.ttl"), new URI("http://localhost:80/cn-formalisation/3/bob/profile-schema.ttl"));

        RDFConstraint serverConstraintBobFoaf = representation -> representation.equals(bobFoaf) ? mustBeOfRDFRepresentationType(representation, 0.7F) : 0F;
        RDFConstraint serverConstraintBobFoafOrg = representation -> representation.equals(bobFoafOrg) ? mustBeOfRDFRepresentationType(representation, 0.8F) : 0F;
        RDFConstraint serverConstraintBobSchema = representation -> representation.equals(bobSchema) ? mustBeOfRDFRepresentationType(representation, 0.6F) : 0F;

        web.addWebServer(bob, new WebServerResource(
                Set.of(bobFoaf, bobFoafOrg, bobSchema),
                scnMeasure,
                Set.of(serverConstraintBobFoaf, serverConstraintBobFoafOrg, serverConstraintBobSchema)));

        Set<RDFConstraintWithReport> clientConstraints = getClientConstraints(3);

        Query query = new Query(bob.uri, clientConstraints.stream().map(constraint -> (Constraint) constraint).collect(Collectors.toSet()));

        WebServerResource resourceWebServer = web.findResourceWebServer(query.resourceURI);

        SemanticResponse response = web.negotiateSemanticResponse(resourceWebServer.representations,
                resourceWebServer.cnMeasure,
                query.clientConstraints,
                resourceWebServer.serverConstraints);

        System.out.println(response);

    }

    @SuppressWarnings("SameParameterValue")
    public static Set<RDFConstraintWithReport> getClientConstraints(int useCase) {
        switch (useCase) {
            case 1:
                return Set.of(
                        representation -> {
                            ValidationReport report = isRepresentationValid(representation, new URI("http://localhost:80/cn-formalisation/3/bob/profile-schema-custom.ttl"));
                            if (report != null && report.conforms()) {
                                return new SemanticMeasurement(1F, getStringGraph(report.getGraph()));
                            } else {
                                return new SemanticMeasurement(0F, null);
                            }
                        }
                );
            case 2:
                return Set.of(
                        representation -> {
                            ValidationReport report = isRepresentationValid(representation, new URI("http://localhost:80/cn-formalisation/3/bob/profile-with-severity.ttl"));
                            if (report != null && report.conforms()) {
                                return new SemanticMeasurement(1F, getStringGraph(report.getGraph()));
                            } else {
                                return new SemanticMeasurement(0F, null);
                            }
                        }
                );
            case 3:
                return Set.of(
                        representation -> {
                            ValidationReport report = isRepresentationValid(representation, new URI("http://localhost:80/cn-formalisation/3/bob/profile-with-severity.ttl"));
                            if (report == null) {
                                return new SemanticMeasurement(0F, null);
                            } else if (report.conforms()) {
                                return new SemanticMeasurement(1F, getStringGraph(report.getGraph()));
                            } else {
                                float qualityValue = calculateConformanceScore(report);
                                return new SemanticMeasurement(qualityValue, getStringGraph(report.getGraph()));
                            }
                        }
                );
            default:
                return Set.of(representation -> new SemanticMeasurement(1F, ""));
        }

    }

    private static float calculateConformanceScore(ValidationReport report) {
        float qualityValue = 1;
        for (ReportEntry reportEntry : report.getEntries()) {
            if (reportEntry.severity().equals(Severity.Violation)) {
                return 0;
            } else if (reportEntry.severity().equals(Severity.Warning)) {
                qualityValue -= 0.1;
            } else if (reportEntry.severity().equals(Severity.Info)) {
                qualityValue -= 0.01;
            }
        }
        return Math.max(0, qualityValue);
    }

    public static ValidationReport isRepresentationValid(Representation representation, URI profile) {
        try {

            Graph dataGraph = RDFDataMgr.loadGraph(representation.uri.toString());

            Graph shapesGraph = RDFDataMgr.loadGraph(profile.toString());
            Shapes shapes = Shapes.parse(shapesGraph);

            if (dataGraph.isEmpty()) {
                return getDefaultReport();
            }

            return ShaclValidator.get().validate(shapes, dataGraph);

        } catch (Exception ex) {
            return getDefaultReport();
        }
    }

    private static ValidationReport getDefaultReport() {
        return null;
    }

    private static String getStringGraph(Graph graph) {
        StringWriter sw = new StringWriter();
        RDFDataMgr.write(sw, graph, Lang.TURTLE);
        return sw.toString();
    }

}


