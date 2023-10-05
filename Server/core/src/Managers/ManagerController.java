package Managers;

import Data.Credential;
import DataShared.Ability.ActionManager;
import Managers.Items.ItemManager;
import Managers.Map.MapManager;
import Managers.Network.NetworkManager;
import Managers.Threads.ThreadManager;
import com.badlogic.gdx.assets.AssetManager;

public class ManagerController {
    private SQLManager SQLManager;
    private Managers.Items.ItemManager ItemManager;
    private DataShared.Ability.ActionManager ActionManager;
    private com.badlogic.gdx.assets.AssetManager AssetManager;
    private Managers.Map.MapManager MapManager;
    private ThreadManager threadManager;
    private NetworkManager networkManager;

    public ManagerController()
    {
        //Initiating managers
        ItemManager = new ItemManager();
        ActionManager = new ActionManager();
        AssetManager = new AssetManager();
        MapManager = new MapManager();
        networkManager = new NetworkManager();

        threadManager = new ThreadManager(MapManager, SQLManager);
    }

    public boolean setupManagers()
    {
        //setups
        try {
            ItemManager.LoadItems();
        } catch (Exception e)
        {
            Logger.log(Logger.ErrorLevel.CRITICAL, "Failed to load item file!");
            return false;
        }
        //ActionManager.LoadActions();
        try {
            MapManager.LoadMaps();
        } catch (Exception e)
        {
            Logger.log(Logger.ErrorLevel.CRITICAL, "Failed to load item file!");
            return false;
        }

        //Loading database credentials
        try{
            Credential credential = FileManager.LoadCredentialFile();
            SQLManager = new SQLManager(credential);
        }
        catch (Exception e)
        {
            Logger.log(Logger.ErrorLevel.CRITICAL, "Failed to load credential file!");
            return false;
        }

        networkManager.startNetworkServer();
        return true;
    }

    public DataShared.Ability.ActionManager getActionManager() {
        return ActionManager;
    }

    public com.badlogic.gdx.assets.AssetManager getAssetManager() {
        return AssetManager;
    }

    public Managers.Items.ItemManager getItemManager() {
        return ItemManager;
    }

    public Managers.Map.MapManager getMapManager() {
        return MapManager;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public Managers.SQLManager getSQLManager() {
        return SQLManager;
    }

    public ThreadManager getThreadManager() {
        return threadManager;
    }

}
