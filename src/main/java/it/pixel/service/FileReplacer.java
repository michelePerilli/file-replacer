package it.pixel.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pixel.model.Rule;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReplacer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String destination;
    private final String source;
    private final String format;

    public FileReplacer(final String source, final String destination, final String format) {
        this.source = source;
        this.destination = destination;
        this.format = format;
    }

    public void execute() throws IOException {
        final File f = Paths.get("regex.json").toFile();
        final FileInputStream regexFileInputStream = new FileInputStream(f);
        final String json = IOUtils.toString(regexFileInputStream, StandardCharsets.UTF_8);
        final List<Rule> rules = this.objectMapper.readValue(json, new TypeReference<>() {
        });
        final Stream<Path> walk = Files.walk(Paths.get(this.source));

        try (walk) {
            final List<File> files = walk.filter(Files::isRegularFile).filter(x -> x.getFileName().toString().endsWith(this.format)).map(Path::toFile).toList();

            final String reduce = files.stream().map(File::getPath).collect(Collectors.joining("\n\t> "));

            System.out.println("File(s) found:\n\t> " + reduce);

            for (final File file : files) {
                final FileInputStream fileInputStream = new FileInputStream(file);
                String body = IOUtils.toString(fileInputStream, StandardCharsets.UTF_8);
                for (final Rule rule : rules) {
                    body = body.replaceAll(rule.getRegex(), rule.getValue());
                }
                final File outputFile = new File(this.destination + "\\" + file.getName());

                FileUtils.write(outputFile, body, StandardCharsets.UTF_8);
            }
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }

    }

}