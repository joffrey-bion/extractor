package org.hildan.charsextractor;

import java.io.IOException;

import org.hildan.utils.io.extractor.Extractor;

public class CharacterExtractor extends Extractor {

    private static final String PATH_SRC = "boxdrawing.htm";

    private static final String NUM_PREFIX = "<td class=\"cpt\">U+";

    private static final String NUM_SUFFIX = "</td>";

    private static final String DESC_PREFIX = "<td class=\"name\">";

    private static final String DESC_SUFFIX = "</td>";

    public static void main(String[] args) {
        try {
            new CharacterExtractor().printJavaConstants();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CharacterExtractor() throws IOException {
        super(PATH_SRC);
    }

    private void printJavaConstants() throws IOException {
        while (!isEofReached()) {
            String num = extractNextBetween(NUM_PREFIX, NUM_SUFFIX);
            if (num != null) {
                String desc = extractNextBetween(DESC_PREFIX, DESC_SUFFIX);
                System.out.println(getLine(num, desc));
            }
        }
    }

    private static String getLine(String num, String desc) {
        String fieldName = desc.replace(' ', '_');
        fieldName = fieldName.replace('-', '_');
        return "public static final char " + fieldName + " = Character.toChars(0x" + num + ")[0];";
    }
}
