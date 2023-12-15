package ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed;

import ci.minesstienne.fr.cnformalisation.abstractmodel.semantic.RDFConstraint;

/**
 * @author YoucTagh
 */
public class Shape {
    public RDFConstraint rdfConstraint;
    public ConstraintType constraintType;

    public Shape(RDFConstraint rdfConstraint, ConstraintType constraintType) {
        this.rdfConstraint = rdfConstraint;
        this.constraintType = constraintType;
    }
}