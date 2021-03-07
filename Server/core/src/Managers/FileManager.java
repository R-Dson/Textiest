package Managers;

import Data.Credential;
import Data.FixedValues;
import Managers.Ability.Ability;
import Managers.Items.EquipmentItem;
import Managers.Map.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

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
        return getList(FixedValues.MapData, Map.class);
    }

    public static ArrayList<Ability> GetAbilities(String FilePath){
        return getList(FilePath, Ability.class);
    }

    private static <T> ArrayList<T> getList(String FilePath, Class<T> convert){
        Json j = new Json();
        FileHandle file = Gdx.files.local(FilePath);
        if (file == null)
            return null;
        String data = file.readString();

        ArrayList<T> c = j.fromJson(ArrayList.class, convert, data);

        return c;
    }

    private static String Loader(String path){
        FileHandle file = Gdx.files.local(path);
        if (file == null)
            return null;
        return file.readString();
    }

}
