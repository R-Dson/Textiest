package Data;

import java.util.Random;

public class FixedValues {

    public static Random random = new Random();

    public static final int EquipmentLength = 10;
    public static final int BaseInventoryLength = 30;
    public static final int MaxConnectionsToLayer = 20;
    public static final int HELM = 0, CHEST = 1, LEGS = 2, WEAPON = 3, CAPE = 4, GLOVES = 5, RING = 6, BOOTS = 7, QUIVER = 8, ARROWS = 9;

    public static final float UpdateFrequency30 = 1/30f;
    public static final float TileScale = 1/64f;

    public static final String TextureAtlasData = "Data/TextureAtlasData.json";
    public static final String AbilityData = "Data/AbilityData.json";
    public static final String ItemData = "Data/ItemData.json";
    public static final String MapData = "Data/MapData.json";
    public static final String DefaultMap = "map";


    //TODO Add probabilities for rarity rolls
}
