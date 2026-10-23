package dev.the_fireplace.textbook.logic;

import javax.annotation.Nullable;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.HeadlessException;
import java.io.File;
import java.util.Optional;

/**
 * Replaces the original Fireplace Lib {@code FileDialogFactory}. The original lib wrapped
 * an AWT {@code FileDialog}; we re-implement the same behaviour with a Swing
 * {@link JFileChooser}, which avoids the awkward AWT modal blocking on modern Linux
 * window managers and is simpler to drive from a headless-friendly code path.
 *
 * <p>The translation keys ({@code gui.textbook.export.dialog_title},
 * {@code gui.textbook.import.dialog_title}) are resolved through Minecraft's
 * {@code Component} API so the dialog title localises correctly.</p>
 */
public final class TextbookFileDialogs {

    private static final FileNameExtensionFilter TEXT_FILE_FILTER =
            new FileNameExtensionFilter("Text files (*.txt)", "txt");

    @Nullable
    public File exportTextbookFileDialog() {
        JFileChooser chooser = newFileChooser();
        chooser.setDialogTitle(localized("gui.textbook.export.dialog_title"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(TEXT_FILE_FILTER);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        // Suggest a default file name
        chooser.setSelectedFile(new File(FileSystemView.getFileSystemView().getDefaultDirectory(), "book.txt"));
        int result;
        try {
            result = chooser.showSaveDialog(null);
        } catch (HeadlessException e) {
            return null;
        }
        if (result != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File selected = chooser.getSelectedFile();
        if (selected != null && !selected.getName().contains(".")) {
            selected = new File(selected.getParentFile(), selected.getName() + ".txt");
        }
        return selected;
    }

    @Nullable
    public File importTextbookFileDialog() {
        JFileChooser chooser = newFileChooser();
        chooser.setDialogTitle(localized("gui.textbook.import.dialog_title"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setFileFilter(TEXT_FILE_FILTER);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        int result;
        try {
            result = chooser.showOpenDialog(null);
        } catch (HeadlessException e) {
            return null;
        }
        if (result != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        return chooser.getSelectedFile();
    }

    private static JFileChooser newFileChooser() {
        return new JFileChooser();
    }

    private static String localized(String key) {
        return Optional.ofNullable(net.minecraft.client.Minecraft.getInstance())
                .map(mc -> net.minecraft.network.chat.Component.translatable(key).getString())
                .orElse(key);
    }
}
