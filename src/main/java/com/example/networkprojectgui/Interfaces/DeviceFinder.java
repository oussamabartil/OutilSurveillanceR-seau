package com.example.networkprojectgui.Interfaces;

import com.example.networkprojectgui.DataTypes.DeviceInfo;
import javafx.collections.ObservableList;

public interface DeviceFinder {
    void findDevices();
    ObservableList<DeviceInfo> getDevices();
}
