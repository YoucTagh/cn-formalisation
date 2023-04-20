package ci.minesstienne.fr.cnformalisation.abstractmodel.general;

import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFConstraint;
import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFRepresentation;
import ci.minesstienne.fr.cnformalisation.abstractmodel.swreport.SCNMeasure;
import ci.minesstienne.fr.cnformalisation.abstractmodel.swreport.SemanticMeasurement;
import ci.minesstienne.fr.cnformalisation.abstractmodel.swreport.SemanticResponse;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author YoucTagh
 */
public class Web {

    private final HashMap<Resource, WebServerResource> WEB = new HashMap<>();

    public void addWebServer(Resource resource, WebServerResource webServerResource) {
        WEB.put(resource, webServerResource);
    }

    public WebServerResource findResourceWebServer(URI resourceURI) {
        return WEB.get(new Resource(resourceURI));
    }

    public Response negotiateResponse(Set<Representation> representations, CNMeasure cnMeasure, Set<Constraint> clientConstraints, Set<Constraint> serverConstraints) {
        Representation bestRepresentation = null;
        float bestQuality = 0F;

        for (Representation representation : representations) {
            float representationQuality = cnMeasure.getRepresentationQuality(representation, clientConstraints, serverConstraints);
            if (representationQuality > bestQuality) {
                bestQuality = representationQuality;
                bestRepresentation = representation;
            }
        }
        return new Response(bestRepresentation, Stream.concat(clientConstraints.stream(), serverConstraints.stream()).collect(Collectors.toSet()));
    }

    public SemanticResponse negotiateSemanticResponse(Set<Representation> representations, CNMeasure cnMeasure, Set<Constraint> clientConstraints, Set<Constraint> serverConstraints) {
        RDFRepresentation bestRepresentation = null;
        SemanticMeasurement bestSemanticMeasurement = new SemanticMeasurement(0, "");

        for (Representation representation : representations) {
            SemanticMeasurement semanticMeasurement = ((SCNMeasure) cnMeasure).getRepresentationSemanticMeasurement(
                    representation,
                    clientConstraints,
                    serverConstraints
            );
            if (semanticMeasurement.qualityValue > semanticMeasurement.qualityValue) {
                bestSemanticMeasurement = semanticMeasurement;
                bestRepresentation = (RDFRepresentation) representation;
            }
        }

        return new SemanticResponse(bestRepresentation, Stream.concat(clientConstraints.stream(), serverConstraints.stream()).collect(Collectors.toSet()), bestSemanticMeasurement.report);
    }
}
