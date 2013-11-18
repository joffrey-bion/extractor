package com.joffrey_bion.extractor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Extractor {

    private static final String PATH_SRC_BASE = "pokemonBaseStats.htm";
    private static final String PATH_SRC_EV_YIELD = "pokemonEV.htm";

    private static final String IMG_LINK_PREFIX = "src=\"";
    private static final String IMG_LINK_SUFFIX = "\"";

    private static final String PKMN_NUM_PREFIX = "title=\"";
    private static final String PKMN_NUM_SUFFIX = "\"><img ";

    private static final String PKMN_NAME_PREFIX = "title=\"";
    private static final String PKMN_NAME_SUFFIX = " (Pok";

    private static final String SPECIAL_FORME_PREFIX = "<small>(";
    private static final String SPECIAL_FORME_SUFFIX = ")</small>";

    // end of line after these ones, no suffix needed
    private static final String XP_PREFIX = "<td style=\"background:#FFFFFF\"> ";
    private static final String HP_PREFIX = "<td style=\"background:#FF5959\"> ";
    private static final String ATT_PREFIX = "<td style=\"background:#F5AC78\"> ";
    private static final String DEF_PREFIX = "<td style=\"background:#FAE078\"> ";
    private static final String SPA_PREFIX = "<td style=\"background:#9DB7F5\"> ";
    private static final String SPD_PREFIX = "<td style=\"background:#A7DB8D\"> ";
    private static final String SPE_PREFIX = "<td style=\"background:#FA92B2\"> ";

    private static enum StatToGet {
        BASE,
        EV_YIELD
    }

    private StatToGet stat;
    private String source;
    private BufferedReader reader;
    private String line;

    private Extractor(String file, StatToGet stat) {
        this.source = file;
        this.stat = stat;
    }

    public static Pokemons extractPokemons() {
        Pokemons list = new Pokemons();
        new Extractor(PATH_SRC_BASE, StatToGet.BASE).extractPokemonsStats(list);
        new Extractor(PATH_SRC_EV_YIELD, StatToGet.EV_YIELD).extractPokemonsStats(list);
        return list;
    }

    private void extractPokemonsStats(Pokemons list) {
        try {
            InputStream is = Extractor.class.getResourceAsStream(source);
            reader = new BufferedReader(new InputStreamReader(is));
            System.out.println("Source: " + source);
            line = reader.readLine();
            while (line != null) {
                Pokemon pokemon = new Pokemon();
                pokemon.num = readNextBetween(PKMN_NUM_PREFIX, PKMN_NUM_SUFFIX);
                if (pokemon.num == null) {
                    break;
                }
                pokemon.thumbUrl = readNextBetween(IMG_LINK_PREFIX, IMG_LINK_SUFFIX);
                line = reader.readLine();
                pokemon.name = readNextBetween(PKMN_NAME_PREFIX, PKMN_NAME_SUFFIX);
                pokemon.version = extractBetween(SPECIAL_FORME_PREFIX, SPECIAL_FORME_SUFFIX);
                switch (stat) {
                case BASE:
                    pokemon.base_hp = readNextAfterPrefix(HP_PREFIX);
                    pokemon.base_att = readNextAfterPrefix(ATT_PREFIX);
                    pokemon.base_def = readNextAfterPrefix(DEF_PREFIX);
                    pokemon.base_spa = readNextAfterPrefix(SPA_PREFIX);
                    pokemon.base_spd = readNextAfterPrefix(SPD_PREFIX);
                    pokemon.base_spe = readNextAfterPrefix(SPE_PREFIX);
                    list.add(pokemon);
                    break;
                case EV_YIELD:
                    pokemon.xp_yield = readNextAfterPrefix(XP_PREFIX);
                    pokemon.ev_yield_hp = readNextAfterPrefix(HP_PREFIX);
                    pokemon.ev_yield_att = readNextAfterPrefix(ATT_PREFIX);
                    pokemon.ev_yield_def = readNextAfterPrefix(DEF_PREFIX);
                    pokemon.ev_yield_spa = readNextAfterPrefix(SPA_PREFIX);
                    pokemon.ev_yield_spd = readNextAfterPrefix(SPD_PREFIX);
                    pokemon.ev_yield_spe = readNextAfterPrefix(SPE_PREFIX);
                    Pokemon old = list.get(pokemon);
                    if (old == null) {
                        for (Pokemon p : list.getSimilar(pokemon)) {
                            p.updateEVYieldFrom(pokemon);
                        }
                    } else {
                        old.updateEVYieldFrom(pokemon);
                    }
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
    }

    private String readNextBetween(String prefix, String suffix) throws IOException {
        String result;
        while ((result = extractBetween(prefix, suffix)) == null) {
            if ((line = reader.readLine()) == null) {
                break;
            }
        }
        return result;
    }

    private String readNextAfterPrefix(String prefix) throws IOException {
        String result;
        while ((result = extractAfterPrefix(prefix)) == null) {
            if ((line = reader.readLine()) == null) {
                break;
            }
        }
        return result;
    }

    private String extractAfterPrefix(String prefix) {
        if (line == null) {
            return null;
        }
        int i = line.indexOf(prefix);
        if (i == -1) {
            return null;
        }
        return line.substring(i + prefix.length());
    }

    private String extractBetween(String prefix, String suffix) {
        line = extractAfterPrefix(prefix);
        if (line == null) {
            return null;
        }
        int i = line.indexOf(suffix);
        return line.substring(0, i);
    }
}
