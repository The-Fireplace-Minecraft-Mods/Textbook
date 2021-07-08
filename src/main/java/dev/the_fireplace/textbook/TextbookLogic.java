package dev.the_fireplace.textbook;

import com.google.common.collect.Lists;
import dev.the_fireplace.lib.api.client.injectables.FileDialogFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookScreen;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
@Singleton
public final class TextbookLogic {
    private static final Pattern NEWLINE_REGEX = Pattern.compile("\\R");
    private final FileDialogFactory dialogFactory;

    @Inject
    public TextbookLogic(FileDialogFactory dialogFactory) {
        this.dialogFactory = dialogFactory;
    }

    @Nullable
    public File fileOpenSelectionDialog() {
        return dialogFactory.showOpenFileDialog(
            "gui.textbook.import.dialog_title",
            true,
            null,
            "Text files"
        );
    }

    @Nullable
    public File fileSaveSelectionDialog() {
        return dialogFactory.showSaveFileDialog(
            "gui.textbook.export.dialog_title",
            true,
            null,
            "Text files"
        );
    }

    public void exportContents(File file, BookScreen.Contents contents) {
        StringBuilder output = new StringBuilder();
        for (int pageIndex = 0; pageIndex < contents.getPageCount(); pageIndex++) {
            //noinspection HardcodedLineSeparator
            output.append(NEWLINE_REGEX.matcher(contents.getPage(pageIndex).getString()).replaceAll("\r\n")).append(" ");
        }
        writeStringToFile(file, output.toString());
    }

    @SuppressWarnings("HardcodedLineSeparator")
    public void exportContentsPreservingWhitespace(File file, BookScreen.Contents contents) {
        StringBuilder output = new StringBuilder();
        for (int pageIndex = 0; pageIndex < contents.getPageCount(); pageIndex++) {
            StringBuilder pageContents = new StringBuilder(NEWLINE_REGEX.matcher(contents.getPage(pageIndex).getString()).replaceAll("\r\n"));
            StringBuilder pageWithPreservedWhitespace = new StringBuilder(pageContents);
            while (fitsOnPage(pageWithPreservedWhitespace.toString())) {
                pageContents = new StringBuilder(pageWithPreservedWhitespace);
                pageWithPreservedWhitespace.append("\r\n");
            }
            output.append(pageContents);
        }
        writeStringToFile(file, output.toString());
    }

    private void writeStringToFile(File file, String str) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(str);
        } catch (IOException e) {
            Textbook.getLogger().error("Unable to export to file!", e);
        }
    }

    public List<String> importContents(File file) {
        List<String> lines = Lists.newArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(file), 5000)) {
            String st;
            while ((st = reader.readLine()) != null) {
                lines.add(st);
            }
        } catch (IOException e) {
            Textbook.getLogger().error("Unable to import from file!", e);
            return Collections.emptyList();
        }
        return lines;
    }

    public List<String> toPages(List<String> lines) {
        List<String> pages = Lists.newArrayList();
        StringBuilder page = new StringBuilder();
        for (String line: lines) {
            if (fitsOnPage(page+line)) {
                //noinspection HardcodedLineSeparator
                page.append(line).append("\n");
            } else if (!fitsOnPage(line)) {
                String[] parts = line.split(" ");
                StringBuilder addPart = new StringBuilder();
                for (String part: parts) {
                    if (fitsOnPage(page + addPart.toString() + part + " ")) {
                        addPart.append(part).append(" ");
                    } else {
                        if (!page.toString().isEmpty() || !addPart.toString().isEmpty()) {
                            pages.add(page.append(addPart).toString());
                            page = new StringBuilder();
                            addPart = new StringBuilder();
                        }
                        if (fitsOnPage(part)) {
                            addPart.append(part).append(" ");
                        } else {
                            char[] chars = part.toCharArray();
                            for (char c : chars) {
                                if (!fitsOnPage(page.toString() + c)) {
                                    pages.add(page.toString());
                                    page = new StringBuilder();
                                }
                                page.append(c);
                            }
                            if (fitsOnPage(page + " ")) {
                                page.append(" ");
                            } else {
                                pages.add(page.toString());
                                page = new StringBuilder();
                            }
                        }
                    }
                }
                if (!addPart.toString().isEmpty()) {
                    page.append(addPart);
                }
            } else {
                pages.add(page.toString());
                page = new StringBuilder();
                //noinspection HardcodedLineSeparator
                page.append(line).append("\n");
            }
        }
        if (!page.toString().isEmpty()) {
            pages.add(page.toString());
        }

        return pages;
    }

    public boolean fitsOnPage(String string) {
        return string.length() < 1024 && MinecraftClient.getInstance().textRenderer.getWrappedLinesHeight(string, 114) <= 128;
    }
}
