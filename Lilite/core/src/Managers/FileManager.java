package Managers;

import DataShared.Files.FileManagerShared;
import Managers.Input.KeyBind;

import java.util.ArrayList;

public class FileManager {

    public static ArrayList GetKeyBindings(String FilePath){
        return FileManagerShared.getList(FilePath, KeyBind.class);
    }
}
