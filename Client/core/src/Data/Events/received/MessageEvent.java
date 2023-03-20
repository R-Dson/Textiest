package Data.Events.received;

import Data.Events.Event;
import DataShared.Network.NetworkMessages.ChatMessage;
import DataShared.Network.NetworkMessages.Server.ChatMessages;
import DataShared.Network.UpdatePackageToServer;
import Managers.UI.ChatUI;

public class MessageEvent implements Event {
    private final ChatMessages chatMessages;
    private final ChatUI chatWindow;

    public MessageEvent(ChatMessages chatMessages, ChatUI chatWindow)
    {
        this.chatMessages = chatMessages;
        this.chatWindow = chatWindow;
    }

    @Override
    public void eventUpdate(UpdatePackageToServer updatePackageToServer) {
        if (chatMessages == null)
            return;

        for (ChatMessage message : chatMessages.messages) {
            chatWindow.appendMessage(message);
        }
    }
}
