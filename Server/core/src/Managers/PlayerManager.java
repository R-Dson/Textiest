package Managers;

import Data.FixedValues;
import DataShared.Item.EquipmentItem;
import DataShared.Item.Item;
import DataShared.Player.PlayerData;
import Managers.Items.ItemManager;
import Managers.Network.UserIdentity;
import com.badlogic.gdx.math.Vector2;

public class PlayerManager {

    public static void GenerateData(PlayerData pData){
        pData.Inventory = new Item[FixedValues.BaseInventoryLength];
        pData.Equipment = new EquipmentItem[FixedValues.EquipmentLength];
    }

    public static void AddItemToInventory(UserIdentity userIdentity, Item item){
        PlayerData pData = userIdentity.playerData;
        if (pData.Inventory == null) return;
        int lowest = ItemManager.FindFirstNullInArray(pData.Inventory);
        if (lowest != -1) {
            pData.Inventory[lowest] = item;
            userIdentity.setUpdatedUserInfo(true);
        }
    }

    public static void UnEquip(UserIdentity userIdentity, Item item){
        PlayerData pData = userIdentity.playerData;
        if (pData.Inventory.length < FixedValues.BaseInventoryLength && item instanceof EquipmentItem) {
            EquipmentItem eItem = (EquipmentItem) item;
            int i = ItemManager.EquipmentTypeEnumToInt(eItem.EquipmentType);
            if (i != -1){
                Item currentItem = pData.Equipment[i];
                AddItemToInventory(userIdentity, currentItem); // updates player data
                pData.Equipment[i] = null;
            }
        }
    }

    public static void EquipItem(UserIdentity userIdentity, Item item){
        if (!(item instanceof EquipmentItem))
            return;

        PlayerData pData = userIdentity.playerData;
        EquipmentItem equipmentItem = (EquipmentItem) item;

        int position = ItemManager.EquipmentTypeEnumToInt(equipmentItem.EquipmentType);

        if (position != -1){
            if(pData.Equipment[position] == null)
                pData.Equipment[position] = equipmentItem;
            else{
                if (pData.Inventory.length < FixedValues.BaseInventoryLength) {
                    EquipmentItem currentItem = pData.Equipment[position];
                    pData.Equipment[position] = equipmentItem;
                    AddItemToInventory(userIdentity, currentItem); // updates player data
                }
            }
        }
    }
}
