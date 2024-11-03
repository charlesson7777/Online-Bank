package Communication;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import BankObjects.User;
import BankObjects.UserClient;
import BankObjects.UserClientVIP;
import BankObjects.UserPrivilege;

public class FetcherManagerClient extends FetcherManager {
    List<UserClient> normalClients;
    List<UserClientVIP> vipClients;

    public FetcherManagerClient() {
        super();
        normalClients = new ArrayList<>();
        vipClients = new ArrayList<>();
    }

    @Override
    public void fetch(UUID uid) {
        List<User> userslist = db.getAllUsers();

        for (User user : userslist) {
            if (user.getPrivilege() == UserPrivilege.NORMAL) {
                normalClients.add((UserClient)user);
                
            } else if (user.getPrivilege() == UserPrivilege.VIP) {
                vipClients.add((UserClientVIP)user);
            }
        }

    }

    public List<UserClient> getNormalClients() {
        return normalClients;
    }

    public List<UserClientVIP> getVIPClients() {
        return vipClients;
    }

    public List<UserClient> getAllClients() {
        List<UserClient> allClients = new ArrayList<>();
        allClients.addAll(normalClients);
        allClients.addAll(vipClients);
        return allClients;
    }
    
}
