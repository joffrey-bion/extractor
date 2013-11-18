package com.joffrey_bion.extractor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class Extractor {

    private String source;
    protected BufferedReader reader;
    private boolean eof;
    private String line;
    private int lastIndexInLine;

    protected Extractor(String resourceFile) throws IOException {
        this.source = resourceFile;
        InputStream is = getClass().getResourceAsStream(source);
        if (is == null) {
            throw new FileNotFoundException("Couldn't find the file " + source);
        }
        reader = new BufferedReader(new InputStreamReader(is));
        eof = false;
        nextLine();
    }
    
    public boolean nextLine() throws IOException {
        try {
            line = reader.readLine();
            if (line == null) {
                eof = true;
            }
            lastIndexInLine = 0;
            return !eof;
        } catch (IOException e) {
            try {
                reader.close();
            } catch (IOException ignore) {
            }
            throw e;
        }
    }

    protected boolean isEofReached() {
        return eof;
    }
    
    protected String readNextBetween(String prefix, String suffix) throws IOException {
        String result = null;
        while ((result = extractBetween(prefix, suffix)) == null) {
            if (!nextLine()) {
                break;
            }
        }
        return result;
    }

    protected String readNextAfterPrefix(String prefix) throws IOException {
        String result = null;;
        while ((result = extractAfterPrefix(prefix)) == null) {
            if (!nextLine()) {
                break;
            }
        }
        return result;
    }

    protected String extractAfterPrefix(String prefix) {
        if (line == null) {
            return null;
        }
        int i = line.indexOf(prefix);
        if (i == -1) {
            return null;
        }
        return line.substring(i + prefix.length());
    }

    protected String extractBetween(String prefix, String suffix) {
        line = extractAfterPrefix(prefix);
        if (line == null) {
            return null;
        }
        int i = line.indexOf(suffix);
        return line.substring(0, i);
    }
}
