package Managers;

import Managers.Networking.NetworkManager;

public class Managers {

    private final DataManager DataManager;
    private final NetworkManager networkManager;
    private CameraManager cameraManager;

    public Managers()
    {
        DataManager = new DataManager();
        networkManager = new NetworkManager();
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public DataManager getDataManager() {
        return DataManager;
    }
}
