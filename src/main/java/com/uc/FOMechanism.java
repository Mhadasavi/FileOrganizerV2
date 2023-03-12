    package com.uc;

    import org.apache.commons.lang3.text.WordUtils;

    import java.io.*;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.StandardCopyOption;
    import java.nio.file.attribute.BasicFileAttributes;
    import java.sql.Time;
    import java.time.Instant;
    import java.time.LocalDateTime;
    import java.time.ZoneId;
    import java.util.Date;

    public class FOMechanism {

        final static String USER = "Dharm";
        String jarPath = System.getProperty("user.dir");

        final static String LOG_FILE_URL = "C:\\Users\\" + USER + "\\Documents\\output.txt";

        static void startFileProcessing(File fromPath, File toPath, boolean isMonthRequire) throws IOException {
            int total;
            if (fromPath == null) {
                return;
            }
            total = FileOrganizer(fromPath, toPath, isMonthRequire);
            createLogs(fromPath, toPath, total);
        }

        private static void createLogs(File fromPath, File toPath, int total) {
            try {
                OutputStream outputStream = new FileOutputStream(LOG_FILE_URL, true);
                Writer outputStreamWriter = new OutputStreamWriter(outputStream);

                outputStreamWriter.write("Log: " + Date.from(Instant.now()) + Time.from(Instant.now()) + "\n"
                        + total + " total files moved from " + fromPath + " to location " + toPath + "\n");
                outputStreamWriter.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        static int FileOrganizer(File path, File toPath, boolean isMonthRequire) throws IOException {
            int fileCount = 0;
            File[] fileList = path.listFiles();
            if (fileList == null) {
                return 0;
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
                    fileMover(file, toPath, year, month, isMonthRequire);
                    fileCount++;
                }
            }
            return fileCount;
        }

        private static void fileMover(File file, File toPath, int year, String month, boolean isMonthRequire) throws IOException {
            String fileName = file.getName();
            File directoryYear = new File(toPath.getPath() + "\\" + year);
            if (!directoryYear.exists()) {
                boolean res = directoryYear.mkdir();
            }
            if(isMonthRequire) {
                File directory = new File(directoryYear.toPath() + "\\" + month);
                if (!directory.exists()) {
                    boolean res = directory.mkdir();
                }
            }
            String to = directoryYear + "\\" + fileName;
            Files.move(file.toPath(), Path.of(to), StandardCopyOption.REPLACE_EXISTING);
        }
    }
