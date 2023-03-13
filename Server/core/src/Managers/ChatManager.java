package Managers;

import Managers.Chat.ChatMessage;
import Managers.Network.UserIdentity;

import java.util.ArrayList;
import java.util.Collection;

public class ChatManager {
    ArrayList<ChatMessage> chatHistory;
    ArrayList<ChatMessage> newMessages;
    public ChatManager()
    {
        chatHistory = new ArrayList<>();
        newMessages = new ArrayList<>();
    }

    public void addMessagesToUsers(Collection<UserIdentity> users)
    {
        for (UserIdentity identity : users) {
            identity.addPlayerMessages(newMessages);
        }
        newMessages.clear();
    }

    public void appendChatMessage(ChatMessage cm)
    {
        newMessages.add(cm);
    }

    public ArrayList<ChatMessage> getNewMessages() {
        return newMessages;
    }
}
