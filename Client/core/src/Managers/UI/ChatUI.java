package Managers.UI;

import DataShared.Network.NetworkMessages.ChatMessage;
import DataShared.Network.NetworkMessages.Client.SendMessage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.*;

import java.util.ArrayList;
import java.util.HashMap;

import static DataShared.ChatEnums.ChatEnum;
import static DataShared.ChatEnums.ChatEnum.*;

public class ChatUI extends VisWindow {
    private ArrayList<SendMessage> toBeSent;
    private ScrollableTextArea currentChatWindow;
    private HashMap<ChatEnum, ArrayList<ChatMessage>> chatHistory;
    private ChatEnum currectChat;
    public ChatUI(String title) {
        super(title);
        buildTabs();
        buildChat();
        buildInput();
        toBeSent = new ArrayList<>();
        chatHistory = new HashMap<>();
        setMovable(false);
        setResizable(true);
        setResizeBorder(5);
    }

    private void buildTabs()
    {
        final Button tabPublic = new VisTextButton("Public");
        tabPublic.setDisabled(true);
        final Button tabParty = new VisTextButton("Party");

        ChangeListener tab_listener = new ChangeListener(){
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                // clear chat when switching
                tabPublic.setDisabled(false);
                tabParty.setDisabled(false);

                if (actor.equals(tabPublic)){
                    currectChat = PUBLIC;
                    generateChatbox();
                    tabPublic.setDisabled(true);
                }
                else if (actor.equals(tabParty)) {
                    currectChat = PARTY;
                    generateChatbox();
                    tabParty.setDisabled(true);
                }
            }
        };

        tabPublic.addListener(tab_listener);
        tabParty.addListener(tab_listener);

        HorizontalGroup groupChat = new HorizontalGroup();

        groupChat.addActor(tabPublic);
        groupChat.addActor(tabParty);
        add(groupChat).fillX().expandX().row();
    }

    private void buildChat()
    {
        currectChat = PUBLIC;
        currentChatWindow = new ScrollableTextArea("Chat");
        currentChatWindow.setDisabled(true);
        add(currentChatWindow).fill().expand().row();
        clearChatWithMessage();
    }

    private void buildInput()
    {
        VisTextField textField = new VisTextField();
        VisTextButton textButton = new VisTextButton(">");
        textButton.setWidth(50);

        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SendMessage sm = new SendMessage();
                sm.Message = textField.getText();
                sm.chatEnum = currectChat;
                textField.clearText();
                toBeSent.add(sm);
            }
        });

        add(textField).fillX().expandX();
        add(textButton).fillX();
    }
    private final int CHAT_HISTORY_LIMIT = 500;
    public void appendMessage(ChatMessage message)
    {
        if (chatHistory.get(message.chatEnum) == null)
            chatHistory.put(message.chatEnum, new ArrayList<>());

        if (chatHistory.get(message.chatEnum).size() >= CHAT_HISTORY_LIMIT)
            chatHistory.get(message.chatEnum).remove(chatHistory.get(message.chatEnum).size()-1);

        chatHistory.get(message.chatEnum).add(0, message);

        generateChatbox();
    }

    private void generateChatbox()
    {
        clearChatWithMessage();
        if (chatHistory.get(currectChat) == null)
            return;
        for (ChatMessage chatMessage : chatHistory.get(currectChat)) {
            String text = GenerateString(chatMessage, true);
            currentChatWindow.appendText(" \n" + text);
        }
    }

    private String GenerateString(ChatMessage message, boolean showTime)
    {
        String msg = message.UserName + ": " + message.ChatMessage;
        if (showTime)
            msg = "[" + message.date.toString() + "] " + msg;
        return msg;
    }

    public void clearChatWithMessage()
    {
        switch (currectChat) {
            case PUBLIC:
                currentChatWindow.setText("You joined public chat.");
                break;
            case PARTY:
                currentChatWindow.setText("You joined party chat.");
                break;
        }

    }

    public ArrayList<SendMessage> getToBeSent() {
        return toBeSent;
    }
    public void clearMessages()
    {
        toBeSent.clear();
    }
}
