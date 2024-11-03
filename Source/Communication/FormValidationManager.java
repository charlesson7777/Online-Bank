package Communication;

public abstract class FormValidationManager extends FormValidation {
    protected DBManager managerDB;

    public FormValidationManager(InstanceManager m) {
        super(m);
        managerDB = DBManager.getDBManager();
    }

}
