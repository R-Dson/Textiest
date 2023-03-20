package Data.Events.received;

import Data.Events.Event;
import DataShared.Network.UpdatePackageToServer;
import Managers.UI.SocialUI;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.kotcrab.vis.ui.widget.VisTextButton;

import java.util.ArrayList;

public class UpdateUserPartyEvent implements Event {
    private final SocialUI socialUI;
    private final ArrayList<String> usernames;
    public UpdateUserPartyEvent(SocialUI socialUI, ArrayList<String> usernames)
    {
        this.socialUI = socialUI;
        this.usernames = usernames;
    }

    @Override
    public void eventUpdate(UpdatePackageToServer updatePackageToServer) {
        VerticalGroup partyGroup = socialUI.getPartyGroup();

        partyGroup.clear();
        for (String username : usernames) {
            partyGroup.addActor(new VisTextButton(username)); // shows options
        }
    }
}
