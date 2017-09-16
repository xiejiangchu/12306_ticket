package train;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import train.view.MainView;

/**
 * Created by xie on 17/9/12.
 */
@SpringBootApplication
public class Booter extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        MySplashScreen mySplashScreen = new MySplashScreen();

        launchApp(Booter.class, MainView.class, mySplashScreen, args);
    }
}
