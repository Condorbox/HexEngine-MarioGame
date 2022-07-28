package Hex;

import Renderer.Renderer;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected Renderer renderer = new Renderer();
    protected GameObject activeGameObject = null;

    public Scene(){

    }

    public void init(){

    }

    public void start(){
        for (GameObject gameObject : gameObjects){
            gameObject.start();
            this.renderer.add(gameObject);
        }
        isRunning = true;
    }

    public abstract void update(float deltaTime);

    public void addGameObjectToScene(GameObject gameObject){
        if(!isRunning){
            gameObjects.add(gameObject);
        }else {
            gameObjects.add(gameObject);
            gameObject.start();
            this.renderer.add(gameObject);
        }
    }

    public Camera camera(){
        return camera;
    }

    public void sceneImGui(){
        if (activeGameObject != null){
            ImGui.begin("Inspector");
            activeGameObject.imGui();
            ImGui.end();
        }

        imGui();
    }

    public void imGui(){

    }
}
