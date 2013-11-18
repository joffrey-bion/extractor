package com.jbion.pkmnextractor;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedList;

public class Main {

    private static final String DESKTOP_PATH = System.getProperty("user.home") + "/Desktop";
    private static final String PATH_CSV = DESKTOP_PATH + "/pokemon_species.csv";
    private static final String PATH_SQL = DESKTOP_PATH + "/init_all_species.sql";

    public static void main(String[] args) {
        Pokemons list = PokemonExtractor.extractPokemons();
        for (Pokemon p : list) {
            System.out.println(p);
        }
        // downloadBigImg(list, DESKTOP_PATH + "/images");
        writeToSql(list);
        writeToCsv(list);
    }

    @SuppressWarnings("unused")
    private static void downloadBigImg(LinkedList<Pokemon> list, String destFolder) {
        for (Pokemon p : list) {
            String name = p.name;
            int i = name.indexOf("'");
            if (i != -1) {
                name = name.substring(0, i) + name.substring(i + 1);
            }
            name = name.replace(' ', '-');
            name = name.replaceAll("\\.", "");
            download("http://img.pokemondb.net/artwork/" + name.toLowerCase() + ".jpg", destFolder
                    + "/" + p.num + ".jpg");
        }
    }

    private static void writeToSql(LinkedList<Pokemon> list) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(PATH_SQL));
            for (Pokemon p : list) {
                writer.write(p.toSqlInsert() + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }

    private static void writeToCsv(LinkedList<Pokemon> list) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(PATH_CSV));
            writer.write(Pokemon.getCsvHeaderLine() + "\n");
            for (Pokemon p : list) {
                writer.write(p.toCsvString() + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }

    private static void download(String url, String destFilePath) {
        URL website;
        try {
            website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(destFilePath);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        } catch (MalformedURLException e) {
            e.getMessage();
        } catch (FileNotFoundException e) {
            e.getMessage();
        } catch (IOException e) {
            e.getMessage();
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
