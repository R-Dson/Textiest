package Managers.Entity.Events.Layer;

import Managers.ChatManager;
import Managers.Entity.LayerEvent;
import Managers.Network.UserIdentity;

import java.util.Collection;

public class LayerMessageEvent implements LayerEvent {
    private ChatManager chatManager;
    private Collection<UserIdentity> userIdentities;

    public LayerMessageEvent(ChatManager chatManager, Collection<UserIdentity> userIdentities)
    {
        this.chatManager = chatManager;
        this.userIdentities = userIdentities;
    }

    @Override
    public void eventUpdate() {
        chatManager.addMessagesToUsers(userIdentities);
    }
}
