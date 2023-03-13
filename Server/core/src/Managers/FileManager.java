package Managers;

import Data.Credential;
import Data.FixedValues;
import DataShared.Files.FileManagerShared;
import DataShared.Item.EquipmentItem;
import DataShared.Item.Item;
import Managers.Map.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

public class FileManager {
    public static Credential LoadCredentialFile(){
        String data = Loader("Credentials.json");
        Json j = new Json();
        return j.fromJson(Credential.class, data);
    }

    public static ArrayList<EquipmentItem> LoadItemsEquipFromFile(){
        String data = Loader(FixedValues.ItemEquiptData);
        Json j = new Json();
        return j.fromJson(ArrayList.class, EquipmentItem.class, data);
    }

    public static ArrayList<Item> LoadItemsFromFile(){
        String data = Loader(FixedValues.ItemData);
        Json j = new Json();
        return j.fromJson(ArrayList.class, Item.class, data);
    }

    public static ArrayList<Map> LoadMapsFromFile(){
        return FileManagerShared.getList(FixedValues.MapData, Map.class);
    }

    private static String Loader(String path){
        FileHandle file = Gdx.files.internal(path);
        if (file == null)
            return null;
        return file.readString();
    }
}
