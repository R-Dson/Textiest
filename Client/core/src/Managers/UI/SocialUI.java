package Managers.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.ArrayList;

public class SocialUI extends VisTable {

    private ScrollPane partyPane;
    private VerticalGroup partyGroup;

    private VisTable friendIgnoreTable;
    private ScrollPane friendsPane;
    private VerticalGroup friendGroup;
    private ScrollPane ignorePane;
    private VerticalGroup ignoreGroup;

    public SocialUI()
    {
        partyGroup = new VerticalGroup();
        partyPane = new ScrollPane(partyGroup);

        add(partyPane).expand().fill();


        friendIgnoreTable = new VisTable();

        friendGroup = new VerticalGroup();
        friendsPane = new ScrollPane(friendGroup);

        ignoreGroup = new VerticalGroup();
        ignorePane = new ScrollPane(ignoreGroup);

        friendIgnoreTable.add(friendsPane).expand().fill();

        add(friendIgnoreTable).expand().fill();
    }

    public void updateUsersIgnoreList(ArrayList<String> usernames)
    {
        ignoreGroup.clear();
        for (String username : usernames) {
            ignoreGroup.addActor(new VisLabel(username));
        }
    }

    public void updateUsersFriendsList(ArrayList<String> usernames)
    {
        friendGroup.clear();
        for (String username : usernames) {
            friendGroup.addActor(new VisLabel(username));
        }
    }

    public void updateUsersParty(ArrayList<String> usernames)
    {
        partyGroup.clear();
        for (String username : usernames) {
            partyGroup.addActor(new VisLabel(username));
        }
    }

}
