package com.acidspacecompany.shotinglwp.OpenGLWrapping;

public class MatrixCreator {
    public static float[] rotateM(float sin, float cos) {
        float[] result = new float[16];

        result[0] = cos; result[4] = -sin; result[8] = 0; result[12] = 0;
        result[1] = sin; result[5] = cos;  result[9] = 0; result[13] = 0;
        result[2] = 0;   result[6] = 0;    result[10]= 1; result[14] = 0;
        result[3] = 0;   result[7] = 0;    result[11] = 0;result[15] = 1;

        return result;
    }
}
