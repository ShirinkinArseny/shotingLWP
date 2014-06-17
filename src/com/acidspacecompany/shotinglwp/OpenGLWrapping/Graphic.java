package com.acidspacecompany.shotinglwp.OpenGLWrapping;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


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

    //Шейдер, который просто заливает всё цветом
    private static FillColorShader fillColorShader;

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


        //Создаем VBO для вершин прямоугольника
        int[] buffers = new int[1];
        glGenBuffers(1,buffers,0);
        if (buffers[0]==0)
            Log.e(TAG, "Could not create buffers");
        vboId = buffers[0];
        //Создаем вершины
        FloatBuffer vboBufferVertexes = createNativeFloatArray(new float[]{
                //Левый нижний угол
                0,0,
                //Правый верхний угол
                1,1,
                //Левый верхний угол
                0,1,
                //Правый нижний угол
                1,0,
                //Правый верхний угол
                1,1,
                //Левый нижний угол
                0,0
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
        glDeleteBuffers(1, new int[]{vboId}, 0);
    }

    /**
     * Начало отрисовки. Очищение экрана белым цветом.
     */
    public static void startDraw() {
        //Очищаем экран белым цветом
        glClearColor(1.0f,1.0f,1.0f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void end() {
        glUseProgram(0);
    }

    public static void startDrawLines() {
        fillColorShader.use();
    }

    //Буффер для хранения координат вершин линии
    private static FloatBuffer lineBuffer = createFloatBuffer(4);

    public static void drawLine(float x1, float y1, float x2, float y2, float width, float r, float g, float b, float a) {
        putValuesIntoFloatBuffer(new float[]{x1,y1,x2,y2}, lineBuffer);
        //Передаем позицию линии в шейдер
        final int aPosition = fillColorShader.get_aPosition();
        lineBuffer.position(0);
        glEnableVertexAttribArray(aPosition);
        glVertexAttribPointer(aPosition, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, lineBuffer);
        //Задаем цвет
        fillColorShader.setColor(r, g, b, a);
        //Ширина линии задается не в шейдере так как
        //она используется на этапе растеризации,
        //а не на этапе шейдера
        glLineWidth(width);
        projectionMatrixBuffer.position(0);
        fillColorShader.setMatrix(projectionMatrixBuffer);
        glDrawArrays(GL_LINES, 0, 2);
    }


}