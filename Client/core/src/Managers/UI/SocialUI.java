package Managers.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import java.util.ArrayList;

public class SocialUI extends VisTable {

    private VisScrollPane partyPane;
    private VerticalGroup partyGroup;
    private VisTable friendIgnoreTable;
    private VisScrollPane friendsPane;
    private VerticalGroup friendGroup;
    private VisScrollPane ignorePane;
    private VerticalGroup ignoreGroup;

    public SocialUI()
    {
        partyGroup = new VerticalGroup();
        partyPane = new VisScrollPane(partyGroup);

        add(partyPane).expand().fill();

        friendIgnoreTable = new VisTable();

        friendGroup = new VerticalGroup();
        //friendGroup.addActor(new VisLabel("Friends list"));
        friendsPane = new VisScrollPane(friendGroup);

        ignoreGroup = new VerticalGroup();
        ignorePane = new VisScrollPane(ignoreGroup);

        VisTextButton friends = new VisTextButton("Friends List");
        VisTextButton ignore = new VisTextButton("Ignore List");
        friends.setDisabled(true);

        friendIgnoreTable.add(friends);
        friendIgnoreTable.add(ignore);
        friendIgnoreTable.row();

        //friendIgnoreTable.add(friendsPane).expand().fill();

        add(friendIgnoreTable).expand().fill();
    }

    public void updateUsersIgnoreList(ArrayList<String> usernames)
    {
        ignoreGroup.clear();
        for (String username : usernames) {
            ignoreGroup.addActor(new VisTextButton(username)); // shows options
        }
    }

    public VisScrollPane getFriendsPane() {
        return friendsPane;
    }

    public ScrollPane getIgnorePane() {
        return ignorePane;
    }

    public ScrollPane getPartyPane() {
        return partyPane;
    }

    public VerticalGroup getFriendGroup() {
        return friendGroup;
    }

    public VerticalGroup getIgnoreGroup() {
        return ignoreGroup;
    }

    public VerticalGroup getPartyGroup() {
        return partyGroup;
    }

    public VisTable getFriendIgnoreTable() {
        return friendIgnoreTable;
    }
}
