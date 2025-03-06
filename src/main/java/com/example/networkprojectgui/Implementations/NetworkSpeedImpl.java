package com.example.networkprojectgui.Implementations;


import com.example.networkprojectgui.Interfaces.NetworkSpeed;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class NetworkSpeedImpl implements com.example.networkprojectgui.Interfaces.NetworkSpeed {
    final String CURL_PATH = new File("./bin/curl.exe").getAbsolutePath();
    final int FILE_SIZE = 5;
    final String FILE_UNIT = "MB";
    final int DOWNLOAD_TIMEOUT = 25;

    @Override
    public double measureDowndloadSpeed() {
        double speedMbps = 0;
        File downloadedFile = null;
        try {
            // Create Temp Folder
            File tempDir = new File("temp");
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            // Define downloaded file
            downloadedFile = new File(String.format("temp/%d%s.zip", FILE_SIZE, FILE_UNIT));

            // Run Curl Command with detailed statistics
            ProcessBuilder curlProcess = new ProcessBuilder(
                    CURL_PATH,
                    "-L",
                    "-w", "\nTime total: %{time_total}\nSize downloaded: %{size_download}\nSpeed: %{speed_download}\n",
                    "-o", downloadedFile.getPath(),
                    String.format("http://speedtest.tele2.net/%d%s.zip", FILE_SIZE, FILE_UNIT)
            );
            curlProcess.redirectErrorStream(true);
            Process process = curlProcess.start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    //System.out.println(line); // Print progress in real-time
                }
            }

            // Wait for process with timeout
            if (!process.waitFor(DOWNLOAD_TIMEOUT, TimeUnit.SECONDS)) {
                process.destroy();
                if (!process.waitFor(1, TimeUnit.SECONDS)) {
                    process.destroyForcibly();
                }
                System.out.println("Download timed out.");
                return 0;
            }

            // Extract the speed value
            Pattern speedPattern = Pattern.compile("Speed: (\\d+)");
            Pattern sizePattern = Pattern.compile("Size downloaded: (\\d+)");

            double bytesDownloaded = 0;
            double speedBytesPerSec = 0;

            // Find the downloaded size
            Matcher sizeMatcher = sizePattern.matcher(output);
            if (sizeMatcher.find()) {
                bytesDownloaded = Double.parseDouble(sizeMatcher.group(1));
            }

            // Find the speed
            Matcher speedMatcher = speedPattern.matcher(output);
            if (speedMatcher.find()) {
                speedBytesPerSec = Double.parseDouble(speedMatcher.group(1));
            }

            // Convert to Mbps
            speedMbps = (speedBytesPerSec * 8) / (1_000_000); // Convert to megabits per second

            //System.out.println("\nResults:");
            //System.out.printf("Average Speed: %.2f Mbps%n", speedMbps);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Math.floor(speedMbps * 100) / 100;
    }

    @Override
    public double measureUploadSpeed() {
        double speedMbps = 0;
        try {
            // Create ProcessBuilder with detailed statistics
            ProcessBuilder processBuilder = new ProcessBuilder(
                    CURL_PATH,
                    "-T", String.format("temp/%d%s.zip", FILE_SIZE, FILE_UNIT),
                    "-L",
                    "-w", "\nTime total: %{time_total}\nSize uploaded: %{size_upload}\nSpeed: %{speed_upload}\n",
                    "https://postman-echo.com/post"
            );
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    //System.out.println(line); // Print progress in real-time
                }
            }

            // Wait for process with timeout
            if (!process.waitFor(DOWNLOAD_TIMEOUT, TimeUnit.SECONDS)) {
                process.destroy();
                if (!process.waitFor(1, TimeUnit.SECONDS)) {
                    process.destroyForcibly();
                }
                System.out.println("Upload timed out.");
                return 0;
            }

            // Extract the speed value
            Pattern speedPattern = Pattern.compile("Speed: (\\d+)");
            Pattern sizePattern = Pattern.compile("Size uploaded: (\\d+)");

            double bytesUploaded = 0;
            double speedBytesPerSec = 0;

            // Find the uploaded size
            Matcher sizeMatcher = sizePattern.matcher(output);
            if (sizeMatcher.find()) {
                bytesUploaded = Double.parseDouble(sizeMatcher.group(1));
            }

            // Find the speed
            Matcher speedMatcher = speedPattern.matcher(output);
            if (speedMatcher.find()) {
                speedBytesPerSec = Double.parseDouble(speedMatcher.group(1));
            }

            // Convert to Mbps
            speedMbps = (speedBytesPerSec * 8) / (1_000_000); // Convert to megabits per second

            //System.out.println("\nResults:");
            //System.out.printf("Average Upload Speed: %.2f Mbps%n", speedMbps);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Math.floor(speedMbps * 100) / 100;
    }
}
