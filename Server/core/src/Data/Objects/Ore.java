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

public class Ore extends WorldObject{
    private final Random random = new Random();
    private final int maxNumberOfStones;
    private final int minNumberOfStones;
    private Material oreType;
    private int totalNumberOfStones;
    private int numberOfStonesLeft;

    public Ore(MapLayer mapLayer, int oreID, int respawnTimerMillis, int minNumberOfStones, int maxNumberOfStones)
    {
        super(mapLayer, oreID, respawnTimerMillis);
        this.minNumberOfStones = minNumberOfStones;
        this.maxNumberOfStones = maxNumberOfStones;
        super.setUsable(true);
    }

    public void initOre(ArrayList<Material> oreTypes)
    {
        int treeTypeInt = random.ints(0, oreTypes.size()).findFirst().getAsInt();
        Material oreType = oreTypes.get(treeTypeInt);
        this.oreType = oreType;
        setObjectName(oreType.toString());

        generateNumberOfLogs();
    }

    @Override
    public void reset()
    {
        respawnOre();
    }

    public void respawnOre()
    {
        numberOfStonesLeft = totalNumberOfStones;
        super.setUsable(true);

        Gdx.app.log("STATUS", "Ore respawned.");
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);
        if (getUsable())
            return;
    }

    private void generateNumberOfLogs() {
        totalNumberOfStones = random.ints(minNumberOfStones, maxNumberOfStones).findFirst().getAsInt();
        numberOfStonesLeft = totalNumberOfStones;
    }

    @Override
    public void activity(UserIdentity userIdentity, ObjectActivity objectActivity) {
        super.activity(userIdentity, objectActivity);

        try{
            Item item = ServerClass.getController().getItemManager().getItemFromList(this);

            numberOfStonesLeft--;
            PlayerManager.AddItemToInventory(userIdentity, item);

            Gdx.app.log("STATUS", "Mined " + item.Name);

            if (numberOfStonesLeft <= 0)
            {
                super.setUsable(false);
                Gdx.app.log("STATUS", "Depleted.");

                getMapLayer().sendObjectLayerUpdate();

                objectActivity.removeObjectActivityFromUserIdentity();
            }

        }
        catch (NullPointerException | CloneNotSupportedException e)
        {
            Gdx.app.log("ERROR", "Ore Error.");
        }
    }

    public Material getOreType() {
        return oreType;
    }

    public int getTotalNumberOfStones() {
        return totalNumberOfStones;
    }

    public int getNumberOfLogsStones() {
        return numberOfStonesLeft;
    }

}
