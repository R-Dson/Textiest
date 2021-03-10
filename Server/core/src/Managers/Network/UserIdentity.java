package Managers.Network;

import Components.Entities.PlayerEntity;
import Components.PlayerComponents.B2dBodyComponent;
import Data.FixedValues;
import Data.PlayerData;
import Data.UpdatePackage;
import Managers.Map.Map;
import Managers.Map.MapLayer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.vaniljstudio.server.ServerClass;

public class UserIdentity extends Entity {
    public String UniqueID;
    public int connectionID;
    public PlayerEntity entity;
    public PlayerData playerData;
    public MapLayer currentLayer;
    public Map currentMap;
    public String UserName;

    private float timer = 0;

    public UserIdentity(String UniqueID, int connectionID){
        this.UniqueID = UniqueID;
        this.connectionID = connectionID;
    }

    public void RemoveUserIdentity(){
        Body body = entity.getComponent(B2dBodyComponent.class).body;
        if (body != null)
            currentMap.world.destroyBody(body);
        currentLayer.RemoveUserFromLayer(this);
        currentMap = null;
    }

    public void Update(float delta) {
        timer += delta;
        //MAKE THIS 1/20 later
        if (timer >= FixedValues.UpdateFrequency30)
        {
            //Updates playerData position to the physical body position
            UpdatePlayerPositionData();

            //Creates new package with data
            UpdatePackage UpdatePackage = new UpdatePackage();
            //Sets data
            UpdatePackage.recieverData = playerData;
            FillOtherUsers(UpdatePackage);

            ServerClass.GameServer.getServer().sendToTCP(connectionID, UpdatePackage);
            System.out.println("Sent data to " + connectionID);

            timer -= FixedValues.UpdateFrequency30;
        }

        //TODO SEND INFORMATION TO USER

    }

    private void UpdatePlayerPositionData(){
        Body body = entity.getComponent(B2dBodyComponent.class).body;
        playerData.PlayerPosition = body.getPosition();
    }

    private void FillOtherUsers(UpdatePackage UpdatePackage){
        if(currentLayer != null){
            for (UserIdentity userIdentity : currentLayer.users) {
                //TODO CHANGE DISTANCE
                if (userIdentity.playerData.PlayerPosition.len() < 2000 && connectionID != userIdentity.connectionID)
                {
                    UpdatePackage.OtherPlayers.add(userIdentity.playerData);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserIdentity)
            return ((UserIdentity) o).connectionID == this.connectionID;
        return false;
    }
}
