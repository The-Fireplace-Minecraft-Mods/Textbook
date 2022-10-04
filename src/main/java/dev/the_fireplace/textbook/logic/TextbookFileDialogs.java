package dev.the_fireplace.textbook.logic;

import dev.the_fireplace.lib.api.client.injectables.FileDialogFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;

public class TextbookFileDialogs
{
    private final FileDialogFactory dialogFactory;

    @Inject
    public TextbookFileDialogs(FileDialogFactory dialogFactory) {
        this.dialogFactory = dialogFactory;
    }
    
}
