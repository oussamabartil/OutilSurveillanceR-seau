package com.example.networkprojectgui;

import com.example.networkprojectgui.utils.PoppinsFontFactory;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class Application extends javafx.application.Application {

    // Enum to represent different views
    private enum View {
        SELECTED_NETWORK(1, "#view-1", "Selected Network"),
        MEASUREMENTS(2, "#view-2", "Network Speed"),
        HEALTH(3, "#view-3", "Network Health"),
        DEVICES(4, "#view-4", "Network Devices"),
        SNIFFING(5, "#view-5", "Packet Sniffer");

        private final int id;
        private final String fxmlId;
        private final String title;

        View(int id, String fxmlId, String title) {
            this.id = id;
            this.fxmlId = fxmlId;
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    private Map<View, VBox> viewPanes;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        initializeScene(primaryStage);
        setupViews();
        setupFonts();
        setupNavigationButtons();

        primaryStage.show();
    }

    private void initializeScene(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1000, 800);
        scene.getStylesheets().add("styles.css");
        scene.setFill(Color.TRANSPARENT);

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Network Project");
        primaryStage.setScene(scene);
    }

    private void setupViews() {
        viewPanes = new EnumMap<>(View.class);
        for (View view : View.values()) {
            if (view.fxmlId != null) {
                viewPanes.put(view, (VBox) scene.lookup(view.fxmlId));
            }
        }
    }

    private void setupFonts() {
        // Set view label font
        Label viewLabel = (Label) scene.lookup("#viewLabel");
        viewLabel.setFont(PoppinsFontFactory.createFont("semibold", 28));

        // Setup fonts for each view
        setupView1Fonts();
        setupView2Fonts();
        setupView3Fonts();
    }

    private void setupView1Fonts() {
        VBox view1 = viewPanes.get(View.SELECTED_NETWORK);
        applyFontToNodes(view1, ".view-1-wifi-name", "medium", 24);
        applyFontToNodes(view1, ".view-1-small-label", "regular", 14);
        applyFontToNodes(view1, ".view-1-small-label-value-gray", "medium", 14);
        applyFontToNodes(view1, ".view-1-small-label-value-colored", "medium", 16);
    }

    private void setupView2Fonts() {
        VBox view2 = viewPanes.get(View.MEASUREMENTS);
        applyFontToNodes(view2, ".view-2-big-label", "medium", 44);
        applyFontToNodes(view2, ".view-2-medium-label", "regular", 18);
    }

    private void setupView3Fonts() {
        VBox view3 = viewPanes.get(View.HEALTH);
        applyFontToNodes(view3, ".view-3-small-label-value-gray", "medium", 13);
        applyFontToNodes(view3, ".view-3-medium-label", "regular", 15);
        applyFontToNodes(view3, ".view-3-small-label-value-colored", "medium", 16);
    }

    private void applyFontToNodes(VBox container, String selector, String fontWeight, int fontSize) {
        if (container != null) {
            container.lookupAll(selector).forEach(node ->
                    ((Label) node).setFont(PoppinsFontFactory.createFont(fontWeight, fontSize))
            );
        }
    }

    private void setupNavigationButtons() {
        Platform.runLater(() -> {
            Map<View, Button> buttons = new EnumMap<>(View.class);
            buttons.put(View.SELECTED_NETWORK, (Button) scene.lookup("#selectedNetworkButton"));
            buttons.put(View.MEASUREMENTS, (Button) scene.lookup("#networkMeasurementButton"));
            buttons.put(View.HEALTH, (Button) scene.lookup("#networkHealthButton"));
            buttons.put(View.DEVICES, (Button) scene.lookup("#networkDevicesButton"));
            buttons.put(View.SNIFFING, (Button) scene.lookup("#packetSnifferButton"));

            // Set initial state
            selectView(View.SELECTED_NETWORK, buttons);

            // Setup button click handlers
            buttons.forEach((view, button) ->
                    button.setOnAction(e -> selectView(view, buttons))
            );
        });
    }

    private void selectView(View selectedView, Map<View, Button> buttons) {
        // Update button styles
        buttons.forEach((view, button) -> {
            if (view == selectedView) {
                if (!button.getStyleClass().contains("button-is-selected")) {
                    button.getStyleClass().add("button-is-selected");
                }
            } else {
                button.getStyleClass().remove("button-is-selected");
            }
        });

        // Update view visibility and label
        Label viewLabel = (Label) scene.lookup("#viewLabel");
        viewPanes.forEach((view, pane) -> {
            if (pane != null) {
                boolean isSelected = (view == selectedView);
                pane.setVisible(isSelected);
                pane.setManaged(isSelected);

                // Update the label text based on the selected view
                if (isSelected) {
                    switch (view) {
                        case SELECTED_NETWORK:
                            viewLabel.setText("Selected Network");
                            break;
                        case MEASUREMENTS:
                            viewLabel.setText("Network Speed");
                            break;
                        case HEALTH:
                            viewLabel.setText("Network Health");
                            break;
                        case DEVICES:
                            viewLabel.setText("Network Devices");
                            break;
                        case SNIFFING:
                            viewLabel.setText("Packet Sniffer");
                            break;
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}