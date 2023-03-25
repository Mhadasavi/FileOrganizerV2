package com.uc;

import org.apache.commons.lang3.text.WordUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FOMechanism {

    static String jarPath = System.getProperty("user.dir") + "//output.txt";

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

            outputStreamWriter.write(String.format("Moved %d files from %s to %s. Total file size: %d MB. Time taken: %d ms. Date: %s\n",
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
        File directoryYear = new File(toPath.getPath() + File.separator + year);
        //Path path=Paths.get(file.getAbsolutePath());
        //long fileSize = Files.size(Paths.get(file.getAbsolutePath())) / (1000*1000);
        long fileSizeBytes = file.length();
        long fileSize = fileSizeBytes / 1048576;
        String to = directoryYear.toString();
        if (userOption.get(0)) {
            if (!directoryYear.exists()) {
                boolean res = directoryYear.mkdirs();
            }
        }
        if (userOption.get(1)) {
            File monthDirectory = new File(directoryYear.getPath() + File.separator + month);
            if (!monthDirectory.exists()) {
                boolean res = monthDirectory.mkdirs();
            }
            to = monthDirectory.toString();
        }
        if (userOption.get(2)) {
            String type = getFileType(file.getName());
            if (type != null) {
                File typeDir = new File(toPath.getPath() + File.separator + type);
                if (!typeDir.exists()) {
                    boolean res = typeDir.mkdirs();
                }
                to = typeDir.toString();
            }
        }
        to = to + File.separator + fileName;
        if (file.exists()) {
            try {
                Files.move(file.toPath(), Path.of(to), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
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

    protected static String[][] getLogs(String[] col) {
        String[][] data = new String[0][col.length]; // initialize the array with 0 rows
        File file = new File(System.getProperty("user.dir") + "//output.txt");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                Pattern pattern = Pattern.compile("Moved (\\d+) files from (.*?) to (.*?). Total file size: (\\d+) MB. Time taken: (\\d+) ms. Date: (.*?)$");
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    String[] row = new String[col.length];
                    // extract properties from regex groups
                    row[0] = matcher.group(3);
                    row[1] = matcher.group(1);
                    row[2] = matcher.group(4) + " MB";
                    row[3] = matcher.group(5) + " ms";
                    row[4] = matcher.group(6);
                    data = addRowToArray(data, row);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return getBlankCellData(data);
    }

    private static String[][] getBlankCellData(String[][] data) {
        // determine the number of blank rows needed
        int numRows = 10; // total number of rows to display
        int numDataRows = data.length; // number of rows with data
        int numBlankRows = numRows - numDataRows; // number of blank rows needed
        int colLength = 5;
        if (numDataRows < numRows) {
            // create an array of empty objects for the blank rows
            String[][] blankRows = new String[numBlankRows][colLength];
            for (int i = 0; i < numBlankRows; i++) {
                for (int j = 0; j < colLength; j++) {
                    blankRows[i][j] = ""; // set each cell in the blank row to an empty string
                }
            }
            // combine the data and blank rows into a single array
            String[][] allRows = new String[numRows][colLength];
            System.arraycopy(data, 0, allRows, 0, numDataRows); // copy the data rows
            System.arraycopy(blankRows, 0, allRows, numDataRows, numBlankRows); // copy the blank rows
            return allRows;
        }
        return data;
    }

    private static String[][] addRowToArray(String[][] array, String[] row) {
        String[][] newArray = new String[array.length + 1][row.length];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = row;
        return newArray;
    }
}
