package dev.the_fireplace.textbook.logic;

import dev.the_fireplace.textbook.Textbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Port of the original {@code dev.the_fireplace.textbook.logic.FileImporter}.
 *
 * <p>Reads a plain-text file line-by-line and converts the lines to Minecraft
 * book pages via {@link LineToPageConverter}.</p>
 */
public final class FileImporter {

    private final LineToPageConverter lineToPageConverter;

    public FileImporter(LineToPageConverter lineToPageConverter) {
        this.lineToPageConverter = lineToPageConverter;
    }

    public List<String> importContents(File file) {
        List<String> lines = Lists.newArrayList();
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file, StandardCharsets.UTF_8), 5000)) {
            String st;
            while ((st = reader.readLine()) != null) {
                lines.add(st);
            }
        } catch (IOException e) {
            Textbook.LOGGER.error("Unable to import from file!", e);
            return Collections.emptyList();
        }
        return lines;
    }

    public List<String> toPages(List<String> lines) {
        return lineToPageConverter.setLines(lines).getPages();
    }
}
