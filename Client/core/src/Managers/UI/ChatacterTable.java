package Managers.UI;

import DataShared.Player.PlayerData;
import Managers.PlayerManager;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

public class ChatacterTable extends VisTable {

    public ChatacterTable()
    {
        PlayerData pd = PlayerManager.playerData;
        add(new VisLabel("HP: " + pd.CurrentHealth + "/" + pd.MaxHealth)).fillX().expandX();
    }
}
