package Managers.Network;

import com.badlogic.gdx.utils.Queue;

import java.util.ArrayList;

public class RecievedManager {

    // MOVEMENT
    private static Queue<MovementRequest> movementRequests = new Queue<>();

    public static void AddMovementRequest(UserIdentity userIdentity, ArrayList<Integer> movementList){
        if (!movementList.isEmpty())
            movementRequests.addLast(new MovementRequest(userIdentity, movementList));
    }

    public static MovementRequest GetMovementRequest(){
        return movementRequests.removeFirst();
    }

    public static MovementRequest PeekMovementRequest(){
        return movementRequests.first();
    }

    public static boolean HasMovementRequest(){
        return !movementRequests.isEmpty();
    }

    //MORE HERE

    public static class MovementRequest{

        UserIdentity userIdentity;
        ArrayList<Integer> movementList;

        public MovementRequest(UserIdentity userIdentity, ArrayList<Integer> movementList){
            this.userIdentity = userIdentity;
            this.movementList = movementList;
        }
    }
}

