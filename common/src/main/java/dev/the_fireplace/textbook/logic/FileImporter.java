package dev.the_fireplace.textbook.logic;

import com.google.common.collect.Lists;
import dev.the_fireplace.textbook.TextbookConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public final class FileImporter
{
    public List<String> importContents(File file) {
        List<String> lines = Lists.newArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(file), 5000)) {
            String st;
            while ((st = reader.readLine()) != null) {
                lines.add(st);
            }
        } catch (IOException e) {
            TextbookConstants.getLogger().error("Unable to import from file!", e);
            return Collections.emptyList();
        }
        return lines;
    }

    public List<String> toPages(List<String> lines) {
        return TextbookConstants.getInjector().getInstance(LineToPageConverter.class)
            .setLines(lines)
            .getPages();
    }
}
