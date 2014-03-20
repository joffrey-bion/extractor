package com.jbion.pkmnextractor;

public class Pokemon {

    private static final String TABLE_SPECIES_NAME = "Species";

    private static final String SQL_INSERT_BEGIN = "INSERT INTO " + TABLE_SPECIES_NAME + " VALUES ";

    public String num;
    public String name;
    public String version;
    public String thumbUrl;
    public String base_hp;
    public String base_att;
    public String base_def;
    public String base_spa;
    public String base_spd;
    public String base_spe;
    public String xp_yield;
    public String ev_yield_hp;
    public String ev_yield_att;
    public String ev_yield_def;
    public String ev_yield_spa;
    public String ev_yield_spd;
    public String ev_yield_spe;

    public String get3DigitsNum() {
        return num.substring(0, 3);
    }

    public void updateEVYieldFrom(Pokemon evs) {
        xp_yield = evs.xp_yield;
        ev_yield_hp = evs.ev_yield_hp;
        ev_yield_att = evs.ev_yield_att;
        ev_yield_def = evs.ev_yield_def;
        ev_yield_spa = evs.ev_yield_spa;
        ev_yield_spd = evs.ev_yield_spd;
        ev_yield_spe = evs.ev_yield_spe;
    }
    
    public static String getCsvHeaderLine() {
        return "#,Name,Version,ThumbUrl,Base HP,Base Att,Base Def,Base SpA,Base SpD,Base Spe,"
                + "XP yield,EV yield HP,EV yield Att,EV yield Def,EV yield SpA,EV yield SpD,EV yield Spe";
    }

    public String toCsvString() {
        return get3DigitsNum() + "," + name + "," + version + "," + thumbUrl + "," + base_hp + "," + base_att
                + "," + base_def + "," + base_spa + "," + base_spd + "," + base_spe + ","
                + xp_yield + "," + ev_yield_hp + "," + ev_yield_att + "," + ev_yield_def + ","
                + ev_yield_spa + "," + ev_yield_spd + "," + ev_yield_spe;
    }

    public String toSqlInsert() {
        String strDelim = "'";
        if (name.contains(strDelim)) {
            strDelim = "\"";
        }
        return SQL_INSERT_BEGIN + "(" + get3DigitsNum() + "," + strDelim + name + strDelim
                + "," + strDelim + version + strDelim + "," + base_hp + "," + base_att + ","
                + base_def + "," + base_spa + "," + base_spd + "," + base_spe + "," + xp_yield
                + "," + ev_yield_hp + "," + ev_yield_att + "," + ev_yield_def + "," + ev_yield_spa
                + "," + ev_yield_spd + "," + ev_yield_spe + ");";
    }

    public boolean isSimilarTo(Object pokemon) {
        if (!(pokemon instanceof Pokemon)) {
            return false;
        }
        Pokemon p = (Pokemon) pokemon;
        return p.get3DigitsNum().equals(get3DigitsNum()) && p.name.equals(name);
    }

    @Override
    public boolean equals(Object pokemon) {
        if (!isSimilarTo(pokemon)) {
            return false;
        }
        Pokemon p = (Pokemon) pokemon;
        if (version == null) {
            return p.version == null;
        } else {
            return version.equals(p.version);
        }
    }

    @Override
    public String toString() {
        return num + "," + name + "," + version + "," + base_hp + "," + base_att + "," + base_def
                + "," + base_spa + "," + base_spd + "," + base_spe + "," + xp_yield + ","
                + ev_yield_hp + "," + ev_yield_att + "," + ev_yield_def + "," + ev_yield_spa + ","
                + ev_yield_spd + "," + ev_yield_spe;
    }

	@Override
	public int hashCode() {
		int hash = 1;
		int prime = 31;
		hash = hash * prime + get3DigitsNum().hashCode();
		hash = hash * prime + name.hashCode();
		hash = hash * prime + version.hashCode();
		return hash;
	}
}
