package com.example.networkprojectgui.Implementations;

import com.example.networkprojectgui.Interfaces.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkInfoImpl implements NetworkInfo {
    @Override
    public HashMap<String, String> getInfo() {
        HashMap<String, String> result = new HashMap<String, String>();

        // netsh wlan show interfaces
        ProcessBuilder processBuilder = new ProcessBuilder("netsh", "wlan", "show", "interfaces");
        processBuilder.redirectErrorStream(true); // Merge error and output streams

        // Define regex patterns for SSID, Signal, Channel and Band
        Pattern ssidPattern = Pattern.compile("\\s+SSID\\s+:\\s(.+)");
        Pattern macPattern = Pattern.compile("Physical address\\s+:\\s(.+)");
        Pattern authenticationPattern = Pattern.compile("Authentication\\s+:\\s(.+)");
        Pattern signalPattern = Pattern.compile("Signal\\s+:\\s(\\d+)%");
        Pattern channelPattern = Pattern.compile("Channel\\s+:\\s(\\d+)");
        Pattern bandPattern = Pattern.compile("Band\\s+:\\s(.+)");

        try {
            // Start the process
            Process process = processBuilder.start();

            // Read the output from the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Variables to store the parsed details
            String SSID = null;
            String MACAddress = null;
            String Authentication = null;
            String Signal = null;
            String Channel = null;
            String Band = null;

            while ((line = reader.readLine()) != null) {
                // Check for SSID
                Matcher ssidMatcher = ssidPattern.matcher(line);
                if (ssidMatcher.find()) {
                    SSID = ssidMatcher.group(1);
                }

                // Check for BSSID
                Matcher macMatcher = macPattern.matcher(line);
                if (macMatcher.find()) {
                    MACAddress = macMatcher.group(1);
                }

                // Check for Authentication
                Matcher authenticationMatcher = authenticationPattern.matcher(line);
                if (authenticationMatcher.find()) {
                    Authentication = authenticationMatcher.group(1);
                }

                // Check for Signal strength
                Matcher signalMatcher = signalPattern.matcher(line);
                if (signalMatcher.find()) {
                    Signal = signalMatcher.group(1);
                }

                // Check for Channel
                Matcher channelMatcher = channelPattern.matcher(line);
                if (channelMatcher.find()) {
                    Channel = channelMatcher.group(1);
                }

                // Check for Band
                Matcher bandMatcher = bandPattern.matcher(line);
                if (bandMatcher.find()) {
                    Band = bandMatcher.group(1);
                }

                // Print the Wi-Fi details
                if (SSID != null && Authentication != null && Signal != null && Channel != null && Band != null) {
                    //System.out.println("SSID: " + SSID + ", MAC Address: " + MACAddress + ", Authentication: " + Authentication +
                            //", Signal: " + Signal + "%, Channel: " + Channel + ", Band: " + Band);
                    result.put("SSID", SSID);
                    result.put("Authentication", Authentication);
                    result.put("Signal", Signal);
                    result.put("MACAddress", MACAddress);
                    result.put("Channel", Channel);
                    result.put("Band", Band);
                    // Reset variables for the next network
                    SSID = null;
                    Signal = null;
                    Channel = null;
                }
            }

            // Wait for the process to terminate
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
}