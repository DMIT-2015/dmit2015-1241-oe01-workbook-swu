package dmit2015.faces;

import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

@Named("currentCounterViewScopedView")
@ViewScoped // create this object for one HTTP request and keep in memory if the next is for the same page
// class must implement Serializable
public class CounterViewScopedView implements Serializable {

    // Declare read/write properties (field + getter + setter) for each form field
    @Getter @Setter
    // private Counter selectedCounter;
    private int counter = 0;

//    public int getCounter() {
//        return counter;
//    }

//    public void setCounter(int counter) {
//        this.counter = counter;
//    }

    public void onSubmit() {
        try {
            // Increment the counter by 1
            counter++;
            // Send a Faces info message with the current value
            Messages.addGlobalInfo("Counter = {0}", counter);

        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /**
     * This method is used to handle exceptions and display root cause to user.
     *
     * @param ex The Exception to handle.
     */
    protected void handleException(Exception ex) {
        var details = new StringBuilder();
        Throwable causes = ex;
        while (causes.getCause() != null) {
            details.append(ex.getMessage());
            details.append("    Caused by:");
            details.append(causes.getCause().getMessage());
            causes = causes.getCause();
        }
        Messages.create(ex.getMessage()).detail(details.toString()).error().add("errors");
    }

}