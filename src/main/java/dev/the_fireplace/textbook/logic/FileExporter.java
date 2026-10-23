package dev.the_fireplace.textbook.logic;

import dev.the_fireplace.textbook.Textbook;
import net.minecraft.client.gui.screens.inventory.BookViewScreen.BookAccess;
import net.minecraft.network.chat.FormattedText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * Port of the original {@code dev.the_fireplace.textbook.logic.FileExporter}.
 *
 * <p>Two export modes:
 * <ul>
 *   <li>{@link #exportContents(File, BookAccess)} collapses all pages into one
 *       whitespace-normalised blob separated by a single space.</li>
 *   <li>{@link #exportContentsPreservingWhitespace(File, BookAccess)} keeps page
 *       breaks and trims trailing whitespace per page while preserving internal
 *       line structure.</li>
 * </ul>
 * </p>
 */
public final class FileExporter {
    private static final Pattern NEWLINE_REGEX = Pattern.compile("\\R");

    private final MinecraftBookConstraints constraints;

    public FileExporter(MinecraftBookConstraints constraints) {
        this.constraints = constraints;
    }

    public void exportContents(File file, BookAccess contents) {
        StringBuilder output = new StringBuilder();
        for (int pageIndex = 0; pageIndex < contents.getPageCount(); ++pageIndex) {
            String pageText = pageText(contents, pageIndex);
            output.append(NEWLINE_REGEX.matcher(pageText).replaceAll("\r\n")).append(" ");
        }
        writeStringToFile(file, output.toString());
    }

    public void exportContentsPreservingWhitespace(File file, BookAccess contents) {
        StringBuilder output = new StringBuilder();
        for (int pageIndex = 0; pageIndex < contents.getPageCount(); ++pageIndex) {
            String rawPageText = NEWLINE_REGEX.matcher(pageText(contents, pageIndex)).replaceAll("\r\n");
            StringBuilder pageContents = new StringBuilder(rawPageText);
            StringBuilder pageWithPreservedWhitespace = new StringBuilder(pageContents);
            while (this.constraints.fitsOnPage(pageWithPreservedWhitespace.toString())) {
                pageContents = new StringBuilder(pageWithPreservedWhitespace);
                pageWithPreservedWhitespace.append("\r\n");
            }
            output.append(pageContents);
        }
        writeStringToFile(file, output.toString());
    }

    private static String pageText(BookAccess contents, int pageIndex) {
        FormattedText formattedText = contents.getPage(pageIndex);
        return formattedText.getString();
    }

    private void writeStringToFile(File file, String str) {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(str);
        } catch (IOException e) {
            Textbook.LOGGER.error("Unable to export to file!", e);
        }
    }
}
