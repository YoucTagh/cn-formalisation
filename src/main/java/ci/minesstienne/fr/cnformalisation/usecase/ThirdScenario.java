package ci.minesstienne.fr.cnformalisation.usecase;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.*;
import ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed.*;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.Profile;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFRepresentation;
import org.apache.jena.graph.Graph;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.validation.ReportEntry;
import org.apache.jena.shacl.validation.Severity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static ci.minesstienne.fr.cnformalisation.usecase.FourthScenario.logMeasurement;

/**
 * @author YoucTagh
 */
public class ThirdScenario {

    public static void main(String[] args) {

        Resource abiesNumidica = new Resource(new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica"));

        SemanticCNMeasure semanticCNMeasure = (representation, clientProfiles, serverProfiles) -> {

            float bestQuality = 0;
            Set<ValidationResult> bestReportSet = new HashSet<>();

            for (Profile clientProfile : clientProfiles) {
                for (Profile serverProfile : serverProfiles) {
                    ValidationReport clientReport = isRepresentationValid(representation, clientProfile);
                    SemanticMeasureResponse clientSM = calculateValidationReportScore(clientReport);

                    ValidationReport serverReport = isRepresentationValid(representation, serverProfile);
                    SemanticMeasureResponse serverSM = calculateValidationReportScore(serverReport);

                    float representationQuality = clientSM.quality * serverSM.quality;
                    if (representationQuality > bestQuality) {
                        bestQuality = representationQuality;
                        bestReportSet = clientSM.reportSet;
                    }
                    logMeasurement(serverProfile, clientProfile, representationQuality, representation);
                }
            }
            return new SemanticMeasureResponse(bestQuality, bestReportSet);
        };

        SemanticWeb semanticWeb = new SemanticWeb();

        RDFRepresentation abiesNumidicaFoaf = new RDFRepresentation(new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/an-foaf.ttl"), new Profile(new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/profile-foaf.ttl"), null));
        RDFRepresentation abiesNumidicaFoafOrg = new RDFRepresentation(new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/an-foaf-org.ttl"), new Profile(new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/profile-foaf-org.ttl"), null));
        RDFRepresentation abiesNumidicaSchema = new RDFRepresentation(new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/an-schema.ttl"), new Profile(new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/profile-schema.ttl"), null));

        semanticWeb.addSemanticServerData(abiesNumidica, new SemanticServerData(
                Set.of(abiesNumidicaFoaf, abiesNumidicaFoafOrg, abiesNumidicaSchema),
                semanticCNMeasure,
                Set.of(abiesNumidicaFoaf.profile, abiesNumidicaFoafOrg.profile, abiesNumidicaSchema.profile)
        ));

        Set<Profile> clientProfiles = getClientProfiles(3);

        SemanticRequest semanticRequest = new SemanticRequest(abiesNumidica.identifier, clientProfiles);

        SemanticServerData semanticServerData = semanticWeb.findSemanticServerData(semanticRequest.resourceIdentifier);

        SemanticResponse response = semanticWeb.negotiateSemanticResponse(semanticServerData.rdfRepresentations,
                semanticServerData.semanticCNMeasure,
                semanticRequest.profiles,
                semanticServerData.serverProfiles);

        System.out.println(response);
    }

    @SuppressWarnings("SameParameterValue")
    public static Set<Profile> getClientProfiles(int useCase) {
        switch (useCase) {
            default:
                return Set.of(new Profile(new Identifier("http://localhost:80/cn-formalisation/3/abies_numidica/profile-with-severity.ttl"), null));

        }

    }

    public static ValidationReport isRepresentationValid(Representation representation, Profile profile) {
        try {

            Graph dataGraph = RDFDataMgr.loadGraph(representation.identifier.toString());

            Graph shapesGraph = RDFDataMgr.loadGraph(profile.identifier.toString());
            Shapes shapes = Shapes.parse(shapesGraph);

            ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph);

            return report;

        } catch (Exception ex) {
            ValidationReport report = new ValidationReport.Builder().build();
            report.getEntries().add(ReportEntry.create().severity(Severity.Violation).message(ex.getMessage()));
            return report;
        }
    }

    private static SemanticMeasureResponse calculateValidationReportScore(ValidationReport report) {
        return new SemanticMeasureResponse(report.conforms() ? 1 : 0,
                report
                        .getEntries()
                        .stream()
                        .map(reportEntry -> new ValidationResult(1, ConstraintType.hard, reportEntry.message()))
                        .collect(Collectors.toSet()));
    }


}
