package Managers;

import Data.Credential;
import Data.FixedValues;
import DataShared.Ability.Ability;
import DataShared.Files.FileManagerShared;
import DataShared.Item.EquipmentItem;
import Managers.Map.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.List;

public class FileManager {
    //public static Gson gson = new Gson();
    public static Credential LoadCredentialFile(){
        String data = Loader("Credentials.json");
        Json j = new Json();
        Credential c = j.fromJson(Credential.class, data);
        return c;
    }

    public static ArrayList<EquipmentItem> LoadItemsFromFile(){
        String data = Loader(FixedValues.ItemData);
        Json j = new Json();
        ArrayList<EquipmentItem> c = j.fromJson(ArrayList.class, EquipmentItem.class, data);
        return c;
    }

    public static ArrayList<Map> LoadMapsFromFile(){
        return FileManagerShared.getList(FixedValues.MapData, Map.class);
    }

    private static String Loader(String path){
        FileHandle file = Gdx.files.local(path);
        if (file == null)
            return null;
        return file.readString();
    }
}
