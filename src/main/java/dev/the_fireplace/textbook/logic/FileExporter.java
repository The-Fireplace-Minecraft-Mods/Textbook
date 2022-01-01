package dev.the_fireplace.textbook.logic;

import dev.the_fireplace.textbook.TextbookConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.BookScreen;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public class FileExporter
{
    private static final Pattern NEWLINE_REGEX = Pattern.compile("\\R");

    private final MinecraftBookConstraints constraints;

    @Inject
    public FileExporter(MinecraftBookConstraints constraints) {
        this.constraints = constraints;
    }

    public void exportContents(File file, BookScreen.Contents contents) {
        StringBuilder output = new StringBuilder();
        for (int pageIndex = 0; pageIndex < contents.getPageCount(); pageIndex++) {
            //noinspection HardcodedLineSeparator
            output.append(NEWLINE_REGEX.matcher(contents.getPage(pageIndex).getString()).replaceAll("\r\n")).append(" ");
        }
        writeStringToFile(file, output.toString());
    }

    @SuppressWarnings("HardcodedLineSeparator")
    public void exportContentsPreservingWhitespace(File file, BookScreen.Contents contents) {
        StringBuilder output = new StringBuilder();
        for (int pageIndex = 0; pageIndex < contents.getPageCount(); pageIndex++) {
            StringBuilder pageContents = new StringBuilder(NEWLINE_REGEX.matcher(contents.getPage(pageIndex).getString()).replaceAll("\r\n"));
            StringBuilder pageWithPreservedWhitespace = new StringBuilder(pageContents);
            while (constraints.fitsOnPage(pageWithPreservedWhitespace.toString())) {
                pageContents = new StringBuilder(pageWithPreservedWhitespace);
                pageWithPreservedWhitespace.append("\r\n");
            }
            output.append(pageContents);
        }
        writeStringToFile(file, output.toString());
    }

    private void writeStringToFile(File file, String str) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(str);
        } catch (IOException e) {
            TextbookConstants.getLogger().error("Unable to export to file!", e);
        }
    }
}
