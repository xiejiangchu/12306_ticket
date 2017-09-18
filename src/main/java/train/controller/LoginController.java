package train.controller;

import com.alibaba.fastjson.JSON;
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
import org.springframework.util.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import train.bean.CaptureResult;
import train.bean.MyPoint2D;
import train.service.TrainService;
import train.utils.AlertUtils;

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

    private final int R = 12;

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

    private TrainService trainService;

    List<MyPoint2D> points = new ArrayList<>();

    private String uamtk;
    private boolean capture_check = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        drawCanvas();
        trainService = retrofit.create(TrainService.class);
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

            if (!remove) {
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
    public void onLogin(Event e) throws IOException {
        String username_str = username.getText();
        String password_str = password.getText();
        if (StringUtils.isEmpty(username_str) || StringUtils.isEmpty(password_str)) {
            AlertUtils.showErrorAlert("未填写用户名和密码");
            return;
        }

        if (!capture_check) {
            AlertUtils.showErrorAlert("图像验证码未通过");
            return;
        }


        logger.info("========================   登陆    ========================");
        trainService.login(username_str, password_str, "otn").execute().body();

        logger.info("========================   newapptk    ========================");
        String newapptk = trainService.uamtk("otn").execute().body().getNewapptk();
        logger.info("========================   newapptk:  " + newapptk + "    ========================");


        logger.info("========================   uamauthclient    ========================");
        trainService.uamauthclient(newapptk).execute();
    }

    @FXML
    public void onRemeberMe(Event e) {

    }

    private void fetchImage() {
        points.clear();
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
            gc.setFill(Color.RED);
            gc.fillOval(point.getX()-R/2, point.getY()-R/2, R, R);
        }
    }

    @FXML
    public void onCaptureHandIn(Event e) throws IOException {

        if (points == null || points.size() == 0) {
            AlertUtils.showErrorAlert("验证码未输入");
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < points.size(); i++) {
            builder.append(points.get(i).toString());
            if (i != (points.size() - 1)) {
                builder.append(",");
            }
        }

        System.out.println(builder.toString());

        Call<String> call = trainService.captureCheck(builder.toString(), "E", "sjrand");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    if (StringUtils.isEmpty(response.body())) {
                        capture_check = true;
                        logger.info("验证码验证成功:,无消息返回");
                    } else {
                        CaptureResult result = JSON.parseObject(response.body(), CaptureResult.class);
                        if (result.getResult_message().contains("失败")) {
                            logger.info("验证码验证失败1:" + response.body());
                            capture_check = false;
                        } else {
                            logger.info("验证码验证成功2:" + response.body());
                            capture_check = true;
                        }
                    }
                } else {
                    System.out.println(response.code() + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                logger.info("验证码验证失败3:" + t.getMessage());
            }
        });

    }
}
