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

/**
 * @author YoucTagh
 */
public class FourthScenario {

    public static void main(String[] args) {

        Resource youctagh = new Resource(new Identifier("http://www.youctagh.com/me/"));

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
                        bestReportSet.addAll(clientSM.reportSet);
                        bestReportSet.addAll(serverSM.reportSet);
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

        semanticWeb.addSemanticServerData(youctagh, new SemanticServerData(
                Set.of(abiesNumidicaFoaf, abiesNumidicaFoafOrg, abiesNumidicaSchema),
                semanticCNMeasure,
                Set.of(((Profile) abiesNumidicaFoaf.profile), ((Profile) abiesNumidicaFoafOrg.profile), ((Profile) abiesNumidicaSchema.profile))
        ));

        Set<Profile> clientProfiles = getClientProfiles(3);

        SemanticRequest semanticRequest = new SemanticRequest(youctagh.identifier, clientProfiles);

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

    private static SemanticMeasureResponse calculateValidationReportScore(ValidationReport report) {
        float qualityValue = 1;
        Set<ValidationResult> validationResults = new HashSet<>();
        for (ReportEntry reportEntry : report.getEntries()) {
            if (reportEntry.severity().equals(Severity.Violation)) {
                qualityValue = 0;
                validationResults.add(new ValidationResult(1, ConstraintType.hard, reportEntry.message()));
            } else if (reportEntry.severity().equals(Severity.Warning)) {
                qualityValue -= 0.1;
                validationResults.add(new ValidationResult(0.1F, ConstraintType.soft, reportEntry.message()));
            } else if (reportEntry.severity().equals(Severity.Info)) {
                qualityValue -= 0.01;
                validationResults.add(new ValidationResult(0.01F, ConstraintType.soft, reportEntry.message()));
            }
        }
        return new SemanticMeasureResponse(Math.max(0, qualityValue), validationResults);
    }

    public static ValidationReport isRepresentationValid(Representation representation, Profile profile) {
        try {
            Graph dataGraph = RDFDataMgr.loadGraph(representation.identifier.toString());

            Graph shapesGraph = RDFDataMgr.loadGraph(profile.identifier.toString());
            Shapes shapes = Shapes.parse(shapesGraph);

            return ShaclValidator.get().validate(shapes, dataGraph);

        } catch (Exception ex) {
            ValidationReport report = new ValidationReport.Builder().build();
            report.getEntries().add(ReportEntry.create().severity(Severity.Violation).message(ex.getMessage()));
            return report;
        }
    }

    public static void logMeasurement(Profile serverProfile, Profile clientProfile, float quality, RDFRepresentation rdfRepresentation) {
        System.out.println("\tServerProfile => " + serverProfile);
        System.out.println("\tClientProfile => " + clientProfile);
        System.out.println("\tRepresentation => " + rdfRepresentation.identifier + " => " + quality);
        System.out.println("--------------");
    }
}


