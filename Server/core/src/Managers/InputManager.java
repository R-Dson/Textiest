package Managers;

import Components.PlayerComponents.B2dBodyComponent;
import Managers.Network.UserIdentity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class InputManager {
    public static void CalculateMovement(UserIdentity ui, ArrayList<Integer> movesArray){
        boolean IsShiftDown = false;
        Vector2 totalMovement = new Vector2(0,0);

        for (Integer integer : movesArray) {
            switch (integer){
                case Input.Keys.SHIFT_LEFT:
                    IsShiftDown = !IsShiftDown;
                    break;
                case Input.Keys.W:
                    totalMovement.y += 1;
                    break;
                case Input.Keys.A:
                    totalMovement.x -= 1;
                    break;
                case Input.Keys.S:
                    totalMovement.y -= 1;
                    break;
                case Input.Keys.D:
                    totalMovement.x += 1;
                    break;
            }
        }
        B2dBodyComponent body = ui.entity.getComponent(B2dBodyComponent.class);
        totalMovement.scl(5,5);
        Vector2 temp = new Vector2(totalMovement.x + body.body.getLinearVelocity().x, totalMovement.y + body.body.getLinearVelocity().y);

        body.body.setLinearVelocity(temp);
        System.out.println(temp);
    }
}
