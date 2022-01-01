package dev.the_fireplace.textbook.logic;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public final class LineToPageConverter
{
    private static final String MINECRAFT_LINE_SEPARATOR = "\n";

    private final MinecraftBookConstraints constraints;

    private boolean isProcessed = true;
    private List<String> lines = new ArrayList<>();
    private List<String> pages = new ArrayList<>();
    private StringBuilder currentPage;

    @Inject
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
            return pages;
        }

        pages = new ArrayList<>();
        currentPage = new StringBuilder();
        for (String line : lines) {
            processLine(line);
        }
        if (!currentPage.toString().isEmpty()) {
            pages.add(currentPage.toString());
        }
        this.isProcessed = true;

        return pages;
    }

    private void processLine(String line) {
        boolean lineFitsOnCurrentPage = constraints.fitsOnPage(currentPage + line);
        if (lineFitsOnCurrentPage) {
            addLineToPage(line);
            return;
        }

        boolean lineFitsOnNewPage = constraints.fitsOnPage(line);
        if (lineFitsOnNewPage) {
            startNewPage();
            addLineToPage(line);
            return;
        }

        processLineLongerThanPage(line);
    }

    private void addLineToPage(String line) {
        currentPage.append(line).append(MINECRAFT_LINE_SEPARATOR);
    }

    private void startNewPage() {
        pages.add(currentPage.toString());
        currentPage = new StringBuilder();
    }

    private void processLineLongerThanPage(String line) {
        String[] words = line.split(" ");
        StringBuilder linePartForCurrentPage = new StringBuilder();
        for (String word : words) {
            boolean pageHasRoomForWord = constraints.fitsOnPage(currentPage + linePartForCurrentPage.toString() + word + " ");
            if (pageHasRoomForWord) {
                linePartForCurrentPage.append(word).append(" ");
                continue;
            }
            boolean pageHasContents = !currentPage.toString().isEmpty() || !linePartForCurrentPage.toString().isEmpty();
            if (pageHasContents) {
                currentPage.append(linePartForCurrentPage);
                startNewPage();
                linePartForCurrentPage = new StringBuilder();
            }
            boolean blankPageCanHoldWord = constraints.fitsOnPage(word);
            if (blankPageCanHoldWord) {
                linePartForCurrentPage.append(word).append(" ");
                continue;
            }
            splitLongWordAcrossPages(word);
        }
        if (!currentPage.toString().isEmpty() || !linePartForCurrentPage.toString().isEmpty()) {
            addLineToPage(linePartForCurrentPage.toString());
        }
    }

    private void splitLongWordAcrossPages(String word) {
        char[] chars = word.toCharArray();
        for (char c : chars) {
            if (!constraints.fitsOnPage(currentPage.toString() + c)) {
                startNewPage();
            }
            currentPage.append(c);
        }
        if (constraints.fitsOnPage(currentPage + " ")) {
            currentPage.append(" ");
        } else {
            startNewPage();
        }
    }
}
