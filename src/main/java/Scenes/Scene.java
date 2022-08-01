package Scenes;

import Components.Component;
import Components.ComponentDeserializer;
import Hex.Camera;
import Hex.GameObject;
import Hex.GameObjectDeserializer;
import Renderer.Renderer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Scene {
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected Renderer renderer = new Renderer();
    protected boolean levelLoaded = false;

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
    public abstract void render();

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

    public void imGui(){

    }

    public void saveExit() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            FileWriter writer = new FileWriter("level.txt");
            List<GameObject> gameObjectToSerialize = new ArrayList<>();
            for (GameObject obj : this.gameObjects) {
                if (obj.doSerialization()) {
                    gameObjectToSerialize.add(obj);
                }
            }
            writer.write(gson.toJson(gameObjectToSerialize));
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            int maxCompId = -1;
            int maxGoId = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i=0; i < objs.length; i++) {
                addGameObjectToScene(objs[i]);
                for (Component c : objs[i].getAllComponents()) {
                    if (c.uid() > maxCompId) {
                        maxCompId = c.uid();
                    }
                }
                if (objs[i].uid() > maxGoId) {
                    maxGoId = objs[i].uid();
                }
            }

            maxCompId++;
            maxGoId++;
            Component.init(maxCompId);
            GameObject.init(maxGoId);
            levelLoaded = true;
        }
    }

    public GameObject getGameObject(int gameObjectId) {
        Optional<GameObject> result = this.gameObjects.stream().filter(gameObject -> gameObject.uid() == gameObjectId).findFirst();
        return result.orElse(null);
    }
}
