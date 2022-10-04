package dev.the_fireplace.textbook.usecase;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.mojang.authlib.yggdrasil.response.Response;
import dev.the_fireplace.textbook.logic.FileImporter;
import dev.the_fireplace.textbook.logic.TextbookFileDialogs;
import net.minecraft.client.MinecraftClient;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public class ImportBook
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
            return new Response(false, "", List.of());
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
        List<String> importedLines = Lists.newArrayList(CARRIAGE_RETURN.split(MinecraftClient.getInstance().keyboard.getClipboard()));
        List<String> importedPages = fileImporter.toPages(importedLines);

        return new Response(true, "", importedPages);
    }

    public record Response(boolean success, String title, List<String> pages)
    {
    }
}
