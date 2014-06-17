package com.acidspacecompany.shotinglwp.TimeFunctions;

public class NiceMoveTimeFunction implements TimeFunction{

    private float from;
    private float lastTo;
    private float to;
    private float length;
    private float time=0;

    private float doubleBezierCurve(float t) {
        float t1=(1-t);

        return t1*t1*from+2*t*t1*lastTo+t*t*to;
    }

    private float tripleBezierCurve(float t) {
        float t1=(1-t);
        float t1_2=(1-t)*t1;

        float t_2=t*t;

        return from*t1*t1_2+3*lastTo*t*t1_2+3*to*t_2*t1+to*t_2*t;
    }

    public float getValue() {
        if (time<length)
            return tripleBezierCurve(time/length);
        else return to;
    }

    public void tick(float dt) {
        time+=dt;
    }

    public void setAim(float aim) {
        from=getValue();
        time=0;
        lastTo=to;
        to=aim;
    }

    public NiceMoveTimeFunction(float from, float to, float length) {
        super();
        this.from=from;
        this.to=to;
        lastTo=to;
        this.length=length;
    }
}
