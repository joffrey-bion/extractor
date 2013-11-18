package com.joffrey_bion.extractor.pokemons;

import java.io.IOException;

import com.joffrey_bion.extractor.Extractor;

public class PokemonExtractor extends Extractor {

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

    private PokemonExtractor(String file, StatToGet stat) throws IOException {
        super(file);
        this.stat = stat;
    }

    public static Pokemons extractPokemons() {
        Pokemons list = new Pokemons();
        PokemonExtractor pe1;
        PokemonExtractor pe2;
        try {
            pe1 = new PokemonExtractor(PATH_SRC_BASE, StatToGet.BASE);
            pe2 = new PokemonExtractor(PATH_SRC_EV_YIELD, StatToGet.EV_YIELD);
            pe1.extractPokemonsStats(list);
            pe2.extractPokemonsStats(list);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return list;
    }

    private void extractPokemonsStats(Pokemons list) throws IOException {
        while (!isEofReached()) {
            Pokemon pokemon = new Pokemon();
            pokemon.num = readNextBetween(PKMN_NUM_PREFIX, PKMN_NUM_SUFFIX);
            if (pokemon.num == null) {
                break;
            }
            pokemon.thumbUrl = readNextBetween(IMG_LINK_PREFIX, IMG_LINK_SUFFIX);
            nextLine();
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
    }
}
