package Managers;

import Data.FixedValues;
import DataShared.Item.EquipmentItem;
import DataShared.Player.PlayerData;
import Managers.Items.ItemManager;
import com.badlogic.gdx.math.Vector2;

public class PlayerManager {

    public static Vector2 GetPosition(PlayerData playerData){
        return new Vector2(playerData.x, playerData.y);
    }

    public static void SetPosition(PlayerData playerData, Vector2 pos){
        playerData.x = pos.x;
        playerData.y = pos.y;
    }

    public static void GenerateData(PlayerData pData){
        pData.Inventory = new EquipmentItem[FixedValues.BaseInventoryLength];
        pData.Equipment = new EquipmentItem[FixedValues.EquipmentLength];
    }

    public static void AddItemToInventory(PlayerData pData, EquipmentItem item){
        if (pData.Inventory == null) return;
        int lowest = ItemManager.FindFirstNullInArray(pData.Inventory);
        if (lowest != -1)
            pData.Inventory[lowest] = item;
    }

    public static void UnEquip(PlayerData pData, EquipmentItem item){
        if (pData.Inventory.length < FixedValues.BaseInventoryLength) {
            int i = ItemManager.EquipmentTypeEnumToInt(item.EquipmentType);
            if (i != -1){
                EquipmentItem currentItem = pData.Equipment[i];
                AddItemToInventory(pData, currentItem);
                pData.Equipment[i] = null;
            }
        }
    }

    public static void EquipItem(PlayerData pData, EquipmentItem item){
        int position = ItemManager.EquipmentTypeEnumToInt(item.EquipmentType);
        if (position != -1){
            if(pData.Equipment[position] == null)
                pData.Equipment[position] = item;
            else{
                if (pData.Inventory.length < FixedValues.BaseInventoryLength) {
                    EquipmentItem currentItem = pData.Equipment[position];
                    pData.Equipment[position] = item;
                    AddItemToInventory(pData, currentItem);
                }
            }
        }
    }
}
