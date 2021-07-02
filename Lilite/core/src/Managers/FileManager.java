package Managers;

import DataShared.Files.FileManagerShared;
import Managers.Input.KeyBind;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public static List<KeyBind> GetKeyBindings(String FilePath){
        return FileManagerShared.getList(FilePath, KeyBind.class);
    }
}
