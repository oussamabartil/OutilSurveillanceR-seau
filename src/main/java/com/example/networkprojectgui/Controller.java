package com.example.networkprojectgui;

import com.example.networkprojectgui.DataTypes.DeviceInfo;
import com.example.networkprojectgui.DataTypes.PacketInfo;
import com.example.networkprojectgui.Implementations.*;
import com.example.networkprojectgui.Interfaces.DeviceFinder;
import com.example.networkprojectgui.Interfaces.NetworkHealth;
import com.example.networkprojectgui.Interfaces.NetworkInfo;
import com.example.networkprojectgui.Interfaces.NetworkSpeed;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.jnetpcap.PcapIf;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller implements Initializable {
    private double x = 0, y = 0;
    private NetworkInfo networkInfo;
    private NetworkSpeed networkSpeed;
    private NetworkHealth networkHealth;
    private DeviceFinder deviceFinder;
    private ExecutorService executorService;
    private PacketSnifferImpl packetSniffer;
    private Thread snifferThread;

    @FXML
    private FlowPane topBar;

    @FXML
    private Label view1WifiName;

    @FXML
    private Label view1WifiAuth;

    @FXML
    private Label view1WifiMac;

    @FXML
    private Label view1WifiSignal;

    @FXML
    private Label view1WifiBand;

    @FXML
    private Label view1WifiChannel;

    @FXML
    private Label view2DownSpeed;

    @FXML
    private Label view2UpSpeed;

    @FXML
    private Button view2StartButton;

    @FXML
    private Button view3StartButton;

    @FXML
    private Label view3Latency;

    @FXML
    private Label view3PacketLoss;

    @FXML
    private Label view3Jitter;

    @FXML
    private Label view3HealthStatus;

    @FXML
    private TableView<DeviceInfo> deviceTable;

    @FXML
    private TableColumn<DeviceInfo, String> view4TableNumber;

    @FXML
    private TableColumn<DeviceInfo, String> view4TableIPAddress;

//    @FXML
//    private TableColumn<DeviceInfo, String> view4TableName;

    @FXML
    private TableColumn<DeviceInfo, String> view4TableMacAddress;

    @FXML
    private TableColumn<DeviceInfo, String> view4TableType;

    @FXML
    private Button view4StartButton;

    @FXML
    private ComboBox<PcapIf> view5ComboBox;
    @FXML
    private Button view5StartButton;
    @FXML
    private TableView<PacketInfo> view5Table;
    @FXML
    private TableColumn<PacketInfo, Integer> view5TableN;
    @FXML
    private TableColumn<PacketInfo, String> view5TableTime;
    @FXML
    private TableColumn<PacketInfo, String> view5TableSource;
    @FXML
    private TableColumn<PacketInfo, String> view5TableDestination;
    @FXML
    private TableColumn<PacketInfo, String> view5TableProtocol;
    @FXML
    private TableColumn<PacketInfo, String> view5TableInfo;

    private ObservableList<DeviceInfo> devices = FXCollections.observableArrayList();

    @FXML
    void minimize(MouseEvent event) {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void close(MouseEvent event) {
        if (executorService != null) {
            executorService.shutdown();
        }
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize ExecutorService
        executorService = Executors.newFixedThreadPool(2);

        // Window Setup
        topBar.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });
        topBar.setOnMouseDragged(mouseEvent -> {
            Stage source = (Stage) ((FlowPane) mouseEvent.getSource()).getScene().getWindow();
            source.setX(mouseEvent.getScreenX() - x);
            source.setY(mouseEvent.getScreenY() - y);
        });

        // Load Network Infos
        loadNetworkInfos();

        // Setting Up View 4
        setupDeviceTable();

        // Setting ip View 5
        setupPacketSnifferView();
    }

    public void loadNetworkInfos() {
        networkInfo = new NetworkInfoImpl();

        executorService.submit(() -> {
            HashMap<String, String> networkInfoResult = networkInfo.getInfo();

            // Update UI on JavaFX Application Thread
            Platform.runLater(() -> {
                view1WifiName.setText(networkInfoResult.get("SSID"));
                view1WifiAuth.setText(networkInfoResult.get("Authentication"));
                view1WifiSignal.setText(networkInfoResult.get("Signal"));
                view1WifiMac.setText(networkInfoResult.get("MACAddress"));
                view1WifiBand.setText(networkInfoResult.get("Band"));
                view1WifiChannel.setText(networkInfoResult.get("Channel"));
            });
        });
    }

    public void measureSpeed() {
        view2StartButton.setDisable(true);
        view2StartButton.setText("Measuring...");

        view2DownSpeed.setText("0");
        view2UpSpeed.setText("0");

        executorService.submit(() -> {
            networkSpeed = new NetworkSpeedImpl();

            // Measure download speed
            double downSpeed = networkSpeed.measureDowndloadSpeed();
            Platform.runLater(() -> view2DownSpeed.setText(Double.toString(downSpeed)));

            // Measure upload speed
            double upSpeed = networkSpeed.measureUploadSpeed();
            Platform.runLater(() -> {
                view2UpSpeed.setText(Double.toString(upSpeed));
                view2StartButton.setText("Restart");
                view2StartButton.setDisable(false);
            });
        });
    }

    public void measureHealth() {
        view3StartButton.setDisable(true);
        view3StartButton.setText("Measuring...");

        executorService.submit(() -> {
            networkHealth = new NetworkHealthImpl();
            HashMap<String, String> result = networkHealth.diagnose();

            Platform.runLater(() -> {
                view3PacketLoss.setText(result.get("PacketLoss"));
                view3Latency.setText(result.get("Latency"));
                view3Jitter.setText(result.get("Jitter"));
                view3HealthStatus.setText(result.get("HealthStatus"));

                view3StartButton.setDisable(false);
                view3StartButton.setText("Diagnose");
            });

        });

    }

    public void setupDeviceTable() {
        // Set up the columns
        view4TableNumber.setCellValueFactory(new PropertyValueFactory<>("id"));
        view4TableIPAddress.setCellValueFactory(new PropertyValueFactory<>("ip"));
//        view4TableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        view4TableMacAddress.setCellValueFactory(new PropertyValueFactory<>("mac"));
        view4TableType.setCellValueFactory(new PropertyValueFactory<>("type"));

        deviceTable.setItems(devices);
    }

    public void findDevices() {
        deviceFinder = new DeviceFinderImpl();
        view4StartButton.setDisable(true);
        view4StartButton.setText("Finding...");

        executorService.submit(() -> {
            deviceFinder.findDevices();

            // Update the devices list
            Platform.runLater(() -> {
                devices.clear();
                devices.addAll(deviceFinder.getDevices());

                view4StartButton.setDisable(false);
                view4StartButton.setText("Restart");
            });
        });
    }

    private void setupPacketSnifferView() {
        // Initialize packet sniffer
        packetSniffer = new PacketSnifferImpl();

        // Setup ComboBox
        List<PcapIf> devices = packetSniffer.getDevices();
        view5ComboBox.setItems(FXCollections.observableArrayList(devices));
        view5ComboBox.setCellFactory(param -> new ListCell<PcapIf>() {
            @Override
            protected void updateItem(PcapIf device, boolean empty) {
                super.updateItem(device, empty);
                if (empty || device == null) {
                    setText(null);
                } else {
                    String description = device.getDescription();
                    setText(description != null ? description : device.getName());
                }
            }
        });
        view5ComboBox.setButtonCell(view5ComboBox.getCellFactory().call(null));

        // row coloring
        view5Table.setRowFactory(tv -> new TableRow<PacketInfo>() {
            @Override
            protected void updateItem(PacketInfo item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    switch (item.getSecurityStatus()) {
                        case MALICIOUS:
                            setStyle("-fx-background-color: rgba(255, 0, 0, 0.3);"); // Red with transparency
                            break;
                        case SUSPICIOUS:
                            setStyle("-fx-background-color: rgba(255, 255, 0, 0.3);"); // Yellow with transparency
                            break;
                        default:
                            setStyle(""); // Default color
                            break;
                    }
                }
            }
        });

        // tooltip to show security details
        view5Table.setRowFactory(tv -> {
            TableRow<PacketInfo> row = new TableRow<PacketInfo>() {
                private Tooltip tooltip = new Tooltip();

                @Override
                protected void updateItem(PacketInfo item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setTooltip(null);
                        setStyle("");
                    } else {
                        switch (item.getSecurityStatus()) {
                            case MALICIOUS:
                                setStyle("-fx-background-color: rgba(255, 0, 0, 0.3);");
                                tooltip.setText("Malicious packet detected!\nReason: Suspicious activity on known malicious ports");
                                break;
                            case SUSPICIOUS:
                                setStyle("-fx-background-color: rgba(255, 255, 0, 0.3);");
                                tooltip.setText("Suspicious packet!\nReason: Unusual protocol behavior detected");
                                break;
                            default:
                                setStyle("");
                                tooltip.setText("Normal packet");
                                break;
                        }
                        setTooltip(tooltip);
                    }
                }
            };
            return row;
        });

        // Setup Table columns
        view5TableN.setCellValueFactory(new PropertyValueFactory<>("id"));
        view5TableTime.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        view5TableSource.setCellValueFactory(new PropertyValueFactory<>("sourceIP"));
        view5TableDestination.setCellValueFactory(new PropertyValueFactory<>("destIP"));
        view5TableProtocol.setCellValueFactory(new PropertyValueFactory<>("protocol"));
        view5TableInfo.setCellValueFactory(new PropertyValueFactory<>("info"));

        // Bind table to packet sniffer's observable list
        view5Table.setItems(packetSniffer.getPackets());

        // Setup Start button
        view5StartButton.setOnAction(event -> toggleSniffing());
    }

    private void toggleSniffing() {
        if (view5StartButton.getText().equals("Start")) {
            startSniffing();
        } else {
            stopSniffing();
        }
    }

    private void startSniffing() {
        PcapIf selectedDevice = view5ComboBox.getValue();
        if (selectedDevice == null) {
            // Show error message
            return;
        }

        view5StartButton.setText("Stop");
        view5ComboBox.setDisable(true);

        snifferThread = new Thread(() -> {
            packetSniffer.startSniffing(selectedDevice);
        });
        snifferThread.setDaemon(true);
        snifferThread.start();
    }

    private void stopSniffing() {
        if (packetSniffer != null) {
            packetSniffer.stopSniffing();
        }

        view5StartButton.setText("Start");
        view5ComboBox.setDisable(false);
    }
}