package com.acidspacecompany.shotinglwp.GameObjects;

import com.acidspacecompany.shotinglwp.BicycleDebugger;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Graphic;
import com.acidspacecompany.shotinglwp.World;

public class Background implements GameObject{

    private int resultMatrix=-1;

    @Override
    public void reMatrix() {
        if (resultMatrix!=-1)
            Graphic.cleanResultMatrixID(resultMatrix, "Background");
        resultMatrix=Graphic.getResultMatrixID(
                World.getDisplayWidth()/2, World.getDisplayHeight()/2,
                World.getDisplayWidth()/2, World.getDisplayHeight()/2,"Background");
        BicycleDebugger.i("WH", World.getDisplayWidth()+" "+World.getDisplayHeight());
    }


    public void update(float dt){
    }

    public void draw() {
        Graphic.bindColor(1, 1, 1, 1);
        Graphic.bindResultMatrix(resultMatrix, "Background");
        Graphic.drawBitmap();
    }

    @Override
    public void prepareToDraw() {
    }

    @Override
    public void dispose() {
        Graphic.cleanResultMatrixID(resultMatrix, "Background");
    }

    @Override
    public void setIsNoNeededMore(){
    }

    @Override
    public boolean getIsNeeded() {
        return true;
    }

    public Background() {
        reMatrix();
    }
}
