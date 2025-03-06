package com.example.networkprojectgui.DataTypes;

public class HealthData {
    public double latency; // ms
    public double packetLoss; // percentage
    public double throughput; // Mbps
    public double jitter; // ms
    public int errorRates; // errors/minute

    public HealthData(double latency, double packetLoss, double throughput, double jitter, int errorRates) {
        this.latency = latency;
        this.packetLoss = packetLoss;
        this.throughput = throughput;
        this.jitter = jitter;
        this.errorRates = errorRates;
    }

    public double getLatency() {
        return latency;
    }
    public void setLatency(double latency) {
        this.latency = latency;
    }
    public double getPacketLoss() {
        return packetLoss;
    }
    public void setPacketLoss(double packetLoss) {
        this.packetLoss = packetLoss;
    }
    public double getThroughput() {
        return throughput;
    }
    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }
    public double getJitter() {
        return jitter;
    }
    public void setJitter(double jitter) {
        this.jitter = jitter;
    }
    public int getErrorRates() {
        return errorRates;
    }
    public void setErrorRates(int errorRates) {
        this.errorRates = errorRates;
    }

    @Override
    public String toString() {
        return "Latency: " + latency + ", Packet Loss: " + packetLoss + ", Throughput: " + throughput + ", Jitter: " + jitter + ", Error Rates: " + errorRates;
    }
}
