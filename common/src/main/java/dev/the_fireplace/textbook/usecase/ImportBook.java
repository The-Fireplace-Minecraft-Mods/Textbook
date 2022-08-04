package dev.the_fireplace.textbook.usecase;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import dev.the_fireplace.textbook.logic.FileImporter;
import dev.the_fireplace.textbook.logic.TextbookFileDialogs;
import net.minecraft.client.Minecraft;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class ImportBook
{
    private static final Pattern CARRIAGE_RETURN = Pattern.compile("\\R");

    private final TextbookFileDialogs textbookFileDialogs;
    private final FileImporter fileImporter;

    @Inject
    public ImportBook(TextbookFileDialogs textbookFileDialogs, FileImporter fileImporter) {
        this.textbookFileDialogs = textbookFileDialogs;
        this.fileImporter = fileImporter;
    }

    public Response importBookFromFile() {
        File importFile = textbookFileDialogs.importTextbookFileDialog();
        if (importFile == null) {
            return new Response(false, "", new ArrayList<>());
        }

        List<String> importedLines = fileImporter.importContents(importFile);
        //noinspection UnstableApiUsage
        String title = Files.getNameWithoutExtension(importFile.getName());
        if (title.length() > 16) {
            title = title.substring(0, 16);
        }
        List<String> importedPages = fileImporter.toPages(importedLines);

        return new Response(true, title, importedPages);
    }

    public Response importBookFromClipboard() {
        List<String> importedLines = Lists.newArrayList(CARRIAGE_RETURN.split(Minecraft.getInstance().keyboardHandler.getClipboard()));
        List<String> importedPages = fileImporter.toPages(importedLines);

        return new Response(true, "", importedPages);
    }

    public static class Response
    {
        private final boolean success;
        private final String title;
        private final List<String> pages;

        public Response(boolean success, String title, List<String> pages) {
            this.success = success;
            this.title = title;
            this.pages = pages;
        }

        public boolean success() {
            return success;
        }

        public String title() {
            return title;
        }

        public List<String> pages() {
            return pages;
        }
    }
}
