package Hex;

import Components.SpriteRenderer;
import Components.Spritesheet;
import Components.Transform;

import Util.AssetPool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import org.joml.Vector2f;

public class LevelEditorScene extends Scene{
    private GameObject obj1;
    private Spritesheet spritesheet;
    public LevelEditorScene(){

    }

    @Override
    public void init(){
        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));

        spritesheet = AssetPool.getSpritesheet("Assets/Sprites/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(300, 100), new Vector2f(256, 256)));
        SpriteRenderer obj1SpriteRender = new SpriteRenderer();
        obj1SpriteRender.setSprite(spritesheet.getSprite(0));
        obj1SpriteRender.setZIndex(10);
        obj1.addComponent(obj1SpriteRender);
        addGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        SpriteRenderer obj2SpriteRender = new SpriteRenderer();
        obj2SpriteRender.setSprite(spritesheet.getSprite(14));
        obj2.addComponent(obj2SpriteRender);
        addGameObjectToScene(obj2);

        /*Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String serialized = gson.toJson(obj1);
        System.out.println(serialized);
        GameObject obj = gson.fromJson(serialized, GameObject.class);
        System.out.println(obj);*/
    }

    private void loadResources() {
        AssetPool.getShader("Assets/Shaders/default.glsl");
        AssetPool.addSpritesheet("Assets/Sprites/spritesheet.png", new Spritesheet(AssetPool.getTexture("Assets/Sprites/spritesheet.png"),
                16, 16, 26, 0));
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = spriteFlipTime;

    @Override
    public void update(float deltaTime) {

        spriteFlipTimeLeft -= deltaTime;

        if (spriteFlipTimeLeft <= 0){
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex > 4){
                spriteIndex = 0;
            }
            obj1.getComponent(SpriteRenderer.class).setSprite(spritesheet.getSprite(spriteIndex));
        }

        for (GameObject gameObject : gameObjects){
            gameObject.update(deltaTime);
        }

        renderer.render();
    }

    @Override
    public void imGui() {
        ImGui.begin("Test Window");
        ImGui.text("Some Random text");
        ImGui.end();
    }
}
