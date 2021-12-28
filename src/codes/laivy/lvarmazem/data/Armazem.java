package codes.laivy.lvarmazem.data;

import java.util.LinkedHashMap;
import java.util.Map;

import static codes.laivy.lvarmazem.LvArmazem.Y;

public class Armazem {

    public static final Map<String, Armazem> armazens = new LinkedHashMap<>();

    public int CACTUS;

    public int WHEAT_SEEDS;
    public int WHEAT;

    public int SUGAR_CANE;
    public int MELON;
    public int PUMPKIN;
    public int NETHER_WART;

    public int CARROT;
    public int POTATO;

    private final String id;

    public Armazem(String id) {
        this.id = id;

        if (Y.contains("data." + id)) {
            String i = "data." + id + ".";

            CACTUS = Y.getInt(i + "C");
            WHEAT_SEEDS = Y.getInt(i + "W_S");
            WHEAT = Y.getInt(i + "W");
            SUGAR_CANE = Y.getInt(i + "S");
            MELON = Y.getInt(i + "M");
            PUMPKIN = Y.getInt(i + "P");
            NETHER_WART = Y.getInt(i + "N");
            CARROT = Y.getInt(i + "CA");
            POTATO = Y.getInt(i + "PO");
        }

        armazens.put(id, this);
    }

    public void save() {
        String i = "data." + id + ".";

        Y.set(i + "C", CACTUS);
        Y.set(i + "W_S", WHEAT_SEEDS);
        Y.set(i + "W", WHEAT);
        Y.set(i + "S", SUGAR_CANE);
        Y.set(i + "M", MELON);
        Y.set(i + "P", PUMPKIN);
        Y.set(i + "N", NETHER_WART);
        Y.set(i + "CA", CARROT);
        Y.set(i + "PO", POTATO);
    }

    public String getId() {
        return id;
    }

}
