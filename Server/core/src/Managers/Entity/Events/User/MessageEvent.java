package Managers.Entity.Events.User;

import DataShared.Network.NetworkMessages.Server.ChatMessages;
import DataShared.Network.NetworkMessages.Server.UpdatePackage;
import Managers.Chat.ChatMessage;
import Managers.Entity.UserEvent;

import java.util.ArrayList;

public class MessageEvent implements UserEvent {

    private ArrayList<ChatMessage> messages = new ArrayList<>();

    public MessageEvent(ArrayList<ChatMessage> msgs)
    {
        //messages.addAll(msgs);
        this.messages = msgs;
    }

    @Override
    public void addEventToPackage(UpdatePackage updatePackage) {

        ArrayList<DataShared.Network.NetworkMessages.ChatMessage> msgs = new ArrayList<>();

        for (ChatMessage newMessage : messages) {
            DataShared.Network.NetworkMessages.ChatMessage chatMessage = new DataShared.Network.NetworkMessages.ChatMessage();
            chatMessage.ChatMessage = newMessage.message;
            chatMessage.UserName = newMessage.userIdentity.UserName;
            chatMessage.date = newMessage.date;
            chatMessage.chatEnum = newMessage.chatEnum;
            msgs.add(chatMessage);
        }

        messages.clear();

        ChatMessages cm = new ChatMessages();
        cm.messages = msgs;

        updatePackage.newMessages = cm;
    }

}
