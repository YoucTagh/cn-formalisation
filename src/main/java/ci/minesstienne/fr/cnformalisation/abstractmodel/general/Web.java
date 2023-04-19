package ci.minesstienne.fr.cnformalisation.abstractmodel.general;

import com.sun.source.doctree.SeeTree;
import org.apache.jena.shacl.sys.C;

import java.util.HashMap;
import java.util.Set;

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

    public Representation negotiateRepresentation(Set<Representation> representations, CNMeasure cnMeasure, Set<Constraint> clientConstraints,Set<Constraint> serverConstraints){
        Representation bestRepresentation = null;
        float bestQuality = 0F;

        for (Representation representation: representations){
            float representationQuality = cnMeasure.getRepresentationQuality(representation, clientConstraints,serverConstraints);
            if(representationQuality>bestQuality){
                bestQuality = representationQuality;
                bestRepresentation = representation;
            }
        }
        
        return bestRepresentation;
    }
}
