package Data.Combat;

import Data.NPC;
import Managers.Network.UserIdentity;

public class Combat {

    private float timerPlayer = 0;
    private float timerNPC = 0;

    private UserIdentity player;
    private NPC npc;

    public Combat(UserIdentity player, NPC npc)
    {
        this.player = player;
        this.npc = npc;
    }

    public void Update(float delta)
    {
        timerNPC += delta;
        timerNPC += delta;

        // TODO: GET ATTACK TIME

        if (timerNPC > 100)
        {
            updateNPC();
            timerNPC -= 100;
        }

        if (timerPlayer > 100)
        {
            updatePlayer();
            timerPlayer -= 100;
        }

    }

    private void updateNPC()
    {

    }

    private void updatePlayer()
    {

    }

}
