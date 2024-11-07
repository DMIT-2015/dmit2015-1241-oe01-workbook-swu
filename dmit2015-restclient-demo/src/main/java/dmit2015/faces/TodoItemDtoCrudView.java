package dmit2015.faces;

import dmit2015.restclient.TodoItemDto;
import dmit2015.restclient.TodoItemDtoMpRestClient;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.List;

@Named("currentTodoItemDtoCrudView")
@ViewScoped
public class TodoItemDtoCrudView implements Serializable {

    @Inject
    @RestClient
    private TodoItemDtoMpRestClient _todoItemDtoMpRestClient;

    @Getter
    private List<TodoItemDto> todoItemDtoList;

    @Getter
    @Setter
    private TodoItemDto selectedTodoItemDto;

    @Getter
    @Setter
    private Long editId;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            todoItemDtoList = _todoItemDtoMpRestClient.findAll();
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }

    public void onOpenNew() {
        selectedTodoItemDto = new TodoItemDto();
        editId = null;
    }

    public void onSave() {
        if (editId == null) {
            try {
                Response response = _todoItemDtoMpRestClient.create(selectedTodoItemDto);
                String location = response.getHeaderString("Location");
                String idValue = location.substring(location.lastIndexOf("/") + 1);
                Messages.addFlashGlobalInfo("Create was successful. {0}", idValue);
                todoItemDtoList = _todoItemDtoMpRestClient.findAll();
            } catch (Exception e) {
                e.printStackTrace();
                Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
            }
        } else {
            try {
                _todoItemDtoMpRestClient.update(editId, selectedTodoItemDto);
                Messages.addFlashGlobalInfo("Update was successful.");
            } catch (Exception e) {
                e.printStackTrace();
                Messages.addGlobalError("Update was not successful.");
            }
        }

        PrimeFaces.current().executeScript("PF('manageTodoItemDtoDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-TodoItemDtos");
    }

    public void onDelete() {
        try {
            _todoItemDtoMpRestClient.delete(editId);
            editId = null;
            selectedTodoItemDto = null;
            Messages.addGlobalInfo("Delete was successful.");
            todoItemDtoList = _todoItemDtoMpRestClient.findAll();
            PrimeFaces.current().ajax().update("form:messages", "form:dt-TodoItemDtos");
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Delete not successful.");
        }
    }

}