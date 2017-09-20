package train.controller;

import com.alibaba.fastjson.JSON;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
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
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.functions.Action1;
import train.Booter;
import train.bean.*;
import train.config.TextAreaAppender;
import train.enums.OrderScopeType;
import train.enums.PurposeCodes;
import train.enums.QueryType;
import train.enums.TourFlag;
import train.service.PingService;
import train.service.TrainService;
import train.utils.AlertUtils;
import train.utils.StrUtils;
import train.view.LoginView;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@FXMLController
public class MainController implements Initializable {

    private final static Logger logger = LoggerFactory.getLogger(MainController.class);
    /**
     * 查询次数
     */
    private int count = 0;

    @FXML
    private CheckBox ck_gc;
    @FXML
    private CheckBox ck_d;
    @FXML
    private CheckBox ck_z;
    @FXML
    private CheckBox ck_t;
    @FXML
    private CheckBox ck_k;
    @FXML
    private CheckBox ck_q;


    @Autowired
    private Retrofit retrofit;

    @Autowired
    private PingService pingService;

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
    TableView<OrderDTO> order_table;

    @FXML
    ListView<String> host_listView;

    @FXML
    TableView<PingHost> host_table;

    @FXML
    TableView<Passenger> passenger_table;

    @FXML
    private Label label_count;

    @FXML
    DatePicker date_from;

    @FXML
    TextArea log_textArea;

    @FXML
    private TabPane train_tab;

    private TrainService trainService;


    private static List<String> HOSTS = Arrays.asList("182.243.62.48", "27.148.151.17",
            "219.147.233.232", "59.44.30.36", "183.58.18.36", "14.18.201.47", "42.123.105.35",
            "14.215.9.83", "59.49.42.252", "58.222.19.61", "101.227.66.207", "125.90.206.182",
            "61.156.243.247", "27.159.182.50", "222.180.166.229", "202.98.156.62", "218.92.221.15",
            "218.92.209.74", "180.97.217.77", "183.62.114.247", "183.134.9.58", "61.147.227.247", "58.222.19.72",
            "222.245.77.74", "220.162.97.136", "182.140.236.27", "61.136.167.78", "117.23.2.85", "222.218.45.216",
            "42.81.9.47", "125.46.22.126", "101.227.102.199", "182.106.194.107", "220.162.97.209", "111.206.186.244",
            "58.222.42.9", "123.128.14.201", "115.231.74.76", "153.35.174.108", "221.202.204.253", "150.138.169.233",
            "220.194.200.232", "113.207.77.192", "61.155.237.56", "124.164.8.65", "61.167.54.109", "139.215.210.59",
            "111.62.244.177", "123.138.157.109", "117.148.165.154", "117.23.6.81", "113.107.57.43");
    private Map<String, Station> map_code = new TreeMap<>();
    private Map<String, Station> map = new HashMap<>();
    private List<Station> list = new ArrayList<Station>();
    private List<Train> trains;
    private QueryMyOrder queryMyOrder;
    private GetPassengerDto getPassengerDto;


    private Station station_from_selected;
    private Station station_to_selected;
    private LocalDate date_selected;


    private LocalDate start_date;
    private LocalDate end_date;


    //提交订单
    private String secretStr;

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
    public void onLoginView(Event event) throws IOException {
        Booter.showView(LoginView.class, Modality.NONE);
    }

    @Override
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        setLabelCount();
        TextAreaAppender.setTextArea(log_textArea);

        train_tab.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                String tab_name = newValue.getText();
                if (tab_name.equals("服务器测速")) {

                } else if (tab_name.equals("服务器测速")) {

                } else if (tab_name.equals("订单列表")) {
                    initOrderTab();

                } else if (tab_name.equals("联系人列表")) {
                    initPassengerTab();

                } else if (tab_name.equals("日志")) {

                }
            }
        });


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
        station_to.setConverter(stringConverter);

        trainService = retrofit.create(TrainService.class);

        start_date = LocalDate.now();
        end_date = start_date.plusDays(30);

        date_selected = LocalDate.now();
        date_from.setValue(date_selected);

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
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                station_from_selected = station;
                                station_from.setValue(station_from_selected);
                            }
                        });
                    }

                    if (station.getName().contains("上海虹桥")) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                station_to_selected = station;
                                station_to.setValue(station_to_selected);
                            }
                        });
                    }
                }
                Collections.sort(list, new Comparator<Station>() {
                    @Override
                    public int compare(Station o1, Station o2) {
                        return o1.getAbbr().compareTo(o2.getAbbr());
                    }
                });
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ObservableList<Station> str2 = FXCollections.observableList(list);

                        station_from.setItems(str2);

                        station_to.setItems(str2);
                    }
                });
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
                        if (item.compareTo(start_date) >= 0 && (item.compareTo(end_date) <= 0)) {
                            this.setTextFill(Color.WHITE);
                            this.setStyle("-fx-background: #55CC55;");
                        } else {
                            this.setTextFill(Color.GRAY);
                            this.setStyle("-fx-background: #999999;");
                        }

                    }
                };
            }
        });


        initTrainTable();
        //查询订单
        initOrderTable();

        initPingTable();

        initPassengerTable();


        host_listView.setItems(FXCollections.observableList(HOSTS));


        logger.info("init");
    }


    private void initPingTable() {
        TableColumn<PingHost, String> host_table_col1 = new TableColumn<PingHost, String>("名称");
        TableColumn<PingHost, String> host_table_col2 = new TableColumn<PingHost, String>("Ping");
        host_table_col1.setCellValueFactory(new PropertyValueFactory<PingHost, String>("host"));
        host_table_col2.setCellValueFactory(new PropertyValueFactory<PingHost, String>("ping"));
        host_table.getColumns().addAll(host_table_col1, host_table_col2);
    }

    private void initTrainTable() {
        //查询火车
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
        col1.getStyleClass().add("text-bold");
        col2.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Train, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Train, String> param) {
                Train train = param.getValue();
                String name = map_code.get(train.getFrom_station_telecode()).getName();
                return new SimpleObjectProperty<String>(name);
            }
        });
        col2.getStyleClass().add("text-green");
        col3.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Train, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Train, String> param) {
                Train train = param.getValue();
                String name = map_code.get(train.getTo_station_telecode()).getName();
                return new SimpleObjectProperty<String>(name);
            }
        });
        col3.getStyleClass().add("text-red");
        col4.setCellValueFactory(new PropertyValueFactory<Train, String>("start_time"));
        col5.setCellValueFactory(new PropertyValueFactory<Train, String>("arrive_time"));
        col6.setCellValueFactory(new PropertyValueFactory<Train, String>("lishi"));
        col7.setCellValueFactory(new PropertyValueFactory<Train, String>("tz_num"));
        col8.setCellValueFactory(new PropertyValueFactory<Train, String>("zy_num"));
        col9.setCellValueFactory(new PropertyValueFactory<Train, String>("ze_num"));
        col10.setCellValueFactory(new PropertyValueFactory<Train, String>("gr_num"));
        col11.setCellValueFactory(new PropertyValueFactory<Train, String>("rw_num"));
        col12.setCellValueFactory(new PropertyValueFactory<Train, String>("yw_num"));
        col13.setCellValueFactory(new PropertyValueFactory<Train, String>("rw_num"));
        col14.setCellValueFactory(new PropertyValueFactory<Train, String>("yz_num"));
        col15.setCellValueFactory(new PropertyValueFactory<Train, String>("wz_num"));

        train_table.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15);
    }

    private void initOrderTable() {
        TableColumn<OrderDTO, String> col1 = new TableColumn<OrderDTO, String>("订单号");
        TableColumn<OrderDTO, String> col2 = new TableColumn<OrderDTO, String>("时间");
        TableColumn<OrderDTO, String> col3 = new TableColumn<OrderDTO, String>("数量");
        TableColumn<OrderDTO, String> col4 = new TableColumn<OrderDTO, String>("金额");
        TableColumn<OrderDTO, String> col5 = new TableColumn<OrderDTO, String>("票务");
//        TableColumn<OrderDTO, String> col6 = new TableColumn<OrderDTO, String>("历时");
//        TableColumn<OrderDTO, String> col7 = new TableColumn<OrderDTO, String>("商务/特等");
//        TableColumn<OrderDTO, String> col8 = new TableColumn<OrderDTO, String>("一等");
//        TableColumn<OrderDTO, String> col9 = new TableColumn<OrderDTO, String>("二等");
//        TableColumn<OrderDTO, String> col10 = new TableColumn<OrderDTO, String>("高级软卧");
//        TableColumn<OrderDTO, String> col11 = new TableColumn<OrderDTO, String>("软卧");
//        TableColumn<OrderDTO, String> col12 = new TableColumn<OrderDTO, String>("硬卧");
//        TableColumn<OrderDTO, String> col13 = new TableColumn<OrderDTO, String>("软卧");
//        TableColumn<OrderDTO, String> col14 = new TableColumn<OrderDTO, String>("硬座");
//        TableColumn<OrderDTO, String> col15 = new TableColumn<OrderDTO, String>("无座");

        col1.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("sequence_no"));
        col1.getStyleClass().add("text-bold");
        col2.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("order_date"));
        col2.getStyleClass().add("text-green");
        col3.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("ticket_totalnum"));
        col3.getStyleClass().add("text-red");
        col4.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<OrderDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderDTO, String> param) {
                OrderDTO order = param.getValue();
                String name = String.valueOf(order.getTicket_price_all() / 100);
                return new SimpleObjectProperty<String>(name);
            }
        });
        col5.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<OrderDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderDTO, String> param) {
                OrderDTO order = param.getValue();
                StringBuilder stringBuilder = new StringBuilder();

                String name = JSON.toJSONString(order.getTickets().get(0).getStationTrainDTO());
                return new SimpleObjectProperty<String>(name);
            }
        });
//        col6.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("lishi"));
//        col7.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("tz_num"));
//        col8.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("zy_num"));
//        col9.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("ze_num"));
//        col10.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("gr_num"));
//        col11.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("rw_num"));
//        col12.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("yw_num"));
//        col13.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("rw_num"));
//        col14.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("yz_num"));
//        col15.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("wz_num"));


        order_table.getColumns().addAll(col1, col2, col3, col4, col5);
//        order_table.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15);
    }


    private void initPassengerTable() {
        TableColumn<Passenger, String> col1 = new TableColumn<Passenger, String>("姓名");
        TableColumn<Passenger, String> col2 = new TableColumn<Passenger, String>("出生日期");
        TableColumn<Passenger, String> col3 = new TableColumn<Passenger, String>("身份证");
        TableColumn<Passenger, String> col4 = new TableColumn<Passenger, String>("身份证编号");
        TableColumn<Passenger, String> col5 = new TableColumn<Passenger, String>("类型");
//        TableColumn<OrderDTO, String> col6 = new TableColumn<OrderDTO, String>("历时");
//        TableColumn<OrderDTO, String> col7 = new TableColumn<OrderDTO, String>("商务/特等");
//        TableColumn<OrderDTO, String> col8 = new TableColumn<OrderDTO, String>("一等");
//        TableColumn<OrderDTO, String> col9 = new TableColumn<OrderDTO, String>("二等");
//        TableColumn<OrderDTO, String> col10 = new TableColumn<OrderDTO, String>("高级软卧");
//        TableColumn<OrderDTO, String> col11 = new TableColumn<OrderDTO, String>("软卧");
//        TableColumn<OrderDTO, String> col12 = new TableColumn<OrderDTO, String>("硬卧");
//        TableColumn<OrderDTO, String> col13 = new TableColumn<OrderDTO, String>("软卧");
//        TableColumn<OrderDTO, String> col14 = new TableColumn<OrderDTO, String>("硬座");
//        TableColumn<OrderDTO, String> col15 = new TableColumn<OrderDTO, String>("无座");

        col1.setCellValueFactory(new PropertyValueFactory<Passenger, String>("passenger_name"));
        col1.getStyleClass().add("text-bold");
        col2.setCellValueFactory(new PropertyValueFactory<Passenger, String>("born_date"));
        col2.getStyleClass().add("text-green");
        col3.setCellValueFactory(new PropertyValueFactory<Passenger, String>("passenger_id_type_name"));
        col3.getStyleClass().add("text-red");
        col4.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Passenger, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Passenger, String> param) {
                Passenger passenger = param.getValue();
                String name = passenger.getPassenger_id_no();
                return new SimpleObjectProperty<String>(name);
            }
        });
        col5.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Passenger, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Passenger, String> param) {
                Passenger passenger = param.getValue();
                String name = passenger.getPassenger_type_name();
                return new SimpleObjectProperty<String>(name);
            }
        });
//        col6.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("lishi"));
//        col7.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("tz_num"));
//        col8.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("zy_num"));
//        col9.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("ze_num"));
//        col10.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("gr_num"));
//        col11.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("rw_num"));
//        col12.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("yw_num"));
//        col13.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("rw_num"));
//        col14.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("yz_num"));
//        col15.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("wz_num"));


        passenger_table.getColumns().addAll(col1, col2, col3, col4, col5);
//        order_table.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15);
    }

    private void initOrderTab() {
        Date now = DateTime.now().toDate();
        Date end = DateTime.now().plusDays(20).toDate();
        Call<String> call = trainService.queryMyOrder(QueryType.乘车日期.getVal(), StrUtils.formatDate(now), StrUtils.formatDate(end), OrderScopeType.所有.getVal(), 10, 0, "G", null);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    if (response.headers().get("Content-Type").contains("application/json")) {
                        logger.info("获取订单成功");
                        queryMyOrder = JSON.parseObject(response.body(), QueryMyOrder.class);
                        order_table.setItems(FXCollections.observableArrayList(queryMyOrder.getData().getOrderDTODataList()));
                    } else {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Booter.showView(LoginView.class, Modality.NONE);
                            }
                        });
                    }
                } else {
                    System.out.println(response);
                    logger.info("获取订单失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                logger.info("获取订单失败");
            }
        });
    }

    private void initPassengerTab() {
        Call<GetPassengerDto> call = trainService.getPassengerDTOs();
        call.enqueue(new Callback<GetPassengerDto>() {
            @Override
            public void onResponse(Call<GetPassengerDto> call, Response<GetPassengerDto> response) {
                if (!response.body().getData().getExMsg().contains("登录")) {
                    logger.info("获取联系人成功");
                    ObservableList<Passenger> passengers = FXCollections.observableArrayList(response.body().getData().getNormal_passengers());
                    passenger_table.setItems(passengers);
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Booter.showView(LoginView.class, Modality.NONE);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetPassengerDto> call, Throwable t) {
                logger.info("获取联系人失败");
            }
        });
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

        trainService.init().execute();


        Call<FetchTrain> call = trainService.queryX(date_selected.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                station_from_selected.getCode(), station_to_selected.getCode(), PurposeCodes.成人.getVal());
        call.enqueue(new Callback<FetchTrain>() {
            @Override
            public void onResponse(Call<FetchTrain> call, Response<FetchTrain> response) {
                if (trains == null) {
                    trains = new ArrayList<Train>();
                } else {
                    trains.clear();
                }
                if (response.code() == 200) {
                    List<String> list = response.body().getData().getResult();
                    if (list == null) {
                        return;
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
                        train.gg_num = !StringUtils.isEmpty(fields[20]) ? fields[20] : "--";
                        train.gr_num = !StringUtils.isEmpty(fields[21]) ? fields[21] : "--";
                        train.qt_num = !StringUtils.isEmpty(fields[22]) ? fields[22] : "--";
                        train.rw_num = !StringUtils.isEmpty(fields[23]) ? fields[23] : "--";
                        train.rz_num = !StringUtils.isEmpty(fields[24]) ? fields[24] : "--";
                        train.tz_num = !StringUtils.isEmpty(fields[25]) ? fields[25] : "--";
                        train.wz_num = !StringUtils.isEmpty(fields[26]) ? fields[26] : "--";
                        train.yb_num = !StringUtils.isEmpty(fields[27]) ? fields[27] : "--";
                        train.yw_num = !StringUtils.isEmpty(fields[28]) ? fields[28] : "--";
                        train.yz_num = !StringUtils.isEmpty(fields[29]) ? fields[29] : "--";
                        train.ze_num = !StringUtils.isEmpty(fields[30]) ? fields[30] : "--";
                        train.zy_num = !StringUtils.isEmpty(fields[31]) ? fields[31] : "--";
                        train.swz_num = !StringUtils.isEmpty(fields[32]) ? fields[32] : "--";
                        train.srrb_num = !StringUtils.isEmpty(fields[33]) ? fields[33] : "--";
                        train.yp_ex = fields[34];
                        train.seat_types = fields[35];
                        trains.add(train);
                    }
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        train_table.setItems(FXCollections.observableArrayList(filterTrain(trains)));
                        count++;
                        setLabelCount();
                    }
                });

            }

            @Override
            public void onFailure(Call<FetchTrain> call, Throwable t) {
                logger.info(t.getMessage());
            }
        });


    }

    private void setLabelCount() {
        String count_str = "第" + count + "次查询";
        label_count.setText(count_str);
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

    @FXML
    public void onClearLog(Event e) {
        log_textArea.clear();
    }

    @FXML
    public void onPingHost(Event e) {

        List<PingHost> pingHostList = new ArrayList<>();
        rx.Observable.from(HOSTS).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                int result = pingService.ping(s);
                PingHost pingHost = new PingHost();
                pingHost.setHost(s);
                pingHost.setPing(result);
                pingHostList.add(pingHost);
            }
        });

        host_table.setItems(FXCollections.observableList(pingHostList));
    }


    private List<Train> filterTrain(List<Train> trains) {
        StringBuilder stringBuilder = new StringBuilder();
        if (ck_gc.isSelected()) {
            stringBuilder.append("G");
        }
        if (ck_d.isSelected()) {
            stringBuilder.append("D");
        }
        if (ck_z.isSelected()) {
            stringBuilder.append("Z");
        }
        if (ck_t.isSelected()) {
            stringBuilder.append("T");
        }
        if (ck_k.isSelected()) {
            stringBuilder.append("K");
        }
        if (ck_q.isSelected()) {
            stringBuilder.append("QT");
        }
        List<Train> out = trains.stream().filter(item -> {
            String str = item.getStation_train_code();
            String str2 = "";
            if (str != null && !"".equals(str)) {
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) >= 65 && str.charAt(i) <= 90) {
                        str2 += str.charAt(i);
                    } else {
                        break;
                    }
                }
            }
            if (stringBuilder.toString().contains(str2)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return out;
    }


    @FXML
    public void submitOrderRequest() {
        if (StringUtils.isEmpty(secretStr)) {
            AlertUtils.showErrorAlert("请选择车次");
            return;
        }
        trainService.submitOrderRequest(secretStr, date_selected.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                date_selected.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), TourFlag.单程.getVal(), PurposeCodes.成人.getVal(),
                station_from_selected.getCode(), station_to_selected.getCode(), null);
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }
}
