package Data;

import Components.Entities.PlayerEntity;
import Managers.Items.EquipmentItem;
import Managers.Items.ItemManager;
import com.badlogic.gdx.math.Vector2;

public class PlayerData {
    //Information
    public String Name;
    public String LastMultiLocation;
    public String targetName;
    public boolean isChanneling = false;

    //Data
    public int MaxHealth = -1;
    public int CurrentHealth = -1;
    public Vector2 PlayerPosition = new Vector2(0,0);

    //textures
    public int playerTextureID = -1;
    public String playerTextureName;

    public EquipmentItem[] Equipment;
    public EquipmentItem[] Inventory;

    public boolean doneCharacterCreation = false;

    //private
    private PlayerEntity target;

    public void GenerateData(){
        Inventory = new EquipmentItem[FixedValues.BaseInventoryLength];
        Equipment = new EquipmentItem[FixedValues.EquipmentLength];
    }

    public void AddItemToInventory(EquipmentItem item){
        if (Inventory == null) return;
        int lowestpos = ItemManager.FindFirstNullInArray(Inventory);
        if (lowestpos != -1)
            Inventory[lowestpos] = item;
    }

    public void UnEquip(EquipmentItem item){
        if (Inventory.length < FixedValues.BaseInventoryLength) {
            int i = ItemManager.EquipmentTypeEnumToInt(item.EquipmentType);
            if (i != -1){
                EquipmentItem currentItem = Equipment[i];
                AddItemToInventory(currentItem);
                Equipment[i] = null;
            }
        }
    }

    public void EquipItem(EquipmentItem item){
        int position = ItemManager.EquipmentTypeEnumToInt(item.EquipmentType);
        if (position != -1){
            if(Equipment[position] == null){
                Equipment[position] = item;
            }
            else{
                if (Inventory.length < FixedValues.BaseInventoryLength) {
                    EquipmentItem currentItem = Equipment[position];
                    Equipment[position] = item;
                    AddItemToInventory(currentItem);
                }
            }
        }
    }

}
