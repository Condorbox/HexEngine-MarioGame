package Scenes;

import Components.GameCamera;
import Components.SpriteRenderer;
import Components.Spritesheet;
import Components.StateMachine;
import Hex.GameObject;
import Util.AssetPool;

public class LevelSceneInitializer extends SceneInitializer {
    public LevelSceneInitializer() {

    }

    @Override
    public void init(Scene scene) {
        Spritesheet sprites = AssetPool.getSpritesheet("Assets/Sprites/decorationsAndBlocks.png");

        GameObject cameraObject = scene.createGameObject("GameCamera");
        cameraObject.addComponent(new GameCamera(scene.camera()));
        cameraObject.start();
        scene.addGameObjectToScene(cameraObject);
    }

    @Override
    public void loadResources(Scene scene) {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("Assets/Sprites/decorationsAndBlocks.png",
                new Spritesheet(AssetPool.getTexture("Assets/Sprites/decorationsAndBlocks.png"),
                        16, 16, 81, 0));
        AssetPool.addSpritesheet("Assets/Sprites/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("Assets/Sprites/spritesheet.png"),
                        16, 16, 26, 0));
        AssetPool.addSpritesheet("Assets/Sprites/turtle.png",
                new Spritesheet(AssetPool.getTexture("Assets/Sprites/turtle.png"),
                        16, 24, 4, 0));
        AssetPool.addSpritesheet("Assets/Sprites/bigSpritesheet.png",
                new Spritesheet(AssetPool.getTexture("Assets/Sprites/bigSpritesheet.png"),
                        16, 32, 42, 0));
        AssetPool.addSpritesheet("Assets/Sprites/pipes.png",
                new Spritesheet(AssetPool.getTexture("Assets/Sprites/pipes.png"),
                        32, 32, 4, 0));
        AssetPool.addSpritesheet("Assets/Sprites/gizmos.png",
                new Spritesheet(AssetPool.getTexture("Assets/Sprites/gizmos.png"),
                        16, 16, 43, 0));
        AssetPool.addSpritesheet("Assets/Sprites/gizmos.png",
                new Spritesheet(AssetPool.getTexture("Assets/Sprites/gizmos.png"),
                        24, 48, 3, 0));

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

        for (GameObject g : scene.getGameObjects()) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilePath()));
                }
            }

            if (g.getComponent(StateMachine.class) != null) {
                StateMachine stateMachine = g.getComponent(StateMachine.class);
                stateMachine.refreshTextures();
            }
        }
    }

    @Override
    public void imGui() {

    }
}
