package Managers.Items;

import Data.FixedValues;
import Managers.FileManager;

import java.util.ArrayList;

public class ItemManager {

    public ArrayList<EquipmentItem> itemList = new ArrayList<>();

    public void LoadItems(){
        itemList = FileManager.LoadItemsFromFile();
    }
    //TODO Add item generator

    //TODO ASYNC?
    public EquipmentItem GetRandomBaseItem(){
        if(itemList == null) return null;
        int n = FixedValues.random.nextInt(itemList.size() + 1);
        EquipmentItem tempItem = itemList.get(n);

        //repeats until it finds an item
        while (tempItem.ItemRarity != ItemEnums.ItemRarity.NORMAL)
        {
            n = FixedValues.random.nextInt(itemList.size() + 1);
            tempItem = itemList.get(n);
        }
        return tempItem;
    }

    //Add and remove items from given inventory and player. Returns the new inventory
    public static Item[] AddItemToInventory(Item item, Item[] items){
        int n = FindFirstNullInArray(items);
        //add to inventory if there's space else drop it
        if (n != -1){
            items[n] = item;
        }
        else{
            //Drop item


        }
        return items;
    }

    public static int FindFirstNullInArray(Object[] array){
        int n = -1;
        //Finds first empty slot in inventory
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null)
            {
                n = i;
                return n;
            }
        }
        return n;
    }

    public static int EquipmentTypeEnumToInt(ItemEnums.EquipmentType type){
        switch (type){

            case HELM:
                return FixedValues.HELM;
            case CHEST:
                return FixedValues.CHEST;
            case LEGS:
                return FixedValues.LEGS;
            case WEAPON:
                return FixedValues.WEAPON;
            case CAPE:
                return FixedValues.CAPE;
            case GLOVES:
                return FixedValues.GLOVES;
            case RING:
                return FixedValues.RING;
            case BOOTS:
                return FixedValues.BOOTS;
            case QUIVER:
                return FixedValues.QUIVER;
            case ARROWS:
                return FixedValues.ARROWS;
        }
        return -1;
    }
}
