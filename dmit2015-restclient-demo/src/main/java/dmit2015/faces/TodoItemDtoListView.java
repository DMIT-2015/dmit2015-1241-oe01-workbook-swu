package dmit2015.faces;

import dmit2015.restclient.TodoItemDto;
import dmit2015.restclient.TodoItemDtoMpRestClient;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Named("currentTodoItemDtoListView")
@ViewScoped
public class TodoItemDtoListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    @RestClient
    private TodoItemDtoMpRestClient _todoItemDtoMpRestClient;

    @Getter
    private List<TodoItemDto> todoItemDtoList;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            todoItemDtoList = _todoItemDtoMpRestClient.findAll();
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /**
     * This method is used to handle exceptions and display root cause to user.
     * @param ex The Exception to handle.
     */
    protected void handleException (Exception ex){
        StringBuilder details = new StringBuilder();
        Throwable causes = ex;
        while(causes.getCause() != null){
            details.append(ex.getMessage());
            details.append("    Caused by:");
            details.append(causes.getCause().getMessage());
            causes = causes.getCause();
        }
        Messages.create(ex.getMessage()).detail(details.toString()).error().add("errors");
    }
}