package com.acidspacecompany.shotinglwp.TimeFunctions;

public interface TimeFunction {

    public void tick(float dt);

    public void setAim(float aim);

    public float getValue();
}
