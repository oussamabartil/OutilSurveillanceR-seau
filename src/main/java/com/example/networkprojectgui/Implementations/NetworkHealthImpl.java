package com.example.networkprojectgui.Implementations;

import com.example.networkprojectgui.Interfaces.NetworkHealth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkHealthImpl implements NetworkHealth {
    private static final String TARGET = "8.8.8.8"; // Google DNS server
    private static final int PING_COUNT = 10;

    @Override
    public HashMap<String, String> diagnose() {
        HashMap<String, String> result = new HashMap<>();
        try {
            // Run ping command
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ping",
                    "-n", String.valueOf(PING_COUNT),
                    TARGET
            );
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Track metrics
            int received = 0;
            double totalLatency = 0;
            double minLatency = Double.MAX_VALUE;
            double maxLatency = 0;

            // Process output
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                Pattern timePattern = Pattern.compile("time[=<](\\d+)ms");

                while ((line = reader.readLine()) != null) {
                    //System.out.println(line);

                    if (line.contains("Reply from")) {
                        received++;
                        Matcher matcher = timePattern.matcher(line);
                        if (matcher.find()) {
                            double latency = Double.parseDouble(matcher.group(1));
                            totalLatency += latency;
                            minLatency = Math.min(minLatency, latency);
                            maxLatency = Math.max(maxLatency, latency);
                        }
                    }
                }
            }

            // Calculate and display results
            if (received > 0) {
                double packetLoss = ((PING_COUNT - received) * 100.0) / PING_COUNT;
                double avgLatency = totalLatency / received;
                double jitter = maxLatency - minLatency;

//                System.out.println("\nNetwork Health Summary:");
//                System.out.printf("Packet Loss: %.1f%%%n", packetLoss);
//                System.out.printf("Average Latency: %.1f ms%n", avgLatency);
//                System.out.printf("Jitter: %.1f ms%n", jitter);

                result.put("PacketLoss", String.valueOf(Math.floor(packetLoss * 100) / 100));
                result.put("Latency", String.valueOf(Math.floor(avgLatency * 100) / 100));
                result.put("Jitter", String.valueOf(Math.floor(jitter * 100) / 100));

                // Provide basic health assessment
                if (packetLoss == 0 && avgLatency < 100 && jitter < 30) {
                    result.put("HealthStatus", "Healthy");
                    //System.out.println("Network Status: HEALTHY");
                } else if (packetLoss < 5 && avgLatency < 200 && jitter < 50) {
                    result.put("HealthStatus", "Fair");
                    //System.out.println("Network Status: FAIR");
                } else {
                    result.put("HealthStatus", "Poor");
                    //System.out.println("Network Status: POOR");
                }
            }

            process.waitFor();

        } catch (Exception e) {
            System.err.println("Error during network diagnosis: " + e.getMessage());
        }

        return result;
    }
}