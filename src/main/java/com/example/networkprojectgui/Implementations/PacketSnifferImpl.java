package com.example.networkprojectgui.Implementations;

import com.example.networkprojectgui.DataTypes.PacketInfo;
import com.example.networkprojectgui.Interfaces.PacketSniffer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class PacketSnifferImpl implements PacketSniffer {
    private static final int SNAPLEN = 64 * 1024;
    private static final int TIMEOUT = 1000;
    private static final int PACKET_COUNT = 1;
    private volatile boolean isRunning = false;
    private final ObservableList<PacketInfo> packets = FXCollections.observableArrayList();

    public ObservableList<PacketInfo> getPackets() {
        return packets;
    }

    public void startSniffing(PcapIf device) {
        StringBuilder errbuf = new StringBuilder();
        //System.out.println("Starting capture on device: " + device.getName()); // Debug log

        PacketInfo.resetCounter();
        Platform.runLater(() -> {
            packets.clear();
            //System.out.println("Cleared packets list"); // Debug log
        });

        Pcap pcap = Pcap.openLive(device.getName(), SNAPLEN, Pcap.MODE_PROMISCUOUS, TIMEOUT, errbuf);
        if (pcap == null) {
            System.err.println("Error opening device: " + errbuf); // Debug log
            throw new RuntimeException("Error opening device: " + errbuf);
        }

        try {
            isRunning = true;
            while (isRunning && !Thread.currentThread().isInterrupted()) {
                int result = pcap.loop(PACKET_COUNT, createPacketHandler(), null);
                if (result != Pcap.OK) {
                    System.err.println("Error capturing packets: " + pcap.getErr()); // Debug log
                }
            }
        } finally {
            pcap.close();
            //System.out.println("Capture stopped"); // Debug log
        }
    }

    public List<PcapIf> getDevices() {
        List<PcapIf> devices = new ArrayList<>();
        StringBuilder errbuf = new StringBuilder();

        if (Pcap.findAllDevs(devices, errbuf) != Pcap.OK) {
            throw new RuntimeException("Error finding devices: " + errbuf);
        }

        return devices;
    }
    private PcapPacketHandler<String> createPacketHandler() {
        return (packet, user) -> {
            if (packet == null || packet.size() == 0) {
                return;
            }

            String timestamp = new SimpleDateFormat("HH:mm:ss.SSS").format(
                    new Date(packet.getCaptureHeader().timestampInMillis()));
            String sourceIP = "", destIP = "", protocol = "", details = "";

            // Extract protocol headers
            Ethernet eth = new Ethernet();
            if (packet.hasHeader(eth)) {
                sourceIP = FormatUtils.mac(eth.source());
                destIP = FormatUtils.mac(eth.destination());
            }

            Ip4 ip = new Ip4();
            if (packet.hasHeader(ip)) {
                sourceIP = FormatUtils.ip(ip.source());
                destIP = FormatUtils.ip(ip.destination());
            }

            // Get protocol and details
            byte[] data = packet.getByteArray(0, packet.size());
            StringBuilder protocolDetails = new StringBuilder();

            if (packet.hasHeader(new Tcp())) {
                Tcp tcp = new Tcp();
                packet.hasHeader(tcp);
                protocol = "TCP";
                // Add TCP flags and payload info
                protocolDetails.append(formatPayloadInfo(data));
            } else if (packet.hasHeader(new Udp())) {
                Udp udp = new Udp();
                packet.hasHeader(udp);
                protocol = "UDP";
                // Add UDP payload info
                protocolDetails.append(formatPayloadInfo(data));
            } else if (packet.hasHeader(new Icmp())) {
                Icmp icmp = new Icmp();
                packet.hasHeader(icmp);
                protocol = "ICMP";
                protocolDetails.append(String.format("Type: %d, Code: %d - ", icmp.type(), icmp.code()));
                switch (icmp.type()) {
                    case 0 -> protocolDetails.append("Echo Reply");
                    case 3 -> protocolDetails.append("Destination Unreachable");
                    case 8 -> protocolDetails.append("Echo Request");
                    case 11 -> protocolDetails.append("Time Exceeded");
                    default -> protocolDetails.append("Other ICMP Type");
                }
            } else if (packet.hasHeader(new Arp())) {
                Arp arp = new Arp();
                packet.hasHeader(arp);
                protocol = "ARP";
                switch (arp.operation()) {
                    case 1 -> protocolDetails.append("Who has ").append(FormatUtils.ip(arp.tpa()))
                            .append("? Tell ").append(FormatUtils.ip(arp.spa()));
                    case 2 -> protocolDetails.append(FormatUtils.ip(arp.spa()))
                            .append(" is at ").append(FormatUtils.mac(arp.sha()));
                    default -> protocolDetails.append("Operation: ").append(arp.operation());
                }
            } else {
                protocol = "Other";
                protocolDetails.append(formatPayloadInfo(data));
            }

            details = protocolDetails.toString();

            PacketInfo packetInfo = new PacketInfo(timestamp, sourceIP, destIP, protocol, details);
            Platform.runLater(() -> packets.add(packetInfo));
        };
    }

    private String formatPayloadInfo(byte[] data) {
        StringBuilder info = new StringBuilder();

        // Convert payload to ASCII and get printable characters
        for (int i = 0; i < Math.min(data.length, 60); i++) {
            byte b = data[i];
            if (b >= 32 && b <= 126) {
                info.append((char) b);
            } else {
                info.append('.');
            }
        }

        if (data.length > 60) {
            info.append("...");
        }

        return info.toString();
    }    public void stopSniffing() {
        isRunning = false;
    }

}