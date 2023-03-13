package DataShared.Item;

public class Item implements Cloneable{
    public int ID;
    public String Name;
    public ItemEnums.ItemRarity ItemRarity;
    public Boolean Stackable;
    public ItemEnums.ItemType ItemType;
    public Material Material;

    @Override
    public Item clone() throws CloneNotSupportedException {
        return (Item) super.clone();
    }

    public static String ObjectNameToText(String objectName)
    {
        switch (objectName){
            // TREES
            case "LOG":
                return "Normal Tree";
            case "OAK":
                return "Oak Tree";
            case "WILLOW":
                return "Willow Tree";
            // ORES
            case "STONE":
                return "Stone";
            case "IRON":
                return "Iron Ore";
        }
        return "MISSING OBJECT";
    }
}
