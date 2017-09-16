package train.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import train.bean.MyPoint2D;
import train.service.TrainService;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by xie on 17/9/16.
 */
@FXMLController
public class LoginController implements Initializable {
    private final static String CAPTURE_NAME = "capture.jpeg";

    private final int R = 5;

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private Retrofit retrofit;

    @FXML
    private Canvas canvas;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    private Random random = new Random();

    List<MyPoint2D> points = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fetchImage();
    }

    @FXML
    public void onCanvasClicked(Event e) {
        if (e instanceof MouseEvent) {
            MouseEvent event = (MouseEvent) e;

            boolean remove = false;
            for (int i = 0; i < points.size(); i++) {
                MyPoint2D item = points.get(i);
                double dis = (item.getX() - event.getX()) * (item.getX() - event.getX()) + (item.getY() - event.getY()) * (item.getY() - event.getY());
                if (dis < R * R) {
                    points.remove(i);
                    remove = true;
                    break;
                }
            }

            if(!remove){
                if ((int) event.getY() >= 30) {
                    MyPoint2D myPoint2D = new MyPoint2D((int) event.getX(), (int) event.getY());
                    points.add(myPoint2D);
                }
            }


            drawCanvas();

            System.out.println(points);
        }
    }

    @FXML
    public void onFetchImage(Event e) {
        fetchImage();
    }

    @FXML
    public void onLogin(Event e) {

    }

    @FXML
    public void onRemeberMe(Event e) {

    }

    private void fetchImage() {
        points.clear();
        TrainService trainService = retrofit.create(TrainService.class);
        Call<ResponseBody> call = trainService.captchaImage("E", "login", "sjrand", random.nextDouble());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                File file = new File(CAPTURE_NAME);
                InputStream in = null;
                FileOutputStream out = null;

                try {
                    in = response.body().byteStream();
                    out = new FileOutputStream(file);
                    int c;

                    while ((c = in.read()) != -1) {
                        out.write(c);
                    }
                    drawCanvas();
                } catch (IOException e) {

                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                logger.info("获取图像失败" + t.getMessage());
            }
        });
    }

    private void drawCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        try {
            canvas.getGraphicsContext2D().drawImage(new Image(new FileInputStream(new File(CAPTURE_NAME))), 0, 0);
        } catch (FileNotFoundException e) {
            logger.info("绘制图像失败" + e.getMessage());
        }

        for (int i = 0; i < points.size(); i++) {
            MyPoint2D point = points.get(i);
            gc.setFill(Color.FORESTGREEN);
            gc.fillOval(point.getX(), point.getY(), R, R);
        }
    }
}
