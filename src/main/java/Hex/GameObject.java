package Hex;

import Components.Component;
import Components.ComponentDeserializer;
import Components.SpriteRenderer;
import Components.Transform;
import Util.AssetPool;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid = -1;
    public String name;
    public transient Transform transform;
    private List<Component> components;

    private boolean doSerialization = true;
    private boolean isDead = false;

    public GameObject(String name) {
        this.name = name;
        components = new ArrayList<>();

        this.uid = ID_COUNTER++;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components){
            if (componentClass.isAssignableFrom(c.getClass())){
                try {
                    return componentClass.cast(c);
                }catch (ClassCastException e){
                    e.printStackTrace();
                    assert false : "Error: Casting Component";
                }
            }
        }

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++){ //TODO FIX do it with Iterator
            Component c = components.get(i);
            if(componentClass.isAssignableFrom(c.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {
        c.generateId();
        components.add(c);
        c.gameObject = this;
    }

    public List<Component> getAllComponents() {
        return this.components;
    }

    public void imGui() {
        for (Component component : components) {
            if (ImGui.collapsingHeader(component.getClass().getSimpleName())){
                component.imGui();
            }
        }
    }

    public void destroy() {
        this.isDead = true;
        for (int i=0; i < components.size(); i++) {
            components.get(i).destroy();
        }
    }

    public boolean isDead() {
        return this.isDead;
    }

    public void start() {
        for (int i = 0; i < components.size(); i++){
            components.get(i).start();
        }
    }

    public void update(float deltaTime) {
        for (int i = 0; i < components.size(); i++){
            components.get(i).update(deltaTime);
        }
    }

    public void editorUpdate(float deltaTime) {
        for (int i=0; i < components.size(); i++) {
            components.get(i).editorUpdate(deltaTime);
        }
    }

    public GameObject copy() {
        //TODO Cleaner solution
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
        String objAsJson = gson.toJson(this);
        GameObject obj = gson.fromJson(objAsJson, GameObject.class);

        obj.generateUid();
        for (Component c : obj.getAllComponents()) {
            c.generateId();
        }

        SpriteRenderer sprite = obj.getComponent(SpriteRenderer.class);
        if (sprite != null && sprite.getTexture() != null) {
            sprite.setTexture(AssetPool.getTexture(sprite.getTexture().getFilePath()));
        }

        return obj;
    }

    private void generateUid() {
        this.uid = ID_COUNTER++;
    }

    public int uid() {
        return this.uid;
    }

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }

    public void setNoSerialize() {
        this.doSerialization = false;
    }

    public boolean doSerialization() {
        return this.doSerialization;
    }
}
