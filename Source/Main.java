//This class appears to be the entry point of the application. 

import java.nio.file.Paths;

import javax.swing.SwingUtilities;

import Communication.InstanceManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InstanceManager();
        });
    
    }
}
