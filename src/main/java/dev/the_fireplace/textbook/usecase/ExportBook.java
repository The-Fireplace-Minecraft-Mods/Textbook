package dev.the_fireplace.textbook.usecase;

import dev.the_fireplace.textbook.logic.FileExporter;
import dev.the_fireplace.textbook.logic.TextbookFileDialogs;
import net.minecraft.client.gui.screens.inventory.BookViewScreen.BookAccess;

import java.io.File;

/**
 * Port of the original {@code dev.the_fireplace.textbook.usecase.ExportBook}.
 *
 * <p>Pops a "Save" file dialog and writes the supplied book contents to the
 * chosen file, optionally preserving per-page whitespace.</p>
 */
public final class ExportBook {
    private final TextbookFileDialogs textbookFileDialogs;
    private final FileExporter fileExporter;

    public ExportBook(TextbookFileDialogs textbookFileDialogs, FileExporter fileExporter) {
        this.textbookFileDialogs = textbookFileDialogs;
        this.fileExporter = fileExporter;
    }

    public void export(BookAccess bookContents, boolean preserveWhitespace) {
        File exportFile = this.textbookFileDialogs.exportTextbookFileDialog();
        if (exportFile == null) {
            return;
        }
        if (preserveWhitespace) {
            this.fileExporter.exportContentsPreservingWhitespace(exportFile, bookContents);
        } else {
            this.fileExporter.exportContents(exportFile, bookContents);
        }
    }
}
