package dev.the_fireplace.textbook.usecase;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import dev.the_fireplace.textbook.logic.FileImporter;
import dev.the_fireplace.textbook.logic.TextbookFileDialogs;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Port of the original {@code dev.the_fireplace.textbook.usecase.ImportBook}.
 *
 * <p>Two entry points:
 * <ul>
 *   <li>{@link #importBookFromFile()} pops a "Open" file dialog and reads the chosen
 *       file. The file's name (without extension) becomes the suggested book title.</li>
 *   <li>{@link #importBookFromClipboard()} pulls the current OS clipboard text and
 *       converts it directly to pages without a title.</li>
 * </ul>
 * </p>
 */
public final class ImportBook {
    private static final Pattern CARRIAGE_RETURN = Pattern.compile("\\R");
    private static final int MAX_TITLE_LENGTH = 16;

    private final TextbookFileDialogs textbookFileDialogs;
    private final FileImporter fileImporter;

    public ImportBook(TextbookFileDialogs textbookFileDialogs, FileImporter fileImporter) {
        this.textbookFileDialogs = textbookFileDialogs;
        this.fileImporter = fileImporter;
    }

    public Response importBookFromFile() {
        File importFile = this.textbookFileDialogs.importTextbookFileDialog();
        if (importFile == null) {
            return new Response(false, "", List.of());
        }
        List<String> importedLines = this.fileImporter.importContents(importFile);
        String title = Files.getNameWithoutExtension(importFile.getName());
        if (title.length() > MAX_TITLE_LENGTH) {
            title = title.substring(0, MAX_TITLE_LENGTH);
        }
        List<String> importedPages = this.fileImporter.toPages(importedLines);
        return new Response(true, title, importedPages);
    }

    public Response importBookFromClipboard() {
        String clipboard = Minecraft.getInstance().keyboardHandler.getClipboard();
        List<String> importedLines =
                Lists.newArrayList(Splitter.on(CARRIAGE_RETURN).splitToList(clipboard));
        List<String> importedPages = this.fileImporter.toPages(importedLines);
        return new Response(true, "", importedPages);
    }

    public record Response(boolean success, String title, List<String> pages) {
    }
}
