package dev.the_fireplace.textbook.logic;

import dev.the_fireplace.lib.api.client.injectables.FileDialogFactory;
import dev.the_fireplace.textbook.api.FileDialogFactoryNew;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;

public class TextbookFileDialogs
{
    private final FileDialogFactoryNew dialogFactory;
    @Inject
    public TextbookFileDialogs(FileDialogFactoryNew dialogFactory) {
        this.dialogFactory = dialogFactory;
    }
    @Nullable
    public File exportTextbookFileDialog() {
        return dialogFactory.showSaveFileDialog(
                "gui.textbook.export.dialog_title",
                true,
                null,
                "Text files"
        );
    }
    @Nullable
    public File importTextbookFileDialog() {
        return dialogFactory.showOpenFileDialog(
                "gui.textbook.import.dialog_title",
                true,
                null,
                "Text files"
        );
    }
}