package Hex;

import Components.Sprite;
import Components.SpriteRenderer;
import Components.Spritesheet;
import Components.Transform;
import Util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene{
    public LevelEditorScene(){

    }

    @Override
    public void init(){
        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));

        Spritesheet spritesheet = AssetPool.getSpritesheet("Assets/Sprites/spritesheet.png");

        GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(spritesheet.getSprite(0)));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(spritesheet.getSprite(14)));
        this.addGameObjectToScene(obj2);
    }

    private void loadResources() {
        AssetPool.getShader("Assets/Shaders/default.glsl");
        AssetPool.addSpritesheet("Assets/Sprites/spritesheet.png", new Spritesheet(AssetPool.getTexture("Assets/Sprites/spritesheet.png"),
                16, 16, 26, 0));
    }

    @Override
    public void update(float deltaTime) {
        for (GameObject gameObject : gameObjects){
            gameObject.update(deltaTime);
        }

        renderer.render();
    }
}
