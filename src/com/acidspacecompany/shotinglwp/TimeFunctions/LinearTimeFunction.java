package com.acidspacecompany.shotinglwp.TimeFunctions;

public class LinearTimeFunction implements TimeFunction{

    private float delta;
    private float from;
    private float to;
    private float length;
    protected float time;

    public float getValue() {
        return from+delta*time;
    }

    public void tick(float delta) {
        if (time<length)
        time+=delta;
    }

    public void setAim(float aim) {
        from=getValue();
        time=0;
        to=aim;
        delta=to-from;
    }

    public LinearTimeFunction(float length, float from, float to) {
        super();
        this.length=length;
        this.from=from;
        this.to=to;
        delta=(to-from)/length;
    }
}
