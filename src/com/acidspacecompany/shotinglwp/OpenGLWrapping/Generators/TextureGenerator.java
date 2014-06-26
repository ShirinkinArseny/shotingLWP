package com.acidspacecompany.shotinglwp.OpenGLWrapping.Generators;

import android.graphics.Bitmap;
import com.acidspacecompany.shotinglwp.BicycleDebugger;

import static android.opengl.GLES20.*;
import static android.opengl.GLUtils.texImage2D;


public class TextureGenerator {
    private static final String TAG = "TextureGenerator";

    public static int loadTexture(Bitmap bitmap, boolean isInfinite) {
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0]==0) {
            BicycleDebugger.w(TAG, "Could not generate new a new OpenGL texture object.");
            return 0;
        }

        //Загружаем текстуру
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

        //Настраиваем стандартные параметры отрисовки
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        if (isInfinite)
        {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        }
        else {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        }

        //Загружаем Bitmap
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        //Генерирем Mipmap
        glGenerateMipmap(GL_TEXTURE_2D);

        //Битмап не нужен больше ни кому
        bitmap.recycle();


        //Сбрасываем текстуру так как действия с ней пока закончились
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];

    }

    public static int[] loadTextures(Bitmap[] bitmaps) {
        int[] result = new int[bitmaps.length];

        for (int i=0; i<result.length; i++)
            result[i] = loadTexture(bitmaps[i], false);

        return result;
    }


}
