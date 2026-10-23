package dev.the_fireplace.textbook.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Faithful port of the original {@code dev.the_fireplace.textbook.logic.LineToPageConverter}.
 *
 * <p>Takes the lines read from a text file (or clipboard) and packs them into Minecraft
 * book pages, honouring vanilla page-size constraints and the optional byte-cap config.</p>
 *
 * <p>Strategy:
 * <ul>
 *   <li>If the entire current line + accumulator fits on the current page, append it.</li>
 *   <li>Otherwise, if the line alone fits on a fresh page, start a new page and append it.</li>
 *   <li>Otherwise the line is longer than a page: split it word-by-word, falling back to
 *       character-level splitting for words longer than a page.</li>
 * </ul>
 * </p>
 */
public final class LineToPageConverter {
    private static final String MINECRAFT_LINE_SEPARATOR = "\n";

    private final MinecraftBookConstraints constraints;
    private boolean isProcessed = true;
    private List<String> lines = new ArrayList<>();
    private List<String> pages = new ArrayList<>();
    private StringBuilder currentPage;

    public LineToPageConverter(MinecraftBookConstraints constraints) {
        this.constraints = constraints;
    }

    public LineToPageConverter setLines(List<String> lines) {
        this.lines = lines;
        this.isProcessed = false;
        return this;
    }

    public List<String> getPages() {
        if (this.isProcessed) {
            return this.pages;
        }
        this.pages = new ArrayList<>();
        this.currentPage = new StringBuilder();
        for (String line : this.lines) {
            this.processLine(line);
        }
        if (!this.currentPage.toString().isEmpty()) {
            this.pages.add(this.currentPage.toString());
        }
        this.isProcessed = true;
        return this.pages;
    }

    private void processLine(String line) {
        boolean lineFitsOnCurrentPage = this.constraints.fitsOnPage(this.currentPage.toString() + line);
        if (lineFitsOnCurrentPage) {
            this.addLineToPage(line);
            return;
        }
        boolean lineFitsOnNewPage = this.constraints.fitsOnPage(line);
        if (lineFitsOnNewPage) {
            this.startNewPage();
            this.addLineToPage(line);
            return;
        }
        this.processLineLongerThanPage(line);
    }

    private void addLineToPage(String line) {
        this.currentPage.append(line).append(MINECRAFT_LINE_SEPARATOR);
    }

    private void startNewPage() {
        this.pages.add(this.currentPage.toString());
        this.currentPage = new StringBuilder();
    }

    private void processLineLongerThanPage(String line) {
        String[] words = line.split(" ");
        StringBuilder linePartForCurrentPage = new StringBuilder();
        for (String word : words) {
            boolean pageHasRoomForWord = this.constraints.fitsOnPage(
                    this.currentPage.toString() + linePartForCurrentPage.toString() + word + " ");
            if (pageHasRoomForWord) {
                linePartForCurrentPage.append(word).append(" ");
                continue;
            }
            boolean pageHasContents =
                    !this.currentPage.toString().isEmpty()
                            || !linePartForCurrentPage.toString().isEmpty();
            if (pageHasContents) {
                this.currentPage.append(linePartForCurrentPage);
                this.startNewPage();
                linePartForCurrentPage = new StringBuilder();
            }
            boolean blankPageCanHoldWord = this.constraints.fitsOnPage(word);
            if (blankPageCanHoldWord) {
                linePartForCurrentPage.append(word).append(" ");
                continue;
            }
            this.splitLongWordAcrossPages(word);
        }
        if (!this.currentPage.isEmpty() || !linePartForCurrentPage.toString().isEmpty()) {
            this.addLineToPage(linePartForCurrentPage.toString());
        }
    }

    private void splitLongWordAcrossPages(String word) {
        char[] chars = word.toCharArray();
        for (char c : chars) {
            if (!this.constraints.fitsOnPage(this.currentPage.toString() + c)) {
                this.startNewPage();
            }
            this.currentPage.append(c);
        }
        if (this.constraints.fitsOnPage(this.currentPage.toString() + " ")) {
            this.currentPage.append(" ");
        } else {
            this.startNewPage();
        }
    }
}
