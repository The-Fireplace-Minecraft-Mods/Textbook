package dev.the_fireplace.textbook.api;

import net.minecraft.text.Text;

import javax.annotation.Nullable;
import java.io.File;

public interface FileDialogFactoryNew {
    @Nullable
    File showOpenFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);

    @Nullable
    File showOpenFileDialog(Text title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);

    @Nullable
    File[] showOpenMultiFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);

    @Nullable
    File[] showOpenMultiFileDialog(Text title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);

    @Nullable
    File showSaveFileDialog(String titleTranslationKey, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);

    @Nullable
    File showSaveFileDialog(Text title, boolean rememberPath, @Nullable String[] allowedFileTypePatterns, @Nullable String allowedFileTypesDescription);
}
