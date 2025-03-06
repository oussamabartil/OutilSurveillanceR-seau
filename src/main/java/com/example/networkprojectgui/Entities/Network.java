package com.example.networkprojectgui.Entities;


import com.example.networkprojectgui.DataTypes.SpeedData;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private String SSID;
    private int channels;
    private double signal;
    private String authentication;
    private SpeedData speed;
    private List<Device> devices;

    public Network(String SSID, int channels, double signal, String authentication) {
        this.SSID = SSID;
        this.channels = channels;
        this.signal = signal;
        this.authentication = authentication;
        this.speed = new SpeedData(0, 0);
        this.devices = new ArrayList<>();
    }

    public String getSSID() {
        return SSID;
    }
    public int getChannels() {
        return channels;
    }
    public double getSignal() {
        return signal;
    }
    public String getAuthentication() {
        return authentication;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }
    public void setChannels(int channels) {
        this.channels = channels;
    }
    public void setSignal(double signal) {
        this.signal = signal;
    }
    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }
    public SpeedData getSpeed() { return this.speed; }
    public void setSpeed(SpeedData speed) { this.speed = speed; }
}
