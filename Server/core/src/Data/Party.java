package Data;

import Managers.Network.UserIdentity;

import java.util.ArrayList;

public class Party {
    ArrayList<UserIdentity> userIdentities;

    public Party()
    {
        userIdentities = new ArrayList<>();
    }

    public void addUser(UserIdentity userIdentity)
    {
        userIdentities.add(userIdentity);
        userIdentities.forEach(UserIdentity::updateParty);

        // might not need this?
        userIdentity.addToParty(this);
    }

    public void removeUserByUniqueID(String uID)
    {
        UserIdentity userIdentity = userIdentities.stream().filter(i -> i.UniqueID.equals(uID)).findFirst().get();
        removeUserByUserIdentity(userIdentity);
    }

    public void removeUserByUserIdentity(UserIdentity userIdentity)
    {
        userIdentities.remove(userIdentity);
        userIdentity.removeParty();
        userIdentities.forEach(UserIdentity::updateParty);
    }

    public ArrayList<String> getUserNames()
    {
        ArrayList<String> names = new ArrayList<>();
        userIdentities.forEach(i -> names.add(i.UserName));
        return names;
    }

}
