package train;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import train.view.MainView;

/**
 * Created by xie on 17/9/12.
 */
@SpringBootApplication
public class Booter extends AbstractJavaFxApplicationSupport{

    @Autowired
    ApplicationContext applicationContext;

    public static void main(String[] args) {
        MySplashScreen mySplashScreen = new MySplashScreen();
        launchApp(Booter.class, MainView.class, mySplashScreen, args);
    }

    @Override
    public void stop() throws Exception {
        if(applicationContext!=null){
            System.out.println("yes");
        }
    }
}
