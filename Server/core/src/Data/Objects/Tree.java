package Data.Objects;

import DataShared.Item.Item;
import DataShared.Item.Material;
import Managers.Map.MapLayer;
import Managers.Network.UserIdentity;
import Managers.PlayerManager;
import com.badlogic.gdx.Gdx;
import com.vaniljstudio.server.ServerClass;

import java.util.ArrayList;
import java.util.Random;

public class Tree extends WorldObject{
    private final Random random = new Random();
    private final int maxNumberOfLogs;
    private final int minNumberOfLogs;
    private Material treeType;
    private int totalNumberOfLogs;
    private int numberOfLogsLeft;

    public Tree(MapLayer mapLayer, int TreeID, int respawnTimerMillis, int minNumberOfLogs, int maxNumberOfLogs)
    {
        super(mapLayer, TreeID, respawnTimerMillis);
        this.minNumberOfLogs = minNumberOfLogs;
        this.maxNumberOfLogs = maxNumberOfLogs;
        super.setUsable(true);
    }

    public void initTree(ArrayList<Material> treeTypes)
    {
        int treeTypeInt = random.ints(0, treeTypes.size()).findFirst().getAsInt();
        Material treeType = treeTypes.get(treeTypeInt);
        this.treeType = treeType;
        setObjectName(treeType.toString());

        generateNumberOfLogs();
    }

    public void respawnTree()
    {
        numberOfLogsLeft = totalNumberOfLogs;
        super.setUsable(true);

        Gdx.app.log("STATUS", "Tree respawned.");
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        if (getUsable())
            return;

        if (getTimer() >= this.getRespawnTimerMillis()/1000f)
            respawnTree();
    }

    private void generateNumberOfLogs() {
        totalNumberOfLogs = random.ints(minNumberOfLogs, maxNumberOfLogs+1).findFirst().getAsInt();
        numberOfLogsLeft = totalNumberOfLogs;
    }

    @Override
    public void activity(UserIdentity userIdentity, ObjectActivity objectActivity)
    {
        super.activity(userIdentity, objectActivity);
        try{
            Item item = ServerClass.ItemManager.getItemFromList(this);
            numberOfLogsLeft--;
            PlayerManager.AddItemToInventory(userIdentity, item);

            Gdx.app.log("STATUS", "Cut log " + getObjectName());

            if (numberOfLogsLeft <= 0)
            {
                super.setUsable(false);
                Gdx.app.log("STATUS", "Depleted.");

                getMapLayer().sendObjectLayerUpdate();

                objectActivity.removeObjectActivityFromUserIdentity();
            }

        }
        catch (CloneNotSupportedException | NullPointerException e)
        {
            Gdx.app.log("ERROR", "Tree Error.");
        }

    }

    public boolean ChopLog(UserIdentity userIdentity)
    {
        try{
            Item item = ServerClass.ItemManager.getItemFromList(this);
            numberOfLogsLeft--;
            PlayerManager.AddItemToInventory(userIdentity, item);

            Gdx.app.log("STATUS", "Cut log " + getObjectName());
            if (numberOfLogsLeft <= 0)
            {
                super.setUsable(false);
                return true;
            }
        }
        catch (CloneNotSupportedException | NullPointerException e)
        {

        }
        return false;

    }

    public int getTotalNumberOfLogs() {
        return totalNumberOfLogs;
    }

    public int getNumberOfLogsLeft() {
        return numberOfLogsLeft;
    }

}
