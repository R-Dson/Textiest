package Data.Events.received;

import Data.Events.Event;
import DataShared.Network.UpdatePackageToServer;
import Managers.UI.SocialUI;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import java.util.ArrayList;

public class UpdateFriendsEvent implements Event {

    private final SocialUI socialUI;
    private final ArrayList<String> usernames;

    public UpdateFriendsEvent(SocialUI socialUI, ArrayList<String> usernames)
    {
        this.socialUI = socialUI;
        this.usernames = usernames;
    }

    @Override
    public void eventUpdate(UpdatePackageToServer updatePackageToServer) {
        VisTable friendIgnoreTable = socialUI.getFriendIgnoreTable();
        ScrollPane friendsPane = socialUI.getFriendsPane();
        Group friendGroup = socialUI.getFriendGroup();

        friendIgnoreTable.removeActor(friendsPane);
        friendGroup.clear();

        for (String username : usernames) {
            friendGroup.addActor(new VisTextButton(username)); // sends message
        }
        friendsPane = new ScrollPane(friendGroup);
        friendIgnoreTable.add(friendsPane).expand().fill();
    }
}
