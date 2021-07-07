package DataShared.Files;

import DataShared.Ability.Ability;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

public class FileManagerShared {
    public static ArrayList GetAbilities(String FilePath){
        return getList(FilePath, Ability.class);
    }

    public static <T> ArrayList getList(String FilePath, Class<T> convert){
        Json j = new Json();
        FileHandle file = Gdx.files.local(FilePath);
        if (file == null)
            return null;
        String data = file.readString();

        return j.fromJson(ArrayList.class, convert, data);
    }
}
