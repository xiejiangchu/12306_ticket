package train.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Retrofit;

@FXMLController
public class ToolController {

    @Autowired
    private Retrofit retrofit;

    public void doSomething(final Event e) {
        System.out.println("You pressed some button!");
    }
}
