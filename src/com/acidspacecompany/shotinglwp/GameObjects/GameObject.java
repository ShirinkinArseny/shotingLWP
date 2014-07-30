package com.acidspacecompany.shotinglwp.GameObjects;

public interface GameObject {

    public void reMatrix();

    public void update(float dt);

    public void draw();

    public void prepareToDraw();

    public void dispose();

    public boolean getIsNeeded();


}
