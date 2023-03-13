package Managers.UI;

import Managers.PlayerManager;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

public class CombatUI extends VisTable {
    private ChatacterTable characterTable;
    public CombatUI(boolean vertical)
    {
        super(vertical);
        characterTable = new ChatacterTable();

        add(characterTable);
        //this.setHeight(this.getPrefHeight()/2);
    }
}
