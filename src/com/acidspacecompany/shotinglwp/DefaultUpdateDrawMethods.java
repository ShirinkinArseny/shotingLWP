package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.GameObjects.GameObject;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;

import java.util.HashSet;
import java.util.List;

public class DefaultUpdateDrawMethods {



    public static void removeUnusedGameObjects(List objects) {
        for (int i = 0; i < objects.size(); i++) {
            if (!((GameObject) objects.get(i)).getIsNeeded()) {
                ((GameObject) objects.get(i)).dispose();
                objects.remove(i);
                i--;
            }
        }
    }

    public static void removeUnusedGameObjectsWithoutDispose(List objects) {
        for (int i = 0; i < objects.size(); i++) {
            if (!((GameObject)objects.get(i)).getIsNeeded()) {
                objects.remove(i);
                i--;
            }
        }
    }

    public static void updateGameObjects(List objects, float dt) {
        for (Object go : objects) {
            ((GameObject) go).update(dt);
        }
    }


    public static void drawGameObjectLayer(List objects, int textureID) {
        if (!objects.isEmpty()) {
            Graphic.bindBitmap(textureID);
            ((GameObject) objects.get(0)).prepareToDraw();
            for (Object go : objects) {
                ((GameObject) go).draw();
            }
        }
    }

    public static void drawGameObjectLayer(HashSet objects, int textureID) {
        if (!objects.isEmpty()) {
            Graphic.bindBitmap(textureID);
            for (Object go : objects) {
                ((GameObject) go).draw();
            }
        }
    }
}
