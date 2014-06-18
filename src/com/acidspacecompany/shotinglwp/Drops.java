package com.acidspacecompany.shotinglwp;

import com.acidspacecompany.shotinglwp.Geometry.Point;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Primitives.LinePrimitive;
import com.acidspacecompany.shotinglwp.TimeFunctions.LinearTimeFunction;

public class Drops extends Point {

    private static LinePrimitive drops;
    private LinearTimeFunction tf;
    private LinearTimeFunction alpha;
    private float angle;
    private float time=0;
    private float length;


    public static void startDraw() {
        drops.startDraw();
    }

    public static void init(float lineWidth) {
        float[] vertexes=new float[16];


        float deltaAngle= (float) (Math.PI/10);
        float angle= -1.5f*deltaAngle;
        for (int i=0; i<4; i++) {
            vertexes[i*4]=   (float) Math.cos(angle);
            vertexes[i*4+1]= (float) Math.sin(angle);
            vertexes[i*4+2]= (float) Math.cos(angle)*1.3f;
            vertexes[i*4+3]= (float) Math.sin(angle)*1.3f;
            angle+=deltaAngle;
        }

        drops=new LinePrimitive(vertexes, 1f, 0, 0, 0, lineWidth);
    }

    public boolean isNeedable(){
        return time<length;
    }

    public void update(float dt){
        tf.tick(dt);
        time+=dt;
    }

    public void draw() {
        drops.drawWithAlpha(getX(), getY(), tf.getValue(), angle, alpha.getValue());
    }

    public Drops(float x, float y, float angle, float size, float length) {
        super(x, y);
        this.angle= (float) Math.toDegrees(angle);
        this.length=length;
        tf=new LinearTimeFunction(length, 0, size);
        alpha=new LinearTimeFunction(length, 1, 0);
    }
}
