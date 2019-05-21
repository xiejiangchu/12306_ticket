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
import okhttp3.ResponseBody;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import train.Booter;
import train.bean.*;
import train.config.CdnConfig;
import train.config.RetrofitConfig;
import train.config.TextAreaAppender;
import train.enums.*;
import train.service.PingService;
import train.service.TrainService;
import train.utils.AlertUtils;
import train.utils.Constants;
import train.utils.GUIUtils;
import train.utils.StringUtils;
import train.view.LoginView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static train.utils.StringUtils.joinPassanger;
import static train.utils.StringUtils.joinoldPassanger;

@FXMLController
public class MainController implements Initializable {

    private final static Logger logger = LoggerFactory.getLogger(MainController.class);
    private final static String CAPTURE_NAME = "capture-ticket.jpeg";
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

    List<Passenger> normal_passengers;


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
        //ping
        host_table.setItems(pingService.getHost());


        setLabelCount();
        TextAreaAppender.setTextArea(log_textArea);

        train_tab.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                String tab_name = newValue.getText();
                if (tab_name.equals("服务器测速")) {

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

        /**
         * 初始化
         */
        trainService = retrofit.create(TrainService.class);
        try {
            trainService.init().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        start_date = LocalDate.now();
        end_date = start_date.plusDays(30);

        date_selected = LocalDate.now().plusDays(28);
        date_from.setValue(date_selected);

        trainService.stationName(Constants.STATION_VERSION).enqueue(new Callback<String>() {
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
                        if (null != str2) {
                            station_from.setItems(str2);
                            station_to.setItems(str2);
                        }
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

        /**
         * 提前获取联系人
         */
        initPassengerTab();


        host_listView.setItems(FXCollections.observableList(CdnConfig.getHosts()));


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
        TableColumn<Train, String> station_train_code = new TableColumn<Train, String>("名称");
        TableColumn<Train, String> from_station_telecode = new TableColumn<Train, String>("出发站");
        TableColumn<Train, String> to_station_telecode = new TableColumn<Train, String>("到达站");
        TableColumn<Train, String> start_to_end = new TableColumn<Train, String>("区间");
        TableColumn<Train, String> start_time = new TableColumn<Train, String>("出发时间");
        TableColumn<Train, String> arrive_time = new TableColumn<Train, String>("到达时间");
        TableColumn<Train, String> lishi = new TableColumn<Train, String>("历时");
        TableColumn<Train, String> col7 = new TableColumn<Train, String>("商务/特等");
        TableColumn<Train, String> col8 = new TableColumn<Train, String>("一等");
        TableColumn<Train, String> col9 = new TableColumn<Train, String>("二等");
        TableColumn<Train, String> col10 = new TableColumn<Train, String>("高级软卧");
        TableColumn<Train, String> col11 = new TableColumn<Train, String>("软卧");
        TableColumn<Train, String> col12 = new TableColumn<Train, String>("硬卧");
        TableColumn<Train, String> col13 = new TableColumn<Train, String>("软座");
        TableColumn<Train, String> col14 = new TableColumn<Train, String>("硬座");
        TableColumn<Train, String> col15 = new TableColumn<Train, String>("无座");
        TableColumn<Train, String> start_station_telecode = new TableColumn<Train, String>("start_station_telecode");
        TableColumn<Train, String> end_station_telecode = new TableColumn<Train, String>("end_station_telecode");
        TableColumn<Train, String> canWebBuy = new TableColumn<Train, String>("WEB");
        TableColumn<Train, String> buttonTextInfo = new TableColumn<Train, String>("状态");
        TableColumn<Train, String> seat_types = new TableColumn<Train, String>("座位类型");

        station_train_code.setCellValueFactory(new PropertyValueFactory<Train, String>("station_train_code"));
        station_train_code.getStyleClass().add("text-bold");
        from_station_telecode.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Train, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Train, String> param) {
                Train train = param.getValue();
                String name = map_code.get(train.getFrom_station_telecode()).getName();
                return new SimpleObjectProperty<String>(name);
            }
        });
        from_station_telecode.getStyleClass().add("text-green");
        to_station_telecode.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Train, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Train, String> param) {
                Train train = param.getValue();
                String name = map_code.get(train.getTo_station_telecode()).getName();
                return new SimpleObjectProperty<String>(name);
            }
        });
        to_station_telecode.getStyleClass().add("text-red");
        start_to_end.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Train, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Train, String> param) {
                Train train = param.getValue();
                String name = map_code.get(train.getStart_station_telecode()).getName() + "--" + map_code.get(train.getEnd_station_telecode()).getName();
                return new SimpleObjectProperty<String>(name);
            }
        });
        start_to_end.getStyleClass().add("text-red");


//        "train_no": "D2287",/*车次*/
//        "start_station_name": "上海虹桥",/*车次始发站*/
//        "end_station_name": "深圳北",/*车次终点站*/
//        "from_station_name": "上海虹桥",/*出发站*/
//        "to_station_name": "温州南",/*到达站*/
//        "start_time": "06:25",/*出发时间*/
//        "arrive_time": "10:53",/*到达时间*/
//        "train_class_name": "动车",/*车次类型*/
//        "day_difference": "0",/*历时天数*/
//        "lishi": "04:28",/*总历时时间*/
//        "gr_num": "--",/*高级软卧：-- 说明无该席位*/
//        "qt_num": "--",/*其他*/
//        "rw_num": "--",/*软卧*/
//        "rz_num": "--",/*软座*/
//        "tz_num": "--",/*特等座*/
//        "wz_num": "无",/*无座*/
//        "yw_num": "--",/*硬卧*/
//        "yz_num": "--",/*硬座*/
//        "ze_num": "无",/*二等座*/
//        "zy_num": "无",/*一等座*/
//        "swz_num": "--"/*商务座*/

        start_time.setCellValueFactory(new PropertyValueFactory<Train, String>("start_time"));
        arrive_time.setCellValueFactory(new PropertyValueFactory<Train, String>("arrive_time"));
        lishi.setCellValueFactory(new PropertyValueFactory<Train, String>("lishi"));
        col7.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Train, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Train, String> param) {
                Train train = param.getValue();
                String name = train.getSwz_num() + "/" + train.getTz_num();
                return new SimpleObjectProperty<String>(name);
            }
        });
        col8.setCellValueFactory(new PropertyValueFactory<Train, String>("zy_num"));
        col9.setCellValueFactory(new PropertyValueFactory<Train, String>("ze_num"));
        col10.setCellValueFactory(new PropertyValueFactory<Train, String>("gr_num"));
        col11.setCellValueFactory(new PropertyValueFactory<Train, String>("rw_num"));
        col12.setCellValueFactory(new PropertyValueFactory<Train, String>("yw_num"));
        col13.setCellValueFactory(new PropertyValueFactory<Train, String>("rz_num"));
        col14.setCellValueFactory(new PropertyValueFactory<Train, String>("yz_num"));
        col15.setCellValueFactory(new PropertyValueFactory<Train, String>("wz_num"));
        start_station_telecode.setCellValueFactory(new PropertyValueFactory<Train, String>("start_station_telecode"));
        end_station_telecode.setCellValueFactory(new PropertyValueFactory<Train, String>("end_station_telecode"));
        canWebBuy.setCellValueFactory(new PropertyValueFactory<Train, String>("canWebBuy"));
        seat_types.setCellValueFactory(new PropertyValueFactory<Train, String>("seat_types"));

        buttonTextInfo.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Train, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Train, String> param) {
                Train train = param.getValue();
                if (org.springframework.util.StringUtils.isEmpty(train.getSecretStr())) {
                    return new SimpleObjectProperty<String>("不可用");
                }
                return new SimpleObjectProperty<String>(train.getButtonTextInfo());
            }
        });

        train_table.getColumns().addAll(station_train_code, start_to_end, from_station_telecode, to_station_telecode, start_time, arrive_time, lishi, col7, col8, col9, col10, col11, col12, col13, col14, col15,
                start_station_telecode, end_station_telecode, canWebBuy, seat_types, buttonTextInfo);
        train_table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void initOrderTable() {
        TableColumn<OrderDTO, String> sequence_no = new TableColumn<OrderDTO, String>("订单号");
        TableColumn<OrderDTO, String> order_date = new TableColumn<OrderDTO, String>("时间");
        TableColumn<OrderDTO, String> ticket_totalnum = new TableColumn<OrderDTO, String>("数量");
        TableColumn<OrderDTO, String> ticket_price_all = new TableColumn<OrderDTO, String>("金额");
        TableColumn<OrderDTO, String> stationTrainDTO = new TableColumn<OrderDTO, String>("票务");

        sequence_no.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("sequence_no"));
        sequence_no.getStyleClass().add("text-bold");
        order_date.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("order_date"));
        order_date.getStyleClass().add("text-green");
        ticket_totalnum.setCellValueFactory(new PropertyValueFactory<OrderDTO, String>("ticket_totalnum"));
        ticket_totalnum.getStyleClass().add("text-red");
        ticket_price_all.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<OrderDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderDTO, String> param) {
                OrderDTO order = param.getValue();
                String name = String.valueOf(order.getTicket_price_all() / 100);
                return new SimpleObjectProperty<String>(name);
            }
        });
        stationTrainDTO.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<OrderDTO, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderDTO, String> param) {
                OrderDTO order = param.getValue();
                String name = JSON.toJSONString(order.getTickets().get(0).getStationTrainDTO());
                return new SimpleObjectProperty<String>(name);
            }
        });


        order_table.getColumns().addAll(sequence_no, order_date, ticket_totalnum, ticket_price_all, stationTrainDTO);
    }


    private void initPassengerTable() {
        TableColumn<Passenger, String> passenger_name = new TableColumn<Passenger, String>("姓名");
        TableColumn<Passenger, String> born_date = new TableColumn<Passenger, String>("添加日期");
        TableColumn<Passenger, String> passenger_id_type_name = new TableColumn<Passenger, String>("身份证");
        TableColumn<Passenger, String> passenger_id_no = new TableColumn<Passenger, String>("身份证编号");
        TableColumn<Passenger, String> passenger_type_name = new TableColumn<Passenger, String>("类型");
        TableColumn<Passenger, String> first_letter = new TableColumn<Passenger, String>("首字母");
        TableColumn<Passenger, String> country_code = new TableColumn<Passenger, String>("国籍");

        passenger_name.setCellValueFactory(new PropertyValueFactory<Passenger, String>("passenger_name"));
        passenger_name.getStyleClass().add("text-bold");
        born_date.setCellValueFactory(new PropertyValueFactory<Passenger, String>("born_date"));
        born_date.getStyleClass().add("text-green");
        passenger_id_type_name.setCellValueFactory(new PropertyValueFactory<Passenger, String>("passenger_id_type_name"));
        passenger_id_type_name.getStyleClass().add("text-red");
        passenger_id_no.setCellValueFactory(new PropertyValueFactory<Passenger, String>("passenger_id_no"));
        passenger_id_no.getStyleClass().add("text-red");

        passenger_type_name.setCellValueFactory(new PropertyValueFactory<Passenger, String>("passenger_type_name"));
        passenger_type_name.getStyleClass().add("text-red");


        passenger_table.getColumns().addAll(passenger_name, born_date, passenger_id_type_name, passenger_id_no, passenger_type_name, first_letter, country_code);
    }

    private void initOrderTab() {
        Date now = DateTime.now().toDate();
        Date end = DateTime.now().plusDays(20).toDate();
        Call<String> call = trainService.queryMyOrder(QueryType.乘车日期.getVal(), StringUtils.formatDate(now), StringUtils.formatDate(end), OrderScopeType.所有.getVal(), 10, 0, "G", null);

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
                    normal_passengers = response.body().getData().getNormal_passengers();
                    ObservableList<Passenger> passengers = FXCollections.observableArrayList(normal_passengers);
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

        Retrofit retrofit = RetrofitConfig.getRetrofit(String.format("http://%s/", pingService.getCurrentHost()));
        Call<FetchTrain> call = retrofit.create(TrainService.class).queryX(date_selected.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
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
                    if (response.body().getData() == null) {
                        logger.info("未获取到车次");
                        return;
                    }
                    List<String> list = response.body().getData().getResult();
                    if (list == null) {
                        logger.info("未获取到车次");
                        return;
                    }
                    logger.info("获取到车次,共{}个", list.size());
                    for (int i = 0; i < list.size(); i++) {
                        String[] fields = list.get(i).split("\\|");
                        if (fields.length < 37) {
                            logger.info("获取车次信息长度不匹配 {} 不大于 37 ", fields.length);
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
                        train.gg_num = !org.springframework.util.StringUtils.isEmpty(fields[20]) ? fields[20] : "--";
                        train.gr_num = !org.springframework.util.StringUtils.isEmpty(fields[21]) ? fields[21] : "--";
                        train.qt_num = !org.springframework.util.StringUtils.isEmpty(fields[22]) ? fields[22] : "--";
                        train.rw_num = !org.springframework.util.StringUtils.isEmpty(fields[23]) ? fields[23] : "--";
                        train.rz_num = !org.springframework.util.StringUtils.isEmpty(fields[24]) ? fields[24] : "--";
                        train.tz_num = !org.springframework.util.StringUtils.isEmpty(fields[25]) ? fields[25] : "--";
                        train.wz_num = !org.springframework.util.StringUtils.isEmpty(fields[26]) ? fields[26] : "--";
                        train.yb_num = !org.springframework.util.StringUtils.isEmpty(fields[27]) ? fields[27] : "--";
                        train.yw_num = !org.springframework.util.StringUtils.isEmpty(fields[28]) ? fields[28] : "--";
                        train.yz_num = !org.springframework.util.StringUtils.isEmpty(fields[29]) ? fields[29] : "--";
                        train.ze_num = !org.springframework.util.StringUtils.isEmpty(fields[30]) ? fields[30] : "--";
                        train.zy_num = !org.springframework.util.StringUtils.isEmpty(fields[31]) ? fields[31] : "--";
                        train.swz_num = !org.springframework.util.StringUtils.isEmpty(fields[32]) ? fields[32] : "--";
                        train.srrb_num = !org.springframework.util.StringUtils.isEmpty(fields[33]) ? fields[33] : "--";
                        train.yp_ex = fields[34];
                        train.seat_types = fields[35];
                        train.exchange_train_flag = fields[36];
                        train.houbu_train_flag = fields[37];
                        if (fields.length > 37) {
                            train.houbu_seat_limit = fields[37];
                        }
                        if (filterTrain(train)) {
                            trains.add(train);
                        }
                    }
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        train_table.setItems(FXCollections.observableArrayList(filterTrain(trains)));
                        GUIUtils.autoFitTable(train_table);
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
        pingService.initTreeMap();
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

    private boolean filterTrain(Train train) {
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
        String str = train.getStation_train_code();
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
        return stringBuilder.toString().contains(str2);
    }


    @FXML
    public void onSubmitOrderRequest() throws IOException {
        if (train_table.getSelectionModel().getSelectedCells() == null || train_table.getSelectionModel().getSelectedCells().size() == 0) {
            AlertUtils.showErrorAlert("请选择车次");
            return;
        }
        int index = train_table.getSelectionModel().getSelectedCells().get(0).getRow();
        Train train = trains.get(index);
        secretStr = train.getSecretStr();
        if (org.springframework.util.StringUtils.isEmpty(secretStr)) {
            AlertUtils.showErrorAlert("请选择车次");
            return;
        }

        Response<ResponseBody> getPassCodeNew_result = trainService.getPassCodeNew(Constants.PASSENGER, Constants.RANDP).execute();
        File file = new File(CAPTURE_NAME);
        InputStream in = null;
        FileOutputStream out = null;

        try {
            in = getPassCodeNew_result.body().byteStream();
            out = new FileOutputStream(file);
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
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

        /**
         * 验证登录
         */
        CheckUser checkUser_result = trainService.checkUser("").execute().body();
        if (!checkUser_result.getData().isFlag()) {
            AlertUtils.showErrorAlert("未登录");
            return;
        }


        /**
         * 预提交订单
         */
        Response<BaseDto> submitOrderRequest_result = trainService.submitOrderRequest(secretStr,
                date_selected.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                TourFlag.单程.getVal(),
                PurposeCodes.成人.getVal(),
                station_from_selected.getName(),
                station_to_selected.getName(),
                null).execute();

        if (!submitOrderRequest_result.body().isStatus()) {
            AlertUtils.showErrorAlert(submitOrderRequest_result.body().getMessages().toString());
            return;
        }

        /**
         * 模拟跳转页面InitDc
         */
        Response<String> initDc_result = trainService.initDc().execute();

        Document document = Jsoup.parse(initDc_result.body(), "UTF-8");

        /**
         * 获取提交信息
         */
        int position = initDc_result.body().indexOf("globalRepeatSubmitToken");
        int position_start = initDc_result.body().indexOf("'", position) + 1;
        int position_end = initDc_result.body().indexOf("'", position_start);
        String token = String.valueOf(initDc_result.body().subSequence(position_start, position_end));
        String passengerTicketStr = joinPassanger(normal_passengers.get(0));
        String oldPassengerStr = joinoldPassanger(normal_passengers.get(0)) + "_";


        CheckOrderInfoDto checkOrderInfo_result = trainService.checkOrderInfo(Constants.CANCEL_FLAG, Constants.BED_LEVEL_ORDER_NUM,
                passengerTicketStr, oldPassengerStr, TourFlag.单程.getVal(),
                "", "1", "", token).execute().body();

        logger.info("{}", JSON.toJSONString(checkOrderInfo_result));

        if (!checkOrderInfo_result.getData().isSubmitStatus()) {
            AlertUtils.showErrorAlert(checkOrderInfo_result.getMessages().toString());
            return;
        }
        String token_ypInfoDetail = StringUtils.match(initDc_result.body(), "'ypInfoDetail'");
        String train_location = StringUtils.match2(initDc_result.body(), "'train_location'");
        String purpose_codes = StringUtils.match(initDc_result.body(), "'purpose_codes'");
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = date_selected;
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        Date date = Date.from(zdt.toInstant());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy 00:00:00 'GMT'Z (中国标准时间)", Locale.ENGLISH);

        Response<String> getQueueCount_result = trainService.getQueueCount(
                dateFormat.format(date),
                train.getTrain_no(),
                train.getStation_train_code(),
                SeatType.二等座.getVal(),
                train.getFrom_station_telecode(),
                train.getTo_station_telecode(),
                token_ypInfoDetail,
                purpose_codes,
                train_location,
                checkOrderInfo_result.getData().getIsCheckOrderInfo() == null ? "" : checkOrderInfo_result.getData().getIsCheckOrderInfo()
        ).execute();
        logger.info("{}", getQueueCount_result);

    }

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }

    public void onSwitchStation(ActionEvent actionEvent) {
        Station temp = station_from_selected;
        station_from_selected = station_to_selected;
        station_to_selected = temp;
        station_from.setValue(station_from_selected);
        station_to.setValue(station_to_selected);
    }
}
