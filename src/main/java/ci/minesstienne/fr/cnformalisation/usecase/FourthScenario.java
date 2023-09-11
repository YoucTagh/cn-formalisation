package ci.minesstienne.fr.cnformalisation.usecase;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.*;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.Profile;
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
public class FourthScenario {

    public static void main(String[] args) {

        Resource abiesNumidica = new Resource(new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica"));

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

        RDFRepresentation abiesNumidicaFoaf = new RDFRepresentation(new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/an-foaf.ttl"), new Profile(new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/profile-foaf.ttl")));
        RDFRepresentation abiesNumidicaFoafOrg = new RDFRepresentation(new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/an-foaf-org.ttl"), new Profile(new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/profile-foaf-org.ttl")));
        RDFRepresentation abiesNumidicaSchema = new RDFRepresentation(new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/an-schema.ttl"), new Profile(new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/profile-schema.ttl")));

        RDFConstraint serverConstraintAbiesNumidicaFoaf = representation -> representation.equals(abiesNumidicaFoaf) ? mustBeOfRDFRepresentationType(representation, 0.7F) : 0F;
        RDFConstraint serverConstraintAbiesNumidicaFoafOrg = representation -> representation.equals(abiesNumidicaFoafOrg) ? mustBeOfRDFRepresentationType(representation, 0.8F) : 0F;
        RDFConstraint serverConstraintAbiesNumidicaSchema = representation -> representation.equals(abiesNumidicaSchema) ? mustBeOfRDFRepresentationType(representation, 0.6F) : 0F;

        web.addServerData(abiesNumidica, new ServerData(
                Set.of(abiesNumidicaFoaf, abiesNumidicaFoafOrg, abiesNumidicaSchema),
                scnMeasure,
                Set.of(serverConstraintAbiesNumidicaFoaf, serverConstraintAbiesNumidicaFoafOrg, serverConstraintAbiesNumidicaSchema)));

        Set<RDFConstraintWithReport> clientConstraints = getClientConstraints(3);

        Request request = new Request(abiesNumidica.identifier, clientConstraints.stream().map(constraint -> (Constraint) constraint).collect(Collectors.toSet()));

        ServerData serverData = web.findServerData(request.resourceIdentifier);

        SemanticResponse response = web.negotiateSemanticResponse(serverData.representations,
                serverData.cnMeasure,
                request.clientConstraints,
                serverData.serverConstraints);

        System.out.println(response);

    }

    @SuppressWarnings("SameParameterValue")
    public static Set<RDFConstraintWithReport> getClientConstraints(int useCase) {
        switch (useCase) {
            case 1:
                return Set.of(
                        representation -> {
                            ValidationReport report = isRepresentationValid(representation, new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/profile-schema-custom.ttl"));
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
                            ValidationReport report = isRepresentationValid(representation, new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/profile-with-severity.ttl"));
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
                            ValidationReport report = isRepresentationValid(representation, new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/profile-with-severity.ttl"));
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

    public static ValidationReport isRepresentationValid(Representation representation, Identifier profile) {
        try {

            Graph dataGraph = RDFDataMgr.loadGraph(representation.identifier.toString());

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


