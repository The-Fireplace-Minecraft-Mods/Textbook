package dev.the_fireplace.textbook.usecase;

import dev.the_fireplace.textbook.logic.FileExporter;
import dev.the_fireplace.textbook.logic.TextbookFileDialogs;
import net.minecraft.client.gui.screen.ingame.BookScreen;

import javax.inject.Inject;
import java.io.File;

public class ExportBook
{
    private final TextbookFileDialogs textbookFileDialogs;
    private final FileExporter fileExporter;

    @Inject
    public ExportBook(TextbookFileDialogs textbookFileDialogs, FileExporter fileExporter, FileExporter fileExporter1) {
        this.textbookFileDialogs = textbookFileDialogs;
        this.fileExporter = fileExporter1;
    }

    public void export(BookScreen.Contents bookContents, boolean preserveWhitespace) {
        File exportFile = textbookFileDialogs.exportTextbookFileDialog();
        if (exportFile == null) {
            return;
        }
        if (preserveWhitespace) {
            fileExporter.exportContentsPreservingWhitespace(exportFile, bookContents);
        } else {
            fileExporter.exportContents(exportFile, bookContents);
        }
    }
}
