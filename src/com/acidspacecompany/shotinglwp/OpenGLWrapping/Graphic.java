package com.acidspacecompany.shotinglwp.OpenGLWrapping;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedList;


import static android.opengl.GLES20.*;

public class Graphic {

    private static final String TAG = "OpenGLES20Engine";

    //Создание матрицы преобразования для вывода на экран с использованием абсолютной системы координат
    //Собственно матрица (матрица проекции)
    private static FloatBuffer projectionMatrixBuffer;
    private static float[] orthoMatrix = new float[16];

    /**
     * Обновление параметров матрицы преобразования. Должна быть вызвана при изменении параметров экрана
     * @param width Ширина
     * @param height Высота
     */
    public static void resize(int width, int height) {
        glViewport(0,0,width,height);
        Matrix.orthoM(orthoMatrix, 0, 0,width,height,0,-1,1);

        projectionMatrixBuffer = createNativeFloatArray(orthoMatrix);
        projectionMatrixBuffer.position(0);

    }


    //В float 4 байта
    private static final int BYTES_PER_FLOAT = 4;
    //Вершины двухмерные
    private static final int POSITION_COMPONENT_COUNT = 2;

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

    private static FloatBuffer createNativeFloatArray(LinkedList<Float> vertexes) {
        FloatBuffer result = createFloatBuffer(vertexes.size());
        for (Float vertex : vertexes)
            result.put(vertex);
        return result;
    }
    private static FloatBuffer createFloatBuffer(int length) {
        return  ByteBuffer.
                //Теперь задаем размер таким, чтобы влезли все величины
                        allocateDirect(length * BYTES_PER_FLOAT).
                //Задаем порядок, родной для машины (прямой (0x01) или перевернутый (0x10))
                        order(ByteOrder.nativeOrder()).
                //Храним его как массив значений float
                        asFloatBuffer();
    }
    private static void putValuesIntoFloatBuffer(float[] values, FloatBuffer floatBuffer) {
        floatBuffer.clear();
        floatBuffer.put(values);
    }

    public static int createPrimitive(float[] vertexes) {
        //Создаем буффер вершин
        FloatBuffer nativeVertexes = createNativeFloatArray(vertexes);
        nativeVertexes.position(0);

        //Генерируем буффер OpenGL
        int[] buffers = new int[1];
        glGenBuffers(1,buffers,0);
        final int bufferId = buffers[0];
        //Ловим ошибку создания
        if (bufferId == 0)
            Log.e(TAG, "Could not create buffer");


        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
            glBufferData(GL_ARRAY_BUFFER, nativeVertexes.capacity() * BYTES_PER_FLOAT, nativeVertexes, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        return bufferId;
    }

    //Шейдер, который просто заливает всё цветом
    private static FillColorShader fillColorShader;

    /**
     * Инициализация системы. Компилирование всего и вся.
     * @param context Контекст, в котором содержатся все ресурсы
     */
    public static void init(Context context) {
        //Загружаем шейдеры
        fillColorShader = new FillColorShader(context);
        fillColorShader.validate();

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
    }

    /**
     * Начало отрисовки. Очищение экрана белым цветом.
     */
    public static void startDraw() {
        //Очищаем экран белым цветом
        glClearColor(1.0f,1.0f,1.0f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void startDrawPrimitive(int bufferId) {
        fillColorShader.use();
        //Задаем позицию вершин
        final int aPosition = fillColorShader.get_aPosition();
        glEnableVertexAttribArray(aPosition);
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
            glVertexAttribPointer(aPosition, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public static void end() {
        glUseProgram(0);
    }


    //Буфер для линий в виде списка
    private static LinkedList<Float> lineVertexes = new LinkedList<>();
    public static void startDrawLines(float r,float g, float b, float a, float width) {
        //Очищаем буфер вершин
        lineVertexes.clear();
        fillColorShader.use();
        //Задаем цвет
        fillColorShader.setColor(r,g,b,a);
        glLineWidth(width);
    }

    public static void endDrawLines() {
        //Создаем FloatBuffer из тех вершин, которые хранятся в списке
        final FloatBuffer nativeVertexes = createNativeFloatArray(lineVertexes);
        //Количество элементов в буффере
        final int floatSize = lineVertexes.size();
        //Передаем их в шейдер
        final int aPosition = fillColorShader.get_aPosition();
        nativeVertexes.position(0);
        glEnableVertexAttribArray(aPosition);
        glVertexAttribPointer(aPosition, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, nativeVertexes);
        //Цвет уже задан
        projectionMatrixBuffer.position(0);
        fillColorShader.setMatrix(projectionMatrixBuffer);
        glDrawArrays(GL_LINES, 0, floatSize / POSITION_COMPONENT_COUNT);
    }

    //Буффер для хранения координат вершин линии
    private static FloatBuffer lineBuffer = createFloatBuffer(4);

    public static void drawLine(float x1, float y1, float x2, float y2) {
        lineVertexes.add(x1);
        lineVertexes.add(y1);
        lineVertexes.add(x2);
        lineVertexes.add(y2);
    }

    private static float[] offsetMatrix = new float[16], scaleMatrix = new float[16], rotateMatrix = new float[16], resultMatrix = new float[16];
    private static void createMatrix(float x, float y, float scaleX, float scaleY, float angle) {
        Matrix.setIdentityM(offsetMatrix, 0);
        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.setIdentityM(rotateMatrix,0);
        Matrix.setIdentityM(resultMatrix, 0);

        Matrix.translateM(offsetMatrix, 0, x,y,0);
        Matrix.scaleM(scaleMatrix,0,scaleX, scaleY, 1);
        Matrix.rotateM(rotateMatrix, 0, (float)Math.toDegrees(angle),0,0,1);
        Matrix.multiplyMM(resultMatrix, 0, rotateMatrix, 0, scaleMatrix, 0);
        Matrix.multiplyMM(resultMatrix, 0, offsetMatrix, 0, resultMatrix, 0);
        Matrix.multiplyMM(resultMatrix, 0, orthoMatrix, 0, resultMatrix, 0);
    }

    public static void drawPrimitive(int pointsCount, float r, float g, float b, float a, float lineWidth, float xOffset, float yOffset,float scale, float angle) {
        glLineWidth(lineWidth);
        fillColorShader.setColor(r,g,b,a);
        createMatrix(xOffset,yOffset,scale,scale,angle);
        fillColorShader.setMatrix(resultMatrix,0);

        glDrawArrays(GL_LINES,0, pointsCount);
    }



}