package com.example.networkprojectgui.Implementations;

import com.example.networkprojectgui.DataTypes.DeviceInfo;
import com.example.networkprojectgui.Interfaces.DeviceFinder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceFinderImpl implements DeviceFinder {
    private ObservableList<DeviceInfo> devices = FXCollections.observableArrayList();

    @Override
    public void findDevices() {
        try {
            devices.clear(); // Clear existing devices
            DeviceInfo.resetCounter();

            ProcessBuilder arpProcess = new ProcessBuilder("arp", "-a");
            arpProcess.redirectErrorStream(true);
            Process process = arpProcess.start();

            Pattern devicePattern = Pattern.compile("^\\s*(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s+([-0-9a-f]{17}|\\S+)\\s+(\\S+)\\s*$",
                    Pattern.CASE_INSENSITIVE);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Matcher matcher = devicePattern.matcher(line);
                    if (matcher.find()) {
                        String ip = matcher.group(1);
                        String mac = matcher.group(2);
                        String type = matcher.group(3);

                        if (!mac.contains("invalid") && !mac.equals("-")) {
                            // Create new DeviceInfo object
                            DeviceInfo device = new DeviceInfo(ip, "Unknown", mac, type);

//                            // Try to resolve hostname
//                            try {
//                                String hostname = InetAddress.getByName(ip).getHostName();
//                                if (!hostname.equals(ip)) {
//                                    device.setName(hostname);
//                                }
//                            } catch (Exception e) {
//                                // If hostname resolution fails, leave as "Unknown"
//                            }

                            devices.add(device);
                        }
                    }
                }
            }

            // Print results (you can remove this if you don't need console output)
//            System.out.println("\nDevices found on network:");
//            System.out.println("----------------------------------------------------------");
//            System.out.printf("%-20s %-20s %-20s %-10s%n", "IP Address", "Device Name", "MAC Address", "Type");
//            System.out.println("----------------------------------------------------------");
//
//            for (DeviceInfo device : devices) {
//                System.out.printf("%-20s %-20s %-20s %-10s%n",
//                        device.getIp(),
//                        device.getName(),
//                        device.getMac(),
//                        device.getType());
//            }
//
//            System.out.println("----------------------------------------------------------");

        } catch (Exception e) {
            System.err.println("Error finding devices: " + e.getMessage());
        }
    }

    // Add this method to get the devices list
    public ObservableList<DeviceInfo> getDevices() {
        return devices;
    }
}