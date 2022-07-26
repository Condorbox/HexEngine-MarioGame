package Hex;

import Components.Component;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String name;
    private List<Component> components;

    public GameObject(String name){
        this.name = name;
        components = new ArrayList<>();
    }

    public <T extends Component> T getComponent(Class<T> componentClass){
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

    public <T extends Component> void removeComponent(Class<T> componentClass){
        for (int i = 0; i < components.size(); i++){ //TODO FIX do it with Iterator
            Component c = components.get(i);
            if(componentClass.isAssignableFrom(c.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c){
        components.add(c);
        c.gameObject = this;
    }

    public void start(){
        for (Component c : components){
            c.start();
        }
    }

    public void update(float deltaTime){
        for (Component c : components){
            c.update(deltaTime);
        }
    }
}