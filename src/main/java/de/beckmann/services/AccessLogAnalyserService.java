package de.beckmann.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccessLogAnalyserService {

    private static final String TIMESTAMP_FORMAT = "dd/MMM/yyyy:HH:mm:ss";
    private static final String HEADER = "Timestamp;Anzahl Requests\n";
    private static final String CSV_FORMAT = "%s;%d\n";
    private static final String REGEX_TIMESTAMP = "\\d{2}\\/\\w{3}\\/\\d{4}:\\d{2}:\\d{2}:\\d{2}";

    public void analyseRequestsPerSecond(String inputFilePath, String outputFilePath) throws IOException
    {
        final List<LocalDateTime> timestamps = readTimestamps(inputFilePath);

            final Map<LocalDateTime, Long> counts = countEntriesPerSecond(timestamps);

            writeToCsv(outputFilePath, counts);
            for (final Map.Entry<LocalDateTime, Long> entry : counts.entrySet()) {
                System.out.println("Timestamp " + entry.getKey() + " had " + entry.getValue() + " entries.");
            }
    }

    private static void writeToCsv(final String outputFile, final Map<LocalDateTime, Long> counts) throws IOException
    {
        final Path path = Path.of(outputFile);
        
        Files.write(path, HEADER.getBytes());
        for(final Entry<LocalDateTime, Long> entry : counts.entrySet())
        {
            final String entryString =String.format(CSV_FORMAT, entry.getKey(), entry.getValue());
            Files.write(path, entryString.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        }
    }

    
    private static List<LocalDateTime> readTimestamps(final String filePath) {

        try {
            final Stream<String> lines = Files.lines(Paths.get(filePath));
            final List<LocalDateTime> timestamps = new ArrayList<>();
            
            lines.forEach(line -> {
                final LocalDateTime dt = getTimstampFromEntry(line);
                if (dt != null) {
                    timestamps.add(dt);
                }

            });

            lines.close();
            return timestamps;
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<LocalDateTime>();
    }

    private static LocalDateTime getTimstampFromEntry(final String entry) {
        final String dateString = getDateStringFromEntry(entry);
        return convertStringToTimestamp(dateString);
    }

    private static String getDateStringFromEntry(final String entry) {
        final Pattern regexPatter = Pattern.compile(REGEX_TIMESTAMP);
        final Matcher matcher = regexPatter.matcher(entry);

        if (matcher.find()) {
            final String s = matcher.group();
            return s;
        }

        return null;
    }

    private static LocalDateTime convertStringToTimestamp(final String timestampString) {
        try {
            
            if (timestampString == null) {
                return null;
            }
            
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT, Locale.ENGLISH);
            return LocalDateTime.from(formatter.parse(timestampString));
        } catch (final DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Map<LocalDateTime, Long> countEntriesPerSecond(final List<LocalDateTime> dateTimeList) {

        return dateTimeList.stream()
                .collect(Collectors.groupingBy(
                        dt -> dt.withNano(0),
                        Collectors.counting()));
    }

}
