package com.example.networkprojectgui.DataTypes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class DeviceInfo {
    private static int counter = 0;
    private final IntegerProperty id;
    private final StringProperty ip;
//    private final StringProperty name;
    private final StringProperty mac;
    private final StringProperty type;

    public static void resetCounter() {
        counter = 0;
    }

    public DeviceInfo(String ip, String name, String mac, String type) {
        this.id = new SimpleIntegerProperty(++counter);
        this.ip = new SimpleStringProperty(ip);
//        this.name = new SimpleStringProperty(name);
        this.mac = new SimpleStringProperty(mac);
        this.type = new SimpleStringProperty(type);
    }

    // ID
    public IntegerProperty idProperty() {
        return id;
    }

    public Integer getId() {
        return id.get();
    }

    // IP Address
    public StringProperty ipProperty() {
        return ip;
    }

    public String getIp() {
        return ip.get();
    }

    public void setIp(String value) {
        ip.set(value);
    }

    // Name
//    public StringProperty nameProperty() {
//        return name;
//    }
//
//    public String getName() {
//        return name.get();
//    }
//
//    public void setName(String value) {
//        name.set(value);
//    }

    // MAC Address
    public StringProperty macProperty() {
        return mac;
    }

    public String getMac() {
        return mac.get();
    }

    public void setMac(String value) {
        mac.set(value);
    }

    // Type
    public StringProperty typeProperty() {
        return type;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String value) {
        type.set(value);
    }
}