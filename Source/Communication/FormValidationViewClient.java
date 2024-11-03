package Communication;

import java.util.UUID;

import UI.PanelViewClient;

public class FormValidationViewClient extends FormValidation {
    
    public FormValidationViewClient(InstanceManager manager) {
        super(manager);
        db = DB.getDB();
    }

    @Override
    public boolean process(String[] response) {
        UUID uid = UUID.fromString(response[0]);
        String type = response[1];
        String clientName = response[2];
        
        PanelViewClient view = new PanelViewClient(type, clientName, uid);
        view.loadActivity();

        return true;
    }
    
}
