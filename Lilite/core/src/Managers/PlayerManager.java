package Managers;

import DataShared.Player.PlayerData;
import com.badlogic.gdx.math.Vector2;

public class PlayerManager {
    public static PlayerData playerData;

    public static Vector2 GetPosition(){
        return new Vector2(playerData.x, playerData.y);
    }
}
