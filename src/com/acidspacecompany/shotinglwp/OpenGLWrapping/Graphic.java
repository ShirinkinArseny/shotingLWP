package com.acidspacecompany.shotinglwp.OpenGLWrapping;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.Matrix;
import android.util.Log;
import com.acidspacecompany.shotinglwp.BicycleDebugger;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Generators.TextureGenerator;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Shaders.FillBitmapShader;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Shaders.FillColorShader;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Shaders.TextureShader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.*;


import static android.opengl.GLES20.*;

public class Graphic {

    private static final String TAG = "OpenGLES20Engine";

    private static float[] orthoMatrix = new float[16];

    /**
     * Обновление параметров матрицы преобразования. Должна быть вызвана при изменении параметров экрана
     * @param width Ширина
     * @param height Высота
     */
    public static void resize(int width, int height) {
        glViewport(0,0,width,height);
        Matrix.orthoM(orthoMatrix, 0, 0,width,height,0,-1,1);

        FloatBuffer projectionMatrixBuffer = createNativeFloatArray(orthoMatrix);
        projectionMatrixBuffer.position(0);
    }


    //В float 4 байта
    private static final int BYTES_PER_FLOAT = 4;
    //Вершины двухмерные
    private static final int POSITION_COMPONENT_COUNT = 2;
    //В текстуре 2 координаты
    private static final int UV_COMPONENT_COUNT = 2;

    /**
     * Метод, создающий нативный массив значений float из массива Java
     * @param javaFloatArray Массив Java
     * @return Нативный массив
     */
    private static FloatBuffer createNativeFloatArray(float[] javaFloatArray)
    {
        //Создаем ByteBuffer
        return ByteBuffer.
                //Теперь задаем размер таким, чтобы влезли все величины
                        allocateDirect(javaFloatArray.length * BYTES_PER_FLOAT).
                //Задаем порядок, родной для машины (прямой (0x01) или перевернутый (0x10))
                        order(ByteOrder.nativeOrder()).
                //Храним его как массив значений float
                        asFloatBuffer().
                //Добавляем значения из массива
                        put(javaFloatArray);
    }

    //Шейдер, который просто заливает всё цветом
    private static FillColorShader fillColorShader;
    //Шейдер, который заливает всё текстурой и прозрачностью
    private static TextureShader textureShader;
    //Шейдер для замощения всего одним битмапом
    private static FillBitmapShader fillBitmapShader;
    //Шейдер для отрисовки прямых

    //Итентификатор VBO
    private static int vboId;

    /**
     * Инициализация системы. Компилирование всего и вся.
     * @param context Контекст, в котором содержатся все ресурсы
     */
    public static void init(Context context) {
        //Загружаем шейдеры
        fillColorShader = new FillColorShader(context);
        fillColorShader.validate();
        textureShader = new TextureShader(context);
        textureShader.validate();
        //fontShader = new FontShader(context);
        //fontShader.validate();
        fillBitmapShader = new FillBitmapShader(context);
        fillBitmapShader.validate();

        //Создаем VBO для вершин прямоугольника
        int[] buffers = new int[1];
        glGenBuffers(1,buffers,0);
        if (buffers[0]==0)
            Log.e(TAG, "Could not create buffers");
        vboId = buffers[0];
        //Создаем вершины
        FloatBuffer vboBufferVertexes = createNativeFloatArray(new float[]{
                -1f,  1f,
                -1f, -1f,
                1f, -1f,
                1f,  1f
        });
        //Задаем указатель на начало массива
        vboBufferVertexes.position(0);
        //Указываем данные
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER,vboBufferVertexes.capacity() * BYTES_PER_FLOAT, vboBufferVertexes, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        //Включаем alpha-blending
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);


        //Отрисовываем только передние стороны каждого битмапа
        //(обратные просто никогда не увидеть так как это 2D)
        glFrontFace(GL_CCW);
        glCullFace(GL_FRONT);
        glEnable(GL_CULL_FACE);
    }


    /**
     * Уничтожение контекста
     */
    public static void destroy() {
        fillColorShader.delete();
        textureShader.delete();
        //fontShader.delete();
        fillBitmapShader.delete();
        int i=0;
        int[] goodArray = new int[textures.size()];
        for (Integer texture : textures)
            goodArray[i++] = texture;
        glDeleteTextures(goodArray.length, goodArray, 0);
        glDeleteBuffers(1, new int[]{vboId}, 0);
    }


    public enum Mode {
        FILL_BITMAP,
        DRAW_RECTANGLES,
        DRAW_BITMAPS
    }

    /**
     * Задаем режим отрисовки прямоугольников
     */
    private static void initRectangles() {
        //Задаем шейдер для отрисовки
        fillColorShader.use();
        //Задаем буфер для отрисовки
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        //Определяем местонахождение всех атрибутов
        final int aPosition = fillColorShader.get_aPosition();
        glEnableVertexAttribArray(aPosition);
        glVertexAttribPointer(aPosition, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, 0);
        //Освобождаем VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }


    /**
     * Задаем режим отрисовки текстур
     */
    private static void initBitmaps() {
        textureShader.use();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        final int aPosition = textureShader.get_aPosition();
        final int aTexturePosition = textureShader.get_aTextureCoordinates();

        glEnableVertexAttribArray(aPosition);
        glVertexAttribPointer(aPosition, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, 0);

        //Для текстурных координат и для позиции используем одни и те же значения

        glEnableVertexAttribArray(aTexturePosition);
        glVertexAttribPointer(aTexturePosition, UV_COMPONENT_COUNT, GL_FLOAT, false, 0, 0);

        //Освобождаем VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        //Задаем 0-й юнит для текстуры
        //Так как текстура всего одна -- то всё будет в порядке
        //Если текстур будет больше -- изменить легко
        //Для этого во время отрисовки нужно вызывать glActiveTexture(GL_NUM)
        //И потом glBindTexture()
        glActiveTexture(GL_TEXTURE0);
        textureShader.setTexture(0);
    }



    /**
     * Начало отрисовки. Очищение экрана белым цветом.
     */
    public static void startDraw() {
        //Очищаем экран белым цветом
        glClearColor(1.0f,1.0f,1.0f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void begin(Mode drawMode) {
        switch (drawMode) {
            case DRAW_RECTANGLES: initRectangles();
                break;
            case DRAW_BITMAPS:    initBitmaps();
                break;
            case FILL_BITMAP: //initFillBitmap();
                break;
        }

        bindColor(1, 1, 1, 1);
    }

    public static void end() {
        glUseProgram(0);
    }

    //Список для хранения текстур чтобы их потом удалить
    private static LinkedList<Integer> textures = new LinkedList<>();

    public static int[] genTextures(Bitmap[] array) {
        return TextureGenerator.loadTextures(array);
    }

    /**
     * Генерация текстуры из Bitmap
     * @param b Битмап, из которого создается текстура (величина сторон должна быть степенью двойки!)
     * @return Итентификатор
     */
    public static int genTexture(Bitmap b) {
        final int id = TextureGenerator.loadTexture(b,false);
        textures.add(id);
        return id;
    }

    private static Bitmap rotate(Bitmap b) {
        android.graphics.Matrix bitmapRotation = new android.graphics.Matrix();
        bitmapRotation.postRotate(180);
        bitmapRotation.postScale(-1,1);
        return Bitmap.createBitmap(b , 0, 0, b.getWidth(), b.getHeight(), bitmapRotation, true);
    }

    public static int genTextureBackground(Bitmap b) {
        return genTexture(b);
    }


    public static int genInfinityTexture(Bitmap b) {
        final int id = TextureGenerator.loadTexture(b,true);
        textures.add(id);
        return id;
    }

    public static int genInfinityTextureBackgroung(Bitmap b) {
        return genInfinityTexture(rotate(b));
    }

    private static float[] rotateScaleMatrix = new float[16];
    private static float[] translateMatrix = new float[16];
    private static float[] resultMatrix = new float[16];

    //Вершины для заливки экрана (не зависят от его размера совсем)
    private static final FloatBuffer fillBitmapVertexesBuffer = createNativeFloatArray(new float[] {
            //Левый нижний угол
            -1, -1, 0,0,
            //Левый верхний угол
            -1, 1, 0,1,
            //Правый верхний угол
            1, 1, 1,1,
            //Правый нижний угол
            1,-1, 1,0,
            //Левый нижний угол
            -1, -1, 0,0,
            //Правый верхний угол
            1,1,    1,1
    });

    private static void initFillBitmap() {
        //Используем верную программу
        fillBitmapShader.use();
        //Задаем вершины
        final int stride = (POSITION_COMPONENT_COUNT + UV_COMPONENT_COUNT) * BYTES_PER_FLOAT;
        final int aPosition = fillBitmapShader.get_aPosition();
        fillBitmapVertexesBuffer.position(0);
        glEnableVertexAttribArray(aPosition);
        glVertexAttribPointer(aPosition, POSITION_COMPONENT_COUNT, GL_FLOAT, false, stride, fillBitmapVertexesBuffer);
        fillBitmapVertexesBuffer.position(POSITION_COMPONENT_COUNT);
        final int aTexturePosition = fillBitmapShader.get_aTexturePosition();
        glEnableVertexAttribArray(aTexturePosition);
        glVertexAttribPointer(aTexturePosition, UV_COMPONENT_COUNT, GL_FLOAT, false, stride, fillBitmapVertexesBuffer);

        glActiveTexture(GL_TEXTURE0);
        fillBitmapShader.setTexture(0);

    }

    /*
    ------------------------------ OPTIMISATION START LINE -------------------------------------------
    */
    public static int getScaleMatricesCount() {
        return scaleMatrices.size();
    }

    public static int getResultMatricesCount() {
        return resultMatrices.size();
    }

    public static int getUsedScaleMatricesCount() {
        return notNullScaleMatrices;
    }

    public static int getUsedResultMatricesCount() {
        return notNullResultMatrices;
    }

    private static ArrayList<float[]> scaleMatrices=new ArrayList<>();
    private static int notNullScaleMatrices=0;
    private static ArrayList<float[]> resultMatrices=new ArrayList<>();
    private static int notNullResultMatrices=0;

    public static void unBindMatrices() {
        rotateScaleMatrix=new float[16];
        translateMatrix=new float[16];
        resultMatrix=new float[16];
    }

    public static void bindBitmap(int id) {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public static void bindScaleMatrix(int id) {
        Graphic.rotateScaleMatrix =scaleMatrices.get(id);
    }

    public static void bindRotateScaleMatrix(int id) {
        bindScaleMatrix(id);
    }

    public static void bindResultMatrix(int id) {
        Graphic.resultMatrix=resultMatrices.get(id);
    }

    private static float [] generateTranslationMatrix(float x, float y) {
        float [] m=new float[16];
        Matrix.setIdentityM(m, 0);
        Matrix.translateM(m, 0, x, y, 0);
        Matrix.multiplyMM(m, 0, orthoMatrix, 0, m, 0);
        return m;
    }

    private static float[] generateScaleMatrix(float w, float h) {
        float [] m=new float[16];
        Matrix.setIdentityM(m, 0);
        Matrix.scaleM(m, 0, w, h, 1);
        return m;
    }

    private static float[] generateRotateMatrix(float angle) {
        float [] m=new float[16];
        Matrix.setIdentityM(m, 0);
        Matrix.rotateM(m, 0, angle+90, 0f, 0f, 1f);
        return m;
    }

    private static float [] generateTranslationRotateMatrix(float x, float y, float alpha) {
        float [] m=new float[16];
        Matrix.multiplyMM(m, 0, generateTranslationMatrix(x, y), 0, generateRotateMatrix(alpha), 0);
        return m;
    }

    private static float[] generateRotateScaleMatrix(float w, float h, float angle) {
        float [] m=new float[16];
        Matrix.multiplyMM(m, 0, generateRotateMatrix(angle), 0, generateScaleMatrix(w, h), 0);
        return m;
    }

    private static float [] generateResultMatrix(float x, float y, float w, float h) {
        float [] m=new float[16];
        Matrix.multiplyMM(m, 0, generateTranslationMatrix(x, y), 0, generateScaleMatrix(w, h), 0);
        return m;
    }

    private static float [] generateResultMatrix(float x, float y, float w, float h, float a) {
        float[] m = new float[16];
        Matrix.multiplyMM(m, 0, generateTranslationMatrix(x, y), 0, generateRotateScaleMatrix(w, h, a), 0);
        return m;
    }

    private static void applyTranslationAndScale() {
        resultMatrix=new float[16];
        Matrix.multiplyMM(resultMatrix, 0, translateMatrix, 0, rotateScaleMatrix, 0);
    }

    public static int getScaleMatrixID(float w, float h) {
        notNullScaleMatrices++;
        for (int i=0; i<scaleMatrices.size(); i++)
            if (scaleMatrices.get(i)==null) {
                scaleMatrices.set(i, generateScaleMatrix(w, h));
                return i;
            }
        scaleMatrices.add(generateScaleMatrix(w, h));
        return scaleMatrices.size()-1;
    }

    public static int getRotateScaleMatrixID(float w, float h, float angle) {


        notNullScaleMatrices++;
        for (int i=0; i<scaleMatrices.size(); i++)
            if (scaleMatrices.get(i)==null) {
                scaleMatrices.set(i, generateRotateScaleMatrix(w, h, angle));
                return i;
            }
        scaleMatrices.add(generateScaleMatrix(w, h));
        return scaleMatrices.size()-1;
    }

    public static int getResultMatrixID(float x, float y, float w, float h) {
        notNullResultMatrices++;
        for (int i=0; i<resultMatrices.size(); i++)
            if (resultMatrices.get(i)==null) {
                resultMatrices.set(i, generateResultMatrix(x, y, w, h));
                return i;
            }
        resultMatrices.add(generateResultMatrix(x, y, w, h));
        return resultMatrices.size()-1;
    }

    public static int getResultMatrixID(float x, float y, float w, float h, float angle) {
        notNullResultMatrices++;
        for (int i=0; i<resultMatrices.size(); i++)
            if (resultMatrices.get(i)==null) {
                resultMatrices.set(i, generateResultMatrix(x, y, w, h, angle));
                return i;
            }
        resultMatrices.add(generateResultMatrix(x, y, w, h));
        return resultMatrices.size()-1;
    }

    public static void cleanScaleMatrixID(int id) {
        if (scaleMatrices.get(id)!=null) {
            scaleMatrices.set(id, null);
            notNullScaleMatrices--;
        }
        else BicycleDebugger.e("cleanScaleMatrixID", id + " TODO!"); //todo
    }

    public static void cleanResultMatrixID(int id) {
        resultMatrices.set(id, null);
        notNullResultMatrices--;
    }

    private static void setScaleMatrix(float w, float h) {
       rotateScaleMatrix =generateScaleMatrix(w, h);
    }

    private static void setTranslationMatrix(float x, float y) {
        translateMatrix=generateTranslationMatrix(x, y);
    }

    private static void setTranslationRotateMatrix(float x, float y, float angle) {
        translateMatrix=generateTranslationRotateMatrix(x, y, angle);
    }

    public static void bindColor(float r, float g, float b, float a) {
        textureShader.setColor(r, g, b, a);
    }

    public static void drawBitmap(float x0, float y0, float r, float g, float b, float a) {
        drawBindedBitmap(x0, y0, r, g, b, a);
    }

    public static void drawBitmap(float x0, float y0) {
        drawBindedBitmap(x0, y0);
    }

    public static void drawBitmap(float x0, float y0, float angle) {
        drawBindedBitmap(x0, y0, angle);
    }

    public static void drawBitmap(float x0, float y0, float w, float h, float angle) {
        drawBindedBitmap(x0, y0, w, h, angle);
    }

    public static void drawBitmap(float a) {
        bindColor(1, 1, 1, a);
        drawBindedBitmap();
    }

    public static void drawBitmap() {
        drawBindedBitmap();
    }

    //main
    private static void drawBindedBitmap() {
        textureShader.setMatrix(resultMatrix, 0);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }

    private static void drawBindedBitmap(float x, float y, float w, float h, float alpha) {
        setScaleMatrix(w, h);
        drawBindedBitmap(x, y, alpha);
    }

    private static void drawBindedBitmap(float x, float y, float alpha) {
        setTranslationRotateMatrix(x, y, alpha);
        applyTranslationAndScale();
        drawBindedBitmap();
    }

    private static void drawBindedBitmap(float x, float y) {
        setTranslationMatrix(x, y);
        applyTranslationAndScale();
        drawBindedBitmap();
    }

    private static void drawBindedBitmap(float x, float y, float r, float g, float b, float a) {
        bindColor(r, g, b, a);
        drawBindedBitmap(x, y);
    }

    public static void drawRect(float r, float g, float b, float a) {
        fillColorShader.setMatrix(resultMatrix,0);
        fillColorShader.setColor(r,g,b,a);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }

    public static void drawRect(float x, float y, float width, float height,
                                float r, float g, float b, float a) {
        setScaleMatrix(width, height);
        setTranslationMatrix(x, y);
        applyTranslationAndScale();
        drawRect(r, g, b, a);
    }

}
