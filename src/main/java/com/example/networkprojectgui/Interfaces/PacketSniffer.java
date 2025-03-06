package com.example.networkprojectgui.Interfaces;

import org.jnetpcap.PcapIf;

import java.util.List;

public interface PacketSniffer {
    /**
     * Get list of available network devices
     * @return List of network devices
     */
    List<PcapIf> getDevices();

    /**
     * Start packet sniffing on selected device
     * @param device The network device to sniff packets from
     */
    void startSniffing(PcapIf device);

    /**
     * Stop the packet sniffing process
     */
    void stopSniffing();
}