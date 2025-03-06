package com.example.networkprojectgui.DataTypes;

import javafx.beans.property.*;

import java.util.Arrays;
import java.util.List;

public class PacketInfo {
    private static int counter = 0;
    private final IntegerProperty id;
    private final StringProperty timestamp;
    private final StringProperty sourceIP;
    private final StringProperty destIP;
    private final StringProperty protocol;
    private final StringProperty info;
    public enum SecurityStatus {
        SAFE, SUSPICIOUS, MALICIOUS
    }
    private final ObjectProperty<SecurityStatus> securityStatus;

    public PacketInfo(String timestamp, String sourceIP, String destIP, String protocol, String info) {
        this.id = new SimpleIntegerProperty(++counter);
        this.timestamp = new SimpleStringProperty(timestamp);
        this.sourceIP = new SimpleStringProperty(sourceIP);
        this.destIP = new SimpleStringProperty(destIP);
        this.protocol = new SimpleStringProperty(protocol);
        this.info = new SimpleStringProperty(info);
        this.securityStatus = new SimpleObjectProperty<>(analyzeSecurityStatus(protocol, sourceIP, destIP, info));
    }

    public SecurityStatus getSecurityStatus() {
        return securityStatus.get();
    }
    public ObjectProperty<SecurityStatus> securityStatusProperty() {
        return securityStatus;
    }

    private SecurityStatus analyzeSecurityStatus(String protocol, String sourceIP, String destIP, String info) {
        // Common malicious port numbers
        List<Integer> suspiciousPorts = Arrays.asList(31337, 12345, 27374, 27665);
        List<Integer> maliciousPorts = Arrays.asList(666, 1243, 6666, 6667, 27444, 27665);

        // Check for common security indicators
        if (protocol.equals("TCP") || protocol.equals("UDP")) {
            // Extract port numbers from info
            try {
                String[] parts = info.split("Ports: ")[1].split(" → ");
                int sourcePort = Integer.parseInt(parts[0]);
                int destPort = Integer.parseInt(parts[1]);

                if (maliciousPorts.contains(sourcePort) || maliciousPorts.contains(destPort)) {
                    return SecurityStatus.MALICIOUS;
                }
                if (suspiciousPorts.contains(sourcePort) || suspiciousPorts.contains(destPort)) {
                    return SecurityStatus.SUSPICIOUS;
                }
            } catch (Exception e) {
                // Parse error, continue with other checks
            }
        }

        // Check for suspicious ICMP patterns
        if (protocol.equals("ICMP")) {
            if (info.contains("Echo Request") || info.contains("Echo Reply")) {
                // Large number of ICMP requests might indicate ping flood
                return SecurityStatus.SUSPICIOUS;
            }
        }

        // Check for ARP spoofing attempts
        if (protocol.equals("ARP")) {
            // Check for suspicious ARP patterns
            if (info.contains("is at")) {
                // Could check for conflicting MAC addresses
                return SecurityStatus.SUSPICIOUS;
            }
        }

        // Add additional security checks here
        // Check for known malicious IP addresses
        if (isKnownMaliciousIP(sourceIP) || isKnownMaliciousIP(destIP)) {
            return SecurityStatus.MALICIOUS;
        }

        return SecurityStatus.SAFE;
    }

    private boolean isKnownMaliciousIP(String ip) {
        // Add your list of known malicious IPs
        List<String> maliciousIPs = Arrays.asList(
                "192.168.1.666", // Malicious IPs
                "10.0.0.666"
        );
        return maliciousIPs.contains(ip);
    }

    public static void resetCounter() {
        counter = 0;
    }

    // Getters
    public Integer getId() { return id.get(); }
    public String getTimestamp() { return timestamp.get(); }
    public String getSourceIP() { return sourceIP.get(); }
    public String getDestIP() { return destIP.get(); }
    public String getProtocol() { return protocol.get(); }
    public String getInfo() { return info.get(); }

    // Property getters
    public IntegerProperty idProperty() { return id; }
    public StringProperty timestampProperty() { return timestamp; }
    public StringProperty sourceIPProperty() { return sourceIP; }
    public StringProperty destIPProperty() { return destIP; }
    public StringProperty protocolProperty() { return protocol; }
    public StringProperty infoProperty() { return info; }
}