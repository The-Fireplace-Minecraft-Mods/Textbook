package the_fireplace.textbook;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class TextbookLogic {
    public static File getFile() {
        String file = TinyFileDialogs.tinyfd_openFileDialog(new TranslatableText("gui.textbook.import.dialog_title").getString(), null, null, new TranslatableText("gui.textbook.import.text_files").getString(), false);
        if(file == null)
            return null;
        return new File(file);
    }

    public static List<String> importContents(File file) {
        List<String> lines = Lists.newArrayList();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file), 5000);
            String st;
            while ((st = reader.readLine()) != null)
                lines.add(st);
            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return lines;
    }

    public static List<String> toPages(List<String> lines) {
        List<String> pages = Lists.newArrayList();
        StringBuilder page = new StringBuilder();
        for(String line: lines) {
            if(fitsOnPage(page+line))
                page.append(line).append("\n");
            else if(!fitsOnPage(line)) {
                String[] parts = line.split(" ");
                StringBuilder addPart = new StringBuilder();
                for(String part: parts) {
                    if (fitsOnPage(page.toString() + addPart.toString() + part + " ")) {
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
                            if (fitsOnPage(page.toString() + " "))
                                page.append(" ");
                            else {
                                pages.add(page.toString());
                                page = new StringBuilder();
                            }
                        }
                    }
                }
                if(!addPart.toString().isEmpty())
                    page.append(addPart);
            } else {
                pages.add(page.toString());
                page = new StringBuilder();
                page.append(line).append("\n");
            }
        }
        if(!page.toString().isEmpty())
            pages.add(page.toString());
        return pages;
    }

    public static boolean fitsOnPage(String string) {
        return string.length() < 1024 && MinecraftClient.getInstance().textRenderer.getStringBoundedHeight(string, 114) <= 128;
    }
}
