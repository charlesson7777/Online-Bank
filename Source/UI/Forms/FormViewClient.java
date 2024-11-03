package UI.Forms;

import java.util.List;

import javax.swing.Box;
import javax.swing.JComboBox;
import BankObjects.UserClient;
import BankObjects.UserPrivilege;
import Communication.FetcherManagerClient;
import Communication.FormValidationViewClient;
import Communication.InstanceManager;
import UI.Theme.MinimalDropDown;

public class FormViewClient extends Form {
    private JComboBox<String> accountsCombo, viewTypeCombo;
    private FetcherManagerClient fetcher;
    private List<UserClient> clientsList;

    public FormViewClient(InstanceManager manager, FetcherManagerClient fetch) {
        super("View Client", manager);
        fetcher = fetch;
        validation = new FormValidationViewClient(manager);
        initialize();

    }

    private void initialize() {
        setTitle("View a Client");

        addFormPrompt("Select a Client:");
        accountsCombo = new MinimalDropDown<>(fetchClients());
        formBody.add(accountsCombo);
        int margins = (int)(screenSize.width / (10.0 * 8) * 0.5);
        formBody.add(Box.createVerticalStrut(margins));

        addFormPrompt("Select view type:");
        String[] viewTypes = new String[] {"ACCOUNTS", "TRANSACTIONS"};
        viewTypeCombo = new MinimalDropDown<>(viewTypes);
        formBody.add(viewTypeCombo);

        submitButton.addActionListener(e -> process());
    }

    private String[] fetchClients() {
        clientsList = fetcher.getAllClients();
        String[] toReturn = new String[clientsList.size()];

        for (int i = 0; i < clientsList.size(); i++) {
            UserClient client = clientsList.get(i);
            String name = client.getFirstName().substring(0, 1).toUpperCase() + 
                client.getFirstName().substring(1).toLowerCase() + " " + 
                client.getLastName().substring(0, 1).toUpperCase() + 
                client.getLastName().substring(1).toLowerCase();

            String privilege;
            if (client.getPrivilege() == UserPrivilege.NORMAL) {
                privilege = "Non-VIP";
            } else {
                privilege = client.getPrivilege().toString();
            }

            toReturn[i] = name + " (" + privilege + ")";
        }

        return toReturn;
    }

    private void process() {
        UserClient client = clientsList.get(accountsCombo.getSelectedIndex());
        String clientID = client.getID().toString();
        String viewTypes = viewTypeCombo.getSelectedItem().toString();
        String clientName = client.getFirstName().substring(0, 1).toUpperCase() + 
            client.getFirstName().substring(1).toLowerCase() + " " + 
            client.getLastName().substring(0, 1).toUpperCase() + 
            client.getLastName().substring(1).toLowerCase();
        

        validation.process(new String[] {clientID, viewTypes, clientName});
    }

    
}
