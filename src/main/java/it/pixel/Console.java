package it.pixel;

import it.pixel.service.FileReplacer;

import java.io.IOException;

public class Console {
    private static final String DEFAULT_FORMAT = "json";
    private static final String DEFAULT_OUTPUT = "output";
    public static void main(final String[] args) throws IOException {
        if (0 == args.length) {
            System.out.println("Input folder required.");
            return;
        }

        String format = Console.DEFAULT_FORMAT;
        if (1 < args.length) {
            format = args[1];
        }

        System.out.println("\nSearch set to ." + format + " files.");
        System.out.println("\nScanning:\n\t> " + args[0]);
        final FileReplacer fileReplacer = new FileReplacer(args[0], Console.DEFAULT_OUTPUT, format);
        fileReplacer.execute();

    }

}
