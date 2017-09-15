package train;

import train.view.FirstView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by xie on 17/9/12.
 */
@SpringBootApplication
public class Booter extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launchApp(Booter.class, FirstView.class, args);
    }
}
