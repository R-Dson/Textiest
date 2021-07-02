package DataShared.Files;

import DataShared.Ability.Ability;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.List;

public class FileManagerShared {
    public static List<Ability> GetAbilities(String FilePath){
        return getList(FilePath, Ability.class);
    }

    public static <T> ArrayList<T> getList(String FilePath, Class<T> convert){
        Json j = new Json();
        FileHandle file = Gdx.files.local(FilePath);
        if (file == null)
            return null;
        String data = file.readString();

        ArrayList<T> c = j.fromJson(ArrayList.class, convert, data);

        return c;
    }
}
