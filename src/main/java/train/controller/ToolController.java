package train.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.event.Event;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Retrofit;

@FXMLController
public class ToolController {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(ToolController.class);

    @Autowired
    private Retrofit retrofit;

    public void doSomething(final Event e) {
        logger.info("You pressed some button!");
    }
}
