package Managers.UI;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisSplitPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;

public class SplitWindow extends VisWindow {

    private VisSplitPane splitPane;
    private VisTable left, right;
    private String username;
    private boolean isDefaultTitle;
    public SplitWindow (String title, boolean isVertical) {
        super(title);
        username = title;

        left = new VisTable(true);
        right = new VisTable(true);

        splitPane = new VisSplitPane(left, right, isVertical);
        add(splitPane).fill().expand();
        setMovable(false);
        setResizable(false);
        isDefaultTitle = true;
       // setResizeBorder(5);
    }

    public SplitWindow (String title, boolean isVertical, VisTable first, VisTable second) {
        super(title);
        username = title;

        splitPane = new VisSplitPane(first, second, isVertical);
        add(splitPane).fill().expand();
        setMovable(false);
        setResizable(false);
        setResizeBorder(5);
    }

    public void updateTitle(String appendText)
    {
        getTitleLabel().setText(username + " \t " + appendText);
        isDefaultTitle = false;
    }

    public boolean isDefaultTitle() {
        return isDefaultTitle;
    }

    public void resetDefaultTitle()
    {
        updateTitle("");
        isDefaultTitle = true;
    }

    public VisSplitPane getSplitPane() {
        return splitPane;
    }

    public VisTable getLeftTable() {
        return left;
    }

    public VisTable getRightTable() {
        return right;
    }

    public VisTable getTopTable() {
        return left;
    }

    public VisTable getBottomTable() {
        return right;
    }
}
