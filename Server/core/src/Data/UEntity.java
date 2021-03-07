package Data;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class UEntity extends Entity {
    //This is so that every entity can be updated
    public void Update(float delta){
        for (Component component: getComponents()) {
            if (component instanceof UComponent)
                ((UComponent) component).Update(delta);
        }
    }
}
