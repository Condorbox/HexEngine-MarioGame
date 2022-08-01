package Hex;

import Components.Component;
import Components.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid = -1;
    private String name;
    public Transform transform;
    private List<Component> components;

    private boolean doSerialization = true;

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.transform = transform;
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
            component.imGui();
        }
    }

    public void start() {
        for (Component c : components) {
            c.start();
        }
    }

    public void update(float deltaTime) {
        for (Component c : components) {
            c.update(deltaTime);
        }
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
