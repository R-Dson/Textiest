package Managers;

import Managers.Ability.Ability;
import Managers.Input.KeyBind;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public static List<KeyBind> GetKeyBindings(String FilePath){
        return getList(FilePath, KeyBind.class);
    }

    public static List<Ability> GetAbilities(String FilePath){
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
}
