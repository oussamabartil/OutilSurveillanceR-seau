package com.example.networkprojectgui.DataTypes;

public class SpeedData {
    private double downloadSpeed; // mbps
    private double uploadSpeed; // mbps

    public SpeedData(double downloadSpeed, double uploadSpeed) {
        this.downloadSpeed = downloadSpeed;
        this.uploadSpeed = uploadSpeed;
    }

    public double getDownloadSpeed() {
        return downloadSpeed;
    }
    public void setDownloadSpeed(double downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }
    public double getUploadSpeed() {
        return uploadSpeed;
    }
    public void setUploadSpeed(double uploadSpeed) {
        this.uploadSpeed = uploadSpeed;
    }

    @Override
    public String toString() {
        return "Download: " + downloadSpeed + " Mbps, Upload: " + uploadSpeed + " Mbps";
    }
}
