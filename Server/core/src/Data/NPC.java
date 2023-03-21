package Data;

import Data.Combat.CombatActivity;
import Managers.Network.UserIdentity;

import java.util.ArrayList;
import java.util.HashSet;

public class NPC {

    private final String name;
    private final int maxHP;
    public int hp;
    private CombatActivity combatActivity;
    private HashSet<UserIdentity> usersInCombat;
    private UserIdentity target;

    public NPC(String name, int maxHP)
    {
        this.name = name;
        this.maxHP = maxHP;

        hp = maxHP;
    }

    public void Update()
    { }

    public void setTarget(UserIdentity target) {
        this.target = target;
    }

    public UserIdentity getTarget() {
        return target;
    }

    public CombatActivity getCombatActivity() {
        return combatActivity;
    }

    public void setCombatActivity(CombatActivity combatActivity) {
        this.combatActivity = combatActivity;
    }

    public void addUser(UserIdentity userIdentity)
    {
        usersInCombat.add(userIdentity);
    }

    public void removeUser(UserIdentity userIdentity)
    {
        usersInCombat.remove(userIdentity);
    }
}
