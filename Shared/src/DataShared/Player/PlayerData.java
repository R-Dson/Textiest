package DataShared.Player;

import DataShared.Item.EquipmentItem;

public class PlayerData {
    //Information
    public String UserName;
    public String Name;
    public String LastMultiLocation;
    public String targetName;
    public boolean isChanneling = false;

    //Data
    public int MaxHealth = -1;
    public int CurrentHealth = -1;
    public float x = 100, y = 100;

    //textures
    public int playerTextureID = -1;
    public String playerTextureName;

    public EquipmentItem[] Equipment;
    public EquipmentItem[] Inventory;

    public boolean doneCharacterCreation = false;
}