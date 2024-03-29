package DataShared.Network;

import DataShared.ChatEnums;
import DataShared.Item.EquipmentItem;
import DataShared.Item.Item;
import DataShared.Item.ItemEnums;
import DataShared.Item.Material;
import DataShared.MapType;
import DataShared.Network.NetworkMessages.Client.*;
import DataShared.Network.NetworkMessages.Client.Chat.InteractWithNPC;
import DataShared.Network.NetworkMessages.Client.Chat.SendMessage;
import DataShared.Network.NetworkMessages.Server.*;
import DataShared.Network.NetworkMessages.ErrorEnum;
import DataShared.Player.PlayerData;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import DataShared.Network.NetworkMessages.*;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class NetworkManager {
    public static void Register (final Kryo kryo){
        //final Kryo kryo = endPoint.getKryo();
        kryo.register(NetworkPackage.class);
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
        kryo.register(SendMessage.class);
        kryo.register(ChatMessage.class);
        kryo.register(ChatMessages.class);
        kryo.register(Date.class);
        kryo.register(ChatEnums.class);
        kryo.register(ChatEnums.ChatEnum.class);
        kryo.register(ChangeMapFromServer.class);
        kryo.register(ChangeMapFromClient.class);
        kryo.register(MapType.class);
        kryo.register(SentWorldObject.class);
        kryo.register(Item.class);
        kryo.register(DataShared.Item.Item[].class);
        kryo.register(InteractObjectRequest.class);
        kryo.register(PlayerStatus.InteractObjectType.class);
        kryo.register(ItemEnums.class);
        kryo.register(ItemEnums.ItemRarity.class);
        kryo.register(ItemEnums.ItemType.class);
        kryo.register(ItemEnums.EquipmentType.class);
        kryo.register(ItemEnums.WeaponItemType.class);
        kryo.register(Material.class);
        kryo.register(PlayerStatus.class);
        kryo.register(UpdateObjects.class);
        kryo.register(UpdateFriends.class);
        kryo.register(UpdateParty.class);
        kryo.register(UpdateIgnore.class);
        kryo.register(UpdateParty.class);
        kryo.register(UpdateFriends.class);
        kryo.register(UpdateIgnore.class);
        kryo.register(AddUserRequest.class);
        kryo.register(PartyInviteRequest.class);
        kryo.register(IgnoreUserRequest.class);
        kryo.register(HashSet.class);
        kryo.register(InteractWithNPC.class);
    }
}
