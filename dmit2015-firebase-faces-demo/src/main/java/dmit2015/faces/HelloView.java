package dmit2015.faces;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.omnifaces.util.Messages;

@Named  // mark this class as a CDI managed object named helloView
@RequestScoped // @ViewScoped, @SessionScoped, @ApplicationScoped
public class HelloView {

    private String firstName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void onSayHello() {
        Messages.addGlobalInfo("Welcome {0} to the Faces Application!",
                firstName);
        Messages.addGlobalError("Welcome {0} to the Faces Application!",
                firstName);
    }
}
