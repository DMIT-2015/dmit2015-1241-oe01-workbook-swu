package dmit2015.faces;

import dmit2015.restclient.TodoItemDto;
import dmit2015.restclient.TodoItemDtoMpRestClient;

import jakarta.ws.rs.core.Response;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Messages;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("currentTodoItemDtoCreateView")
@RequestScoped
public class TodoItemDtoCreateView {

    @Inject
    @RestClient
    private TodoItemDtoMpRestClient _todoItemDtoMpRestClient;

    @Getter
    private TodoItemDto newTodoItemDto = new TodoItemDto();

    public String onCreateNew() {
        String nextPage = null;
        try {
            Response response = _todoItemDtoMpRestClient.create(newTodoItemDto);
            String location = response.getHeaderString("Location");
            String idValue = location.substring(location.lastIndexOf("/") + 1);
            newTodoItemDto = new TodoItemDto();
            Messages.addFlashGlobalInfo("Create was successful. {0}", idValue);
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}