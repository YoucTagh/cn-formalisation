package ci.minesstienne.fr.cnformalisation.abstractmodel.relaxed;

/**
 * @author YoucTagh
 */
public class ValidationResult {
    public float qvalue;
    public ConstraintType constraintType;
    public String message;

    public ValidationResult(float qvalue, ConstraintType constraintType, String message) {
        this.qvalue = qvalue;
        this.constraintType = constraintType;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "qvalue=" + qvalue +
                ", constraintType=" + constraintType +
                ", message='" + message + '\'' +
                '}';
    }
}
