package ci.minesstienne.fr.cnformalisation.abstractmodel.swreport;

import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Constraint;
import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Representation;
import ci.minesstienne.fr.cnformalisation.abstractmodel.general.Response;

import java.util.Set;

/**
 * @author YoucTagh
 */
public class SemanticResponse extends Response {
    public String report;

    public SemanticResponse(Representation representation, Set<Constraint> constraints, String report) {
        super(representation, constraints);
        this.report = report;
    }

    @Override
    public String toString() {
        return "SemanticResponse:" +
                "\n" + "representation = " + representation +
                "\n" + "report = " +
                "\n" + report;
    }
}
