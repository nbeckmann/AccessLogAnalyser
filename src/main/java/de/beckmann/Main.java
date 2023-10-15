package de.beckmann;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import de.beckmann.services.AccessLogAnalyserService;
import de.beckmann.ui.AccessLogAnalyserGui;

public class Main {

    private static final String CLI = "cli";

    public static void main(final String[] args) {
        try {
            if (isCliMode(args)) {
                startCliMode(args);
            }
            startGuiMode();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private static void startCliMode(final String[] args) throws IOException {
        final String inputFilePath = args[1];
        final String outputFilePath = args[2];
        final AccessLogAnalyserService accessLogAnalyserService = new AccessLogAnalyserService();
        accessLogAnalyserService.analyseRequestsPerSecond(inputFilePath, outputFilePath);
    }

    private static void startGuiMode() {
        AccessLogAnalyserGui accessLogAnalyserGui = new AccessLogAnalyserGui();
        accessLogAnalyserGui.showGui();
    }

    private static boolean isCliMode(String[] args) {
        if (args.length < 1) {
            return false;
        }
        String mode = args[0];
        return StringUtils.equalsIgnoreCase(mode, CLI);
    }

}