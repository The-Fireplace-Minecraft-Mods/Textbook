package dev.the_fireplace.textbook.logic;

import com.google.common.collect.Lists;
import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.textbook.Textbook;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class FileImporter
{
    public List<String> importContents(File file) {
        List<String> lines = Lists.newArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(file), 5000)) {
            String st;
            while ((st = reader.readLine()) != null) {
                lines.add(st);
            }
        } catch (IOException e) {
            Textbook.getLogger().error("Unable to import from file!", e);
            return Collections.emptyList();
        }
        return lines;
    }

    public List<String> toPages(List<String> lines) {
        return DIContainer.get().getInstance(LineToPageConverter.class)
            .setLines(lines)
            .getPages();
    }
}
