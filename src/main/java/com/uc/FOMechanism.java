package com.uc;

import org.apache.commons.lang3.text.WordUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FOMechanism {

    final static String USER = "Dharm";
    static String jarPath = System.getProperty("user.dir") + "//output.txt";

    ////final static String LOG_FILE_URL = "C:\\Users\\" + USER + "\\Documents\\output.txt";

    static void startFileProcessing(File fromPath, File toPath, List<Boolean> userOption) throws IOException {
        LocalDateTime startTime = LocalDateTime.now();
        int total;
        if (fromPath == null) {
            return;
        }
        long[] moveResult = FileOrganizer(fromPath, toPath, userOption);
        LocalDateTime endTime = LocalDateTime.now();
        Duration timeTaken = Duration.between(startTime, endTime);

        createLogs(fromPath, toPath, moveResult, timeTaken);
    }

    private static void createLogs(File fromPath, File toPath, long[] moveResult, Duration timeTaken) {
        try {
            OutputStream outputStream = new FileOutputStream(jarPath, true);
            Writer outputStreamWriter = new OutputStreamWriter(outputStream);

//                outputStreamWriter.write("Log: " + Date.from(Instant.now()) + Time.from(Instant.now()) + "\n"
//                        + total + " total files moved from " + fromPath + " to location " + toPath + "\n");
            outputStreamWriter.write(String.format("Moved %d files from %s to %s. Total file size: %d Kilobytes. Time taken: %d ms. Date: %s\n",
                    moveResult[0], fromPath, toPath, moveResult[1], timeTaken.toMillis(), getCurrentDate()));
            outputStreamWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static long[] FileOrganizer(File path, File toPath, List<Boolean> userOption) throws IOException {
        int fileCount = 0;
        long totalSize = 0;
        File[] fileList = path.listFiles();
        if (fileList == null) {
            throw new IllegalArgumentException("No File Found");
        }

        for (File file : fileList) {
            if (file != null) {
                BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                LocalDateTime time = attr.lastModifiedTime()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                int year = time.getYear();
                String month = time.getMonth().name();
                month = WordUtils.capitalizeFully(month);
                long fileSize = fileMover(file, toPath, year, month, userOption);
                totalSize += fileSize;
                fileCount++;
            }
        }
        return new long[]{fileCount, totalSize};
    }

    private static long fileMover(File file, File toPath, int year, String month, List<Boolean> userOption) throws IOException {
        String fileName = file.getName();
        File directoryYear = new File(toPath.getPath() + "//" + year);
        //Path path=Paths.get(file.getAbsolutePath());
        long fileSize = Files.size(Paths.get(file.getAbsolutePath())) / 1024;
        String to = directoryYear.toString();
        if (userOption.get(0)) {
            if (!directoryYear.exists()) {
                directoryYear.mkdir();
            }
        }
        if (userOption.get(1)) {
            File monthDirectory = new File(directoryYear.toPath() + "//" + month);
            if (!monthDirectory.exists()) {
                monthDirectory.mkdir();
            }
            to = monthDirectory.toString();
        }
        if (userOption.get(2)) {
            String type = getFileType(file.getName());
            if (type != null) {
                File typeDir = new File(toPath.getPath() + File.separator + type);
                if (!typeDir.exists()) {
                    typeDir.mkdir();
                }
                to = typeDir.toString();
            }
        }
        to = to + "//" + fileName;
        if (file.exists() && !file.getName().equals(".DS_Store")) {
            Files.move(file.toPath(), Path.of(to), StandardCopyOption.REPLACE_EXISTING);
        }
        return fileSize;
    }

    private static String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }

    private static String getFileType(String fileName) {
        String type = null;
        int pos = fileName.lastIndexOf('.');
        if (pos > 0 && pos < fileName.length() - 1) {
            type = fileName.substring(pos + 1).toLowerCase();
        }
        return type;
    }
}
