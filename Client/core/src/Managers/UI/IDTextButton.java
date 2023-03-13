package Managers.UI;

import com.kotcrab.vis.ui.widget.VisTextButton;

public class IDTextButton extends VisTextButton {
    private int id;
    public IDTextButton(String text) {
        super(text);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
