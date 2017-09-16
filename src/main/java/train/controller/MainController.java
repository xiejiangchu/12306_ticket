package train.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import train.Booter;
import train.bean.FetchTrain;
import train.bean.Station;
import train.bean.Train;
import train.enums.PurposeCodes;
import train.service.TrainService;
import train.utils.AlertUtils;
import train.utils.StrUtils;
import train.view.LoginView;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@FXMLController
public class MainController implements Initializable {

    private Logger logger= LoggerFactory.getLogger(MainController.class);

    @Autowired
    private Retrofit retrofit;

    @FXML
    ComboBox<Station> station_from;

    @FXML
    ComboBox<Station> station_to;

    @FXML
    ComboBox<String> time_from;

    @FXML
    ComboBox<String> time_to;

    @FXML
    TableView<Train> train_table;

    @FXML
    DatePicker date_from;

    private Map<String, Station> map_code = new TreeMap<>();
    private Map<String, Station> map = new HashMap<>();
    private List<Station> list = new ArrayList<Station>();
    private List<Train> trains;


    private Station station_from_selected;
    private Station station_to_selected;
    private LocalDate date_selected;

    private List<String> hours = Arrays.asList(
            "00:00",
            "01:00",
            "02:00",
            "03:00",
            "04:00",
            "05:00",
            "06:00",
            "07:00",
            "08:00",
            "10:00",
            "11:00",
            "12:00",
            "13:00",
            "14:00",
            "15:00",
            "16:00",
            "17:00",
            "18:00",
            "19:00",
            "20:00",
            "21:00",
            "22:00",
            "23:00"
    );

    @FXML
    public void onLogin(Event event) throws IOException {
        Booter.showView(LoginView.class, Modality.NONE);
    }


    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        TrainService trainService = retrofit.create(TrainService.class);
        trainService.stationName(StrUtils.stationName()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String str = response.body();

                str = str.substring(str.indexOf("'") + 2, str.lastIndexOf("'"));
                String[] array_station = str.split("@");
                for (String item : array_station) {
                    String[] array_item = item.split("\\|");
                    Station station = new Station();
                    station.setAbbr(array_item[0]);
                    station.setName(array_item[1]);
                    station.setCode(array_item[2]);
                    station.setPy(array_item[3]);
                    station.setAbbrL(array_item[4]);
                    station.setIndex(Integer.parseInt(array_item[5]));
                    map.put(station.getName(), station);
                    map_code.put(station.getCode(), station);
                    list.add(station);

                    if (station.getName().contains("宜春")) {
                        station_from_selected = station;
                    }

                    if (station.getName().contains("上海虹桥")) {
                        station_to_selected = station;
                    }
                }
                Collections.sort(list, new Comparator<Station>() {
                    @Override
                    public int compare(Station o1, Station o2) {
                        return o1.getAbbr().compareTo(o2.getAbbr());
                    }
                });
                ObservableList<Station> str2 = FXCollections.observableList(list);
                StringConverter stringConverter = new StringConverter<Station>() {
                    @Override
                    public String toString(Station object) {
                        return object.getName();
                    }

                    @Override
                    public Station fromString(String string) {
                        return map.get(string);
                    }
                };
                station_from.setConverter(stringConverter);
                station_from.setItems(str2);
                station_to.setConverter(stringConverter);
                station_to.setItems(str2);


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                logger.info(t.getMessage());
            }
        });

        station_from.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                station_from_selected = station_from.getValue();
            }
        });

        station_to.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                station_to_selected = station_to.getValue();
            }
        });

        time_from.setItems(FXCollections.observableList(hours));
        time_to.setItems(FXCollections.observableList(hours));


        date_from.setOnAction(new EventHandler() {
            public void handle(Event t) {
                date_selected = date_from.getValue();
            }
        });
        date_from.setDayCellFactory(new javafx.util.Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        DayOfWeek day = DayOfWeek.from(item);
                        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                            this.setTextFill(Color.WHITE);
                            this.setStyle("-fx-background: #FF0000;");
                        }
                    }
                };
            }
        });


        TableColumn<Train, String> col1 = new TableColumn<Train, String>("名称");
        TableColumn<Train, String> col2 = new TableColumn<Train, String>("出发站");
        TableColumn<Train, String> col3 = new TableColumn<Train, String>("到达站");
        TableColumn<Train, String> col4 = new TableColumn<Train, String>("出发时间");
        TableColumn<Train, String> col5 = new TableColumn<Train, String>("到达时间");
        TableColumn<Train, String> col6 = new TableColumn<Train, String>("历时");
        TableColumn<Train, String> col7 = new TableColumn<Train, String>("商务/特等");
        TableColumn<Train, String> col8 = new TableColumn<Train, String>("一等");
        TableColumn<Train, String> col9 = new TableColumn<Train, String>("二等");
        TableColumn<Train, String> col10 = new TableColumn<Train, String>("高级软卧");
        TableColumn<Train, String> col11 = new TableColumn<Train, String>("软卧");
        TableColumn<Train, String> col12 = new TableColumn<Train, String>("硬卧");
        TableColumn<Train, String> col13 = new TableColumn<Train, String>("软卧");
        TableColumn<Train, String> col14 = new TableColumn<Train, String>("硬座");
        TableColumn<Train, String> col15 = new TableColumn<Train, String>("无座");

        col1.setCellValueFactory(new PropertyValueFactory<Train, String>("station_train_code"));
        col2.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Train, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Train, String> param) {
                Train train = param.getValue();
                String name = map_code.get(train.getFrom_station_telecode()).getName();
                return new SimpleObjectProperty<String>(name);
            }
        });
        col3.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Train, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Train, String> param) {
                Train train = param.getValue();
                String name = map_code.get(train.getTo_station_telecode()).getName();
                return new SimpleObjectProperty<String>(name);
            }
        });
        col4.setCellValueFactory(new PropertyValueFactory<Train, String>("start_time"));
        col5.setCellValueFactory(new PropertyValueFactory<Train, String>("arrive_time"));
        col6.setCellValueFactory(new PropertyValueFactory<Train, String>("lishi"));
        col7.setCellValueFactory(new PropertyValueFactory<Train, String>("swz_num"));
        col8.setCellValueFactory(new PropertyValueFactory<Train, String>("zy_num"));
        col9.setCellValueFactory(new PropertyValueFactory<Train, String>("ze_num"));
        col10.setCellValueFactory(new PropertyValueFactory<Train, String>("gr_num"));
        col11.setCellValueFactory(new PropertyValueFactory<Train, String>("rw_num"));
        col12.setCellValueFactory(new PropertyValueFactory<Train, String>("yw_num"));
        col13.setCellValueFactory(new PropertyValueFactory<Train, String>("rw_num"));
        col14.setCellValueFactory(new PropertyValueFactory<Train, String>("yz_num"));
        col15.setCellValueFactory(new PropertyValueFactory<Train, String>("wz_num"));


        train_table.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14);


        logger.info("init");
    }

    public void onFetchStation(Event event) throws IOException {

    }

    public void onFetchTrain(Event event) throws IOException {

        if (station_to_selected == null || station_from_selected == null) {
            AlertUtils.showErrorAlert("请选择站点");
            return;
        }

        if (date_selected == null) {
            AlertUtils.showErrorAlert("请选择日期");
            return;
        }
        TrainService trainService = retrofit.create(TrainService.class);
        trainService.init().execute();


        Call<FetchTrain> call = trainService.queryX(date_selected.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                station_from_selected.getCode(), station_to_selected.getCode(), PurposeCodes.成人.getVal());
        call.enqueue(new Callback<FetchTrain>() {
            @Override
            public void onResponse(Call<FetchTrain> call, Response<FetchTrain> response) {
                if (response.code() == 200) {
                    List<String> list = response.body().getData().getResult();
                    if (trains == null) {
                        trains = new ArrayList<Train>();
                    } else {
                        trains.clear();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        String[] fields = list.get(i).split("\\|");
                        if (fields.length != 36) {
                            continue;
                        }
                        Train train = new Train();
                        train.secretStr = fields[0];
                        train.buttonTextInfo = fields[1];
                        train.train_no = fields[2];
                        train.station_train_code = fields[3];
                        train.start_station_telecode = fields[4];
                        train.end_station_telecode = fields[5];
                        train.from_station_telecode = fields[6];
                        train.to_station_telecode = fields[7];
                        train.start_time = fields[8];
                        train.arrive_time = fields[9];
                        train.lishi = fields[10];
                        train.canWebBuy = fields[11];
                        train.yp_info = fields[12];
                        train.start_train_date = fields[13];
                        train.train_seat_feature = fields[14];
                        train.location_code = fields[15];
                        train.from_station_no = fields[16];
                        train.to_station_no = fields[17];
                        train.is_support_card = fields[18];
                        train.controlled_train_flag = fields[19];
                        train.gg_num = StringUtils.isEmpty(fields[20]) ? fields[20] : "--";
                        train.gr_num = StringUtils.isEmpty(fields[21]) ? fields[21] : "--";
                        train.qt_num = StringUtils.isEmpty(fields[22]) ? fields[22] : "--";
                        train.rw_num = StringUtils.isEmpty(fields[23]) ? fields[23] : "--";
                        train.rz_num = StringUtils.isEmpty(fields[24]) ? fields[24] : "--";
                        train.tz_num = StringUtils.isEmpty(fields[25]) ? fields[25] : "--";
                        train.wz_num = StringUtils.isEmpty(fields[26]) ? fields[26] : "--";
                        train.yb_num = StringUtils.isEmpty(fields[27]) ? fields[27] : "--";
                        train.yw_num = StringUtils.isEmpty(fields[28]) ? fields[28] : "--";
                        train.yz_num = StringUtils.isEmpty(fields[29]) ? fields[29] : "--";
                        train.ze_num = StringUtils.isEmpty(fields[30]) ? fields[30] : "--";
                        train.zy_num = StringUtils.isEmpty(fields[31]) ? fields[31] : "--";
                        train.swz_num = StringUtils.isEmpty(fields[32]) ? fields[32] : "--";
                        train.srrb_num = StringUtils.isEmpty(fields[33]) ? fields[33] : "--";
                        train.yp_ex = fields[34];
                        train.seat_types = fields[35];
                        trains.add(train);
                    }
                }


                train_table.setItems(FXCollections.observableArrayList(trains));

            }

            @Override
            public void onFailure(Call<FetchTrain> call, Throwable t) {
                logger.info(t.getMessage());
            }
        });


    }

    private String getDateString(String dateString) {
        Date date = null;
        DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-M-d");
        try {
            date = dateFormat1.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateFormat2.format(date);
    }
}
