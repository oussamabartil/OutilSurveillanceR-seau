package com.example.networkprojectgui.utils;

import com.example.networkprojectgui.Application;
import javafx.scene.text.Font;

import java.util.Objects;

public class PoppinsFontFactory {
    // Load Fonts
    final String PoppinsBoldPath = Application.class.getResource("/fonts/Poppins-Bold.ttf").toExternalForm();
    final String PoppinsSemiBoldPath = Application.class.getResource("/fonts/Poppins-SemiBold.ttf").toExternalForm();
    final String PoppinsMediumPath = Application.class.getResource("/fonts/Poppins-Medium.ttf").toExternalForm();
    final String PoppinsRegularPath = Application.class.getResource("/fonts/Poppins-Regular.ttf").toExternalForm();
    final String PoppinsLightPath = Application.class.getResource("/fonts/Poppins-Light.ttf").toExternalForm();

    public static Font createFont(String type, int size) {
        if(Objects.equals(type, "bold")) return Font.loadFont(Application.class.getResource("/fonts/Poppins-Bold.ttf").toExternalForm(), size);
        else if(Objects.equals(type, "semibold")) return Font.loadFont(Application.class.getResource("/fonts/Poppins-SemiBold.ttf").toExternalForm(), size);
        else if(Objects.equals(type, "medium")) return Font.loadFont(Application.class.getResource("/fonts/Poppins-Medium.ttf").toExternalForm(), size);
        else if(Objects.equals(type, "regular")) return Font.loadFont(Application.class.getResource("/fonts/Poppins-Regular.ttf").toExternalForm(), size);
        else return Font.loadFont(Application.class.getResource("/fonts/Poppins-Bold.ttf").toExternalForm(), size);
    }

}