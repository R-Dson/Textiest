package Data.Events.received;

import Data.Events.Event;
import DataShared.Network.UpdatePackageToServer;
import DataShared.Player.PlayerData;
import Managers.Scenes.MainScene;
import Managers.UI.AreaUI;
import Managers.UI.LocalPlayerButtonListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import java.util.ArrayList;

public class UpdateOtherUsersEvent implements Event {
    private final ArrayList<PlayerData> others;
    private final AreaUI areaUI;
    private final Stage stage;
    private final MainScene mainScene;

    public UpdateOtherUsersEvent(AreaUI areaUI, ArrayList<PlayerData> others, MainScene mainScene, Stage stage)
    {
        this.areaUI = areaUI;
        this.others = others;
        this.mainScene = mainScene;
        this.stage = stage;
    }

    @Override
    public void eventUpdate(UpdatePackageToServer updatePackageToServer) {
        if (others == null)
            return;

        VisScrollPane userPanel = areaUI.getUserPanel();
        VisTable usersTable = areaUI.getUsersTable();

        if (userPanel != null && userPanel.getChildren().size < 2 && others.size() == 0)
            return;

        usersTable.clear();

        VerticalGroup group = new VerticalGroup();


        for (PlayerData pd : others) {
            VisTextButton visTextButton = new VisTextButton(pd.UserName);

            visTextButton.addListener(new LocalPlayerButtonListener(pd.UserName, pd.UniqueUserID, mainScene, stage));

            group.addActor(visTextButton);

        }

        userPanel = new VisScrollPane(group);
        userPanel.setFadeScrollBars(false);
        usersTable.add(userPanel).fillY().expandY();
    }
}
