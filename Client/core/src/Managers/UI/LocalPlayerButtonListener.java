package Managers.UI;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.dialog.ConfirmDialogListener;
import com.kotcrab.vis.ui.util.dialog.Dialogs;

public class LocalPlayerButtonListener extends ChangeListener {
    private final int Whisper = 1;
    private final int Invite = 2;
    private final int AddFriend = 3;
    private final int Ignore = 4;
    private final int Close = 5;

    private String UniqueID;
    private String Name;
    private Stage stage;
    private AreaUI areaUI;

    public LocalPlayerButtonListener(String Name, String UniqueID, Stage stage, AreaUI areaUI)
    {
        this.Name = Name;
        this.UniqueID = UniqueID;
        this.stage = stage;
        this.areaUI = areaUI;
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
                            areaUI.setInvitePartyUniqueID(UniqueID);
                        case AddFriend:
                            areaUI.setAddUniqueID(UniqueID);
                        case Ignore:
                            areaUI.setIgnoreUniqueID(UniqueID);

                    }
                });
    }
}
