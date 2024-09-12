package dmit2015.faces;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotBlank;
import org.omnifaces.util.Messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named  // mark this class as a CDI managed object named helloView
@ViewScoped // @RequestedScoped, @ViewScoped, @SessionScoped, @ApplicationScoped
public class HelloView implements Serializable {

    @NotBlank(message = "First Name is required")
    private String firstName;

    private List<String> userList = new ArrayList<>();

    public List<String> getUserList() {
        return userList;
    }

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

    public void onAddUser() {
        userList.add(firstName);
        firstName = null;
    }
}
