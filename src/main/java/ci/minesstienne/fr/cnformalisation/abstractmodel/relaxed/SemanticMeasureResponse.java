package ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed;

import java.util.Set;

/**
 * @author YoucTagh
 */
public class SemanticMeasureResponse {
    public float quality;
    public Set<ValidationResult> reportSet;

    public SemanticMeasureResponse(float quality, Set<ValidationResult> reportSet) {
        this.quality = quality;
        this.reportSet = reportSet;
    }
}
