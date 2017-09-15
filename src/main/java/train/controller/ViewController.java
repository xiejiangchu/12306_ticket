package train.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.event.Event;
import javafx.stage.Modality;
import train.view.ToolView;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Retrofit;
import train.Booter;

import java.io.IOException;

@FXMLController
public class ViewController {

    @Autowired
    private Retrofit retrofit;

    public void showToolWindow(Event event) throws IOException {
        Booter.showView(ToolView.class, Modality.NONE);
    }
}
