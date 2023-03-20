package Managers.UI;

import Data.Events.Send.AddUserEvent;
import Data.Events.Send.IgnoreUserEvent;
import Data.Events.Send.InvitePartyUniqueIDEvent;
import Managers.Scenes.MainScene;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.dialog.Dialogs;

public class LocalPlayerButtonListener extends ChangeListener {
    private final int Whisper = 1;
    private final int Invite = 2;
    private final int AddFriend = 3;
    private final int Ignore = 4;
    private final int Close = 5;

    private final String UniqueID;
    private final String Name;
    private final Stage stage;
    private final  MainScene mainScene;

    public LocalPlayerButtonListener(String Name, String UniqueID, MainScene mainScene, Stage stage)
    {
        this.Name = Name;
        this.UniqueID = UniqueID;
        this.stage = stage;
        this.mainScene = mainScene;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        Dialogs.showConfirmDialog(
                stage,
                Name,
                "Options",
                new String[]{"Whisper", "Invite", "Add To Friends", "Add To Ignore", "Close"},
                new Integer[]{Whisper, Invite, AddFriend, Ignore, Close},
                result -> {
                    switch (result){
                        case Whisper:
                            // Whisper
                        case Invite:
                            mainScene.addEvent(new InvitePartyUniqueIDEvent(UniqueID));
                        case AddFriend:
                            mainScene.addEvent(new AddUserEvent(UniqueID));
                        case Ignore:
                            mainScene.addEvent(new IgnoreUserEvent(UniqueID));

                    }
                });
    }
}
