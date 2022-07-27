package Hex;

import Components.SpriteRenderer;
import Components.Transform;
import Util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene{
    public LevelEditorScene(){

    }

    @Override
    public void init(){
        this.camera = new Camera(new Vector2f(-250, 0));

        GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("Assets/Sprites/testImage.png")));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(AssetPool.getTexture("Assets/Sprites/testImage2.png")));
        this.addGameObjectToScene(obj2);

        loadResources();
    }

    private void loadResources() {
        AssetPool.getShader("Assets/Shaders/default.glsl");
    }

    @Override
    public void update(float deltaTime) {
        for (GameObject gameObject : gameObjects){
            gameObject.update(deltaTime);
        }

        renderer.render();
    }
}
