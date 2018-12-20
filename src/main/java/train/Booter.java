package train;

import com.aquafx_project.AquaFx;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.application.Platform;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import train.view.MainView;

/**
 * Created by xie on 17/9/12.
 */
@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
public class Booter extends AbstractJavaFxApplicationSupport implements ApplicationContextAware {


    private static AnnotationConfigApplicationContext application;

    public static void main(String[] args) {
        MySplashScreen mySplashScreen = new MySplashScreen();
        launchApp(Booter.class, MainView.class, mySplashScreen, args);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        application = (AnnotationConfigApplicationContext) applicationContext;
    }

    @Override
    public void stop() {
        application.close();
        Platform.exit();
        System.exit(0);
    }
}
