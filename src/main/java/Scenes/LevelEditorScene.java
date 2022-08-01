package Scenes;

import Components.*;

import Hex.Camera;
import Hex.GameObject;
import Hex.Prefabs;
import Hex.Window;
import Renderer.DebugDraw;
import Renderer.Texture;
import Scenes.Scene;
import Util.AssetPool;

import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class LevelEditorScene extends Scene {
    private GameObject obj1;
    private Spritesheet entitySpritesheet;
    private Spritesheet spritesheet;

   GameObject levelEditorComponents = new GameObject("Level Editor", new Transform(new Vector2f()));

    public LevelEditorScene(){

    }

    @Override
    public void init(){
        loadResources();
        Spritesheet gizmos = AssetPool.getSpritesheet("Assets/Sprites/gizmos.png");
        this.camera = new Camera(new Vector2f(-250, 0));
        levelEditorComponents.addComponent(new MouseControls());
        levelEditorComponents.addComponent(new GridLines());
        levelEditorComponents.addComponent(new EditorCamera(camera));
        levelEditorComponents.addComponent(new TranslateGizmo(gizmos.getSprite(1), Window.getImGuiLayer().getPropertiesWindow())); //TODO Event System to change this

        levelEditorComponents.start();

        if (levelLoaded) {
            return;
        }

        /*obj1 = new GameObject("Object 1", new Transform(new Vector2f(300, 100), new Vector2f(256, 256)));
        SpriteRenderer obj1SpriteRender = new SpriteRenderer();
        obj1SpriteRender.setSprite(entitySpritesheet.getSprite(0));
        obj1SpriteRender.setZIndex(10);
        obj1.addComponent(obj1SpriteRender);
        obj1.addComponent(new Rigidbody());
        addGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        SpriteRenderer obj2SpriteRender = new SpriteRenderer();
        obj2SpriteRender.setSprite(entitySpritesheet.getSprite(14));
        obj2.addComponent(obj2SpriteRender);
        addGameObjectToScene(obj2);*/
    }

    private void loadResources() {
        AssetPool.getShader("Assets/Shaders/default.glsl");
        AssetPool.addSpritesheet("Assets/Sprites/spritesheet.png", new Spritesheet(AssetPool.getTexture("Assets/Sprites/spritesheet.png"),
                16, 16, 26, 0));
        AssetPool.addSpritesheet("Assets/Sprites/decorationsAndBlocks.png", new Spritesheet(AssetPool.getTexture("Assets/Sprites/decorationsAndBlocks.png"),
                16, 16, 81, 0));
        AssetPool.addSpritesheet("Assets/Sprites/gizmos.png", new Spritesheet(AssetPool.getTexture("Assets/Sprites/gizmos.png"),
                24, 48, 2, 0));
        entitySpritesheet = AssetPool.getSpritesheet("Assets/Sprites/spritesheet.png");
        spritesheet = AssetPool.getSpritesheet("Assets/Sprites/decorationsAndBlocks.png");

        for (GameObject g : gameObjects) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilePath()));
                }
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        levelEditorComponents.update(deltaTime);
        camera.adjustProjection();

        for (GameObject gameObject : gameObjects){
            gameObject.update(deltaTime);
        }
    }

    @Override
    public void render(){
        renderer.render();
    }

    @Override
    public void imGui() {
        ImGui.begin("Level Editor Stuff");
        levelEditorComponents.imGui();
        ImGui.end();

        ImGui.begin("Level Editor");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i=0; i < spritesheet.size(); i++) {
            Sprite sprite = spritesheet.getSprite(i);
            float spriteWidth = sprite.getWidth() * 4;
            float spriteHeight = sprite.getHeight() * 4;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();
            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                GameObject object = Prefabs.generateSpriteObject(sprite, spriteWidth / 2, spriteWidth / 2); //To make it 32 * 32
                levelEditorComponents.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < spritesheet.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}
