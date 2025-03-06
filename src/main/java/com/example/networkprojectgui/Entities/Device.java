package com.example.networkprojectgui.Entities;

public class Device {
    public String name;
    public String type;
    public String ipAddress;
    public String macAddress;

    public Device(String name, String type, String ipAddress, String macAddress) {
        this.name = name;
        this.type = type;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public String getMacAddress() {
        return macAddress;
    }
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Type: " + type + ", IP: " + ipAddress + ", Mac: " + macAddress;
    }
}
