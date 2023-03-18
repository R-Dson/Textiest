package Data;

import DataShared.Network.NetworkMessages.Client.InteractObjectRequest;

import java.util.ArrayList;

public class updatePackageToServerDummy {
    public static ArrayList<Integer> inputsAsIntegers = new ArrayList<>();
    public static int newMapId = -1;
    public static InteractObjectRequest interactObjectType = null;

    public static void resetMapID()
    {
        newMapId = -1;
    }

    public static void resetInputList()
    {
        updatePackageToServerDummy.inputsAsIntegers.clear();
    }

    public static void setNewMapId(int newMapId) {
        updatePackageToServerDummy.newMapId = newMapId;
    }

    public static void setInputsAsIntegers(ArrayList<Integer> inputsAsIntegers) {
        updatePackageToServerDummy.inputsAsIntegers = inputsAsIntegers;
    }

    public static void setInteractObjectType(InteractObjectRequest interactObjectType) {
        updatePackageToServerDummy.interactObjectType = interactObjectType;
    }

    public static InteractObjectRequest getInteractObjectType() {
        return interactObjectType;
    }
}
