package Scenes;

import Components.*;

import Hex.GameObject;
import Hex.Prefabs;
import Util.AssetPool;

import Util.Settings;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

public class LevelEditorSceneInitializer extends SceneInitializer {
    private Spritesheet spritesheet;
   GameObject levelEditorComponents;

    public LevelEditorSceneInitializer(){

    }

    @Override
    public void init(Scene scene){
        loadResources(scene);

        Spritesheet gizmos = AssetPool.getSpritesheet("Assets/Sprites/gizmos.png");
        spritesheet = AssetPool.getSpritesheet("Assets/Sprites/decorationsAndBlocks.png");

        levelEditorComponents = scene.createGameObject("LevelEditor");
        levelEditorComponents.setNoSerialize();
        levelEditorComponents.addComponent(new MouseControls());
        levelEditorComponents.addComponent(new GridLines());
        levelEditorComponents.addComponent(new EditorCamera(scene.camera()));
        levelEditorComponents.addComponent(new GizmoSystem(gizmos));
        scene.addGameObjectToScene(levelEditorComponents);
    }

    @Override
    public void loadResources(Scene scene) {
        AssetPool.getShader("Assets/Shaders/default.glsl");
        AssetPool.addSpritesheet("Assets/Sprites/decorationsAndBlocks.png", new Spritesheet(AssetPool.getTexture("Assets/Sprites/decorationsAndBlocks.png"),
                16, 16, 81, 0));
        AssetPool.addSpritesheet("Assets/Sprites/gizmos.png", new Spritesheet(AssetPool.getTexture("Assets/Sprites/gizmos.png"),
                24, 48, 3, 0));

        for (GameObject go : scene.getGameObjects()) {
            if (go.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilePath()));
                }
            }
        }
    }

    @Override
    public void imGui() {
        ImGui.begin("Level Editor Stuff");
        levelEditorComponents.imGui();
        ImGui.end();

        ImGui.begin("Test window");

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
                GameObject object = Prefabs.generateSpriteObject(sprite, Settings.GRID_WIDTH, Settings.GRID_HEIGHT);
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