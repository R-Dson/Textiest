package Managers.Chat;

import DataShared.ChatEnums;
import Managers.Network.UserIdentity;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Date;

public class ChatMessage {
    //time
    //sender
    //message
    //ID
    public UserIdentity userIdentity;
    public String message;
    public Date date;
    public ChatEnums.ChatEnum chatEnum;
    public ChatMessage(String message, UserIdentity userIdentity, ChatEnums.ChatEnum chatEnum)
    {
        this.message = message;
        this.userIdentity = userIdentity;
        date = new Date(TimeUtils.millis());
        this.chatEnum = chatEnum;
    }
}
