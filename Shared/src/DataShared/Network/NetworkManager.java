package DataShared.Network;

import DataShared.Item.EquipmentItem;
import DataShared.Player.PlayerData;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import DataShared.Network.NetworkMessages.*;
import java.util.ArrayList;

public class NetworkManager {
    public static void Register (Kryo kryo){
        kryo.register(ConnectionEstablished.class);
        kryo.register(LoginRequest.class);
        kryo.register(LoginError.class);
        kryo.register(ErrorEnum.class);
        kryo.register(RegisterRequest.class);
        kryo.register(PlayerData.class);
        kryo.register(UpdatePackage.class);
        kryo.register(ArrayList.class);
        kryo.register(EquipmentItem[].class);
        kryo.register(Vector2.class);
        kryo.register(LoginResult.class);
        kryo.register(LoginEnum.class);
        kryo.register(UpdatePackageToServer.class);
        kryo.register(SceneNameEnum.class);
        kryo.register(CreationRequest.class);
        kryo.register(ChangeScene.class);
        kryo.register(AssignRequest.class);
    }
}
