package de.beckmann;

import de.beckmann.services.AccessLogAnalyserService;

public class Main {

    public static void main(final String[] args) {
        try {
            final String inputFilePath = args[0];
            final String outputFilePath = args[1];
            final AccessLogAnalyserService accessLogAnalyserService = new AccessLogAnalyserService();
            accessLogAnalyserService.analyseRequestsPerSecond(inputFilePath, outputFilePath);

        } catch (final Exception e) {
            e.printStackTrace();
        }

    }
}