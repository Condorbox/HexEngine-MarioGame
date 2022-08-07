package Scenes;

import Components.*;

import Hex.GameObject;
import Hex.Prefabs;
import Hex.Sound;
import Physics2D.Components.Box2DCollider;
import Physics2D.Components.Rigidbody2D;
import Physics2D.Enums.BodyType;
import Renderer.Font.HFont;
import Util.AssetPool;

import Util.Settings;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

import java.io.File;
import java.util.Collection;

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
        levelEditorComponents.addComponent(new KeyControls());
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
        AssetPool.addSpritesheet("Assets/Sprites/spritesheet.png", new Spritesheet(AssetPool.getTexture("Assets/Sprites/spritesheet.png"),
                        16, 16, 26, 0));
        AssetPool.addSpritesheet("Assets/Sprites/items.png", new Spritesheet(AssetPool.getTexture("Assets/Sprites/items.png"),
                        16, 16, 43, 0));
        AssetPool.addSpritesheet("Assets/Sprites/gizmos.png", new Spritesheet(AssetPool.getTexture("Assets/Sprites/gizmos.png"),
                24, 48, 3, 0));
        AssetPool.addSpritesheet("Assets/Sprites/spritesheet.png", new Spritesheet(AssetPool.getTexture("Assets/Sprites/spritesheet.png"),
                        16, 16, 26, 0));
        AssetPool.addSpritesheet("Assets/Sprites/items.png", new Spritesheet(AssetPool.getTexture("Assets/Sprites/items.png"),
                        16, 16, 43, 0));
        AssetPool.addSpritesheet("Assets/Sprites/turtle.png", new Spritesheet(AssetPool.getTexture("Assets/Sprites/turtle.png"),
                        16, 24, 4, 0));
        AssetPool.addSpritesheet("Assets/Sprites/bigSpritesheet.png", new Spritesheet(AssetPool.getTexture("Assets/Sprites/bigSpritesheet.png"),
                        16, 32, 42, 0));
        AssetPool.addSpritesheet("Assets/Sprites/pipes.png", new Spritesheet(AssetPool.getTexture("Assets/Sprites/pipes.png"),
                        32, 32, 4, 0));

        AssetPool.addSound("Assets/Sounds/main-theme-overworld.ogg", true);
        AssetPool.addSound("Assets/Sounds/flagpole.ogg", false);
        AssetPool.addSound("Assets/Sounds/break_block.ogg", false);
        AssetPool.addSound("Assets/Sounds/bump.ogg", false);
        AssetPool.addSound("Assets/Sounds/coin.ogg", false);
        AssetPool.addSound("Assets/Sounds/gameover.ogg", false);
        AssetPool.addSound("Assets/Sounds/jump-small.ogg", false);
        AssetPool.addSound("Assets/Sounds/mario_die.ogg", false);
        AssetPool.addSound("Assets/Sounds/pipe.ogg", false);
        AssetPool.addSound("Assets/Sounds/powerup.ogg", false);
        AssetPool.addSound("Assets/Sounds/powerup_appears.ogg", false);
        AssetPool.addSound("Assets/Sounds/stage_clear.ogg", false);
        AssetPool.addSound("Assets/Sounds/stomp.ogg", false);
        AssetPool.addSound("Assets/Sounds/kick.ogg", false);
        AssetPool.addSound("Assets/Sounds/invincible.ogg", false);

        for (GameObject go : scene.getGameObjects()) {
            if (go.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilePath()));
                }
            }

            if (go.getComponent(StateMachine.class) != null) {
                StateMachine stateMachine = go.getComponent(StateMachine.class);
                stateMachine.refreshTextures();
            }
        }
    }

    @Override
    public void imGui() {
        ImGui.begin("Level Editor Stuff");
        levelEditorComponents.imGui();
        ImGui.end();

        ImGui.begin("Test window");

        if (ImGui.beginTabBar("WindowTabBar")) {
            if (ImGui.beginTabItem("Blocks")) {

                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                for (int i = 0; i < spritesheet.size(); i++) {
                    if (i == 34) continue;
                    if (i >= 38 && i < 61) continue;

                    Sprite sprite = spritesheet.getSprite(i);
                    float spriteWidth = sprite.getWidth() * 4;
                    float spriteHeight = sprite.getHeight() * 4;
                    int id = sprite.getTexId();
                    Vector2f[] texCoords = sprite.getTexCoords();

                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                        GameObject object = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);
                        Rigidbody2D rb = new Rigidbody2D();
                        rb.setBodyType(BodyType.Static);
                        object.addComponent(rb);
                        Box2DCollider b2d = new Box2DCollider();
                        b2d.setHalfSize(new Vector2f(0.25f, 0.25f));
                        object.addComponent(b2d);
                        object.addComponent(new Ground());
                        if (i == 12) {
                            object.addComponent(new BreakableBrick());
                        }
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

                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Prefabs")) {
                Spritesheet playerSprites = AssetPool.getSpritesheet("Assets/Sprites/spritesheet.png");
                Sprite sprite = playerSprites.getSprite(0);
                float spriteWidth = sprite.getWidth() * 4;
                float spriteHeight = sprite.getHeight() * 4;
                int id = sprite.getTexId();
                Vector2f[] texCoords = sprite.getTexCoords();

                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateMario();
                    levelEditorComponents.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.sameLine();

                Spritesheet items = AssetPool.getSpritesheet("Assets/Sprites/items.png");
                sprite = items.getSprite(0);
                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateQuestionBlock();
                    levelEditorComponents.getComponent(MouseControls.class).pickupObject(object);
                }

                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Sounds")) {
                Collection<Sound> sounds = AssetPool.getAllSounds();
                for (Sound sound : sounds) {
                    File tmp = new File(sound.getFilepath());
                    if (ImGui.button(tmp.getName())) {
                        if (!sound.isPlaying()) {
                            sound.play();
                        } else {
                            sound.stop();
                        }
                    }

                    if (ImGui.getContentRegionAvailX() > 100) {
                        ImGui.sameLine();
                    }
                }

                ImGui.endTabItem();
            }
            ImGui.endTabBar();
        }

        ImGui.end();
    }
}
