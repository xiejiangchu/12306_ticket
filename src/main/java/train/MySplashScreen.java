package train;

import com.aquafx_project.AquaFx;
import de.felixroske.jfxsupport.SplashScreen;
import javafx.scene.Parent;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Created by xie on 17/9/16.
 */
public class MySplashScreen extends SplashScreen {

    public MySplashScreen() {
        super();
        AquaFx.style();
    }

    @Override
    public Parent getParent() {
        final ImageView imageView = new ImageView(getImagePath());
        final ProgressBar splashProgressBar = new ProgressBar();
        splashProgressBar.setPrefWidth(imageView.getImage().getWidth());
        splashProgressBar.setCache(true);
        splashProgressBar.setPrefHeight(2);

        final VBox vbox = new VBox();
        vbox.getChildren().addAll(imageView, splashProgressBar);

        return vbox;
    }

    @Override
    public String getImagePath() {
        String str = Thread.currentThread().getContextClassLoader().getResource("splash.jpg").toString();
        return str;
    }
}
