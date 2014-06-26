package com.acidspacecompany.shotinglwp.OpenGLWrapping;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.Matrix;
import com.acidspacecompany.shotinglwp.BicycleDebugger;
import com.acidspacecompany.shotinglwp.Geometry.Rectangle;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Generators.TextureGenerator;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Shaders.FillBitmapShader;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Shaders.FillColorShader;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Shaders.Shader;
import com.acidspacecompany.shotinglwp.OpenGLWrapping.Shaders.TextureShader;

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
     * Включает использование матрицы преобразования в шейдере
     * @param shader Программа шейдера
     */
    private static void useMatrix(Shader shader) {
        //Задаем матрицу для того чтобы отойти от экранной СК
        projectionMatrixBuffer.position(0);
        shader.setMatrix(projectionMatrixBuffer);
    }

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
            BicycleDebugger.e(TAG, "Could not create buffers");
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
        DRAW_BITMAPS,
        DRAW_LINES
    }

    private static Mode currentMode;

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
        currentMode = drawMode;
        switch (drawMode) {
            case DRAW_RECTANGLES: initRectangles();
                break;
            case DRAW_BITMAPS:    initBitmaps();
                break;
            case FILL_BITMAP: initFillBitmap();
                break;
            case DRAW_LINES: initLines();
                break;
        }
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

    public static int genInfinityTexture(Bitmap b) {
        final int id = TextureGenerator.loadTexture(b,true);
        textures.add(id);
        return id;
    }

    private static float[] scaleMatrix = new float[16];
    private static float[] translateMatrix = new float[16];
    private static float[] resultMatrix = new float[16];

    /**
     * Создание матрицы для преобразования "единичного" прямоугольника к необходимому виду
     * @param x X для левого нижнего угла
     * @param y Y для левого нижнего угла
     * @param width Ширина прямоугольника
     * @param height Высота прямоугольника
     */
    private static void createRectangle(float x, float y, float width, float height) {
        //Создаем матрицу
        //Задаем масштабирование
        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.setIdentityM(translateMatrix,0);
        Matrix.setIdentityM(resultMatrix,0);
        Matrix.scaleM(scaleMatrix, 0, width, height, 1);
        //Задаем перенос
        Matrix.translateM(translateMatrix, 0, x,y,0);
        //Сначала масштабируем, потом переносим
        Matrix.multiplyMM(resultMatrix, 0, translateMatrix, 0, scaleMatrix, 0);
        //Переносим в экранную систему координат
        Matrix.multiplyMM(resultMatrix, 0, orthoMatrix, 0, resultMatrix, 0);
    }

    private static void drawOneRectangle() {
        //Отрисовываем 6 вершин одного прямоугольника
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

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

    private static void initLines() {
        //Используем шейдер для заливки цветом
        //Так как фактически OpenGL без разницы что заливать цветом
        //Прямоугольник или трекгольник
        fillColorShader.use();
    }

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

    public enum PaintingType {Tile, Fill}

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
        fillColorShader.setColor(r,g,b,a);
        //Ширина линии задается не в шейдере так как
        //она используется на этапе растеризации,
        //а не на этапе шейдера
        glLineWidth(width);
        useMatrix(fillColorShader);
        glDrawArrays(GL_LINES, 0, 2);
    }


    public static void drawBitmap(int texture, Rectangle rectangle) {
        drawBitmap(texture, rectangle.getX0(), rectangle.getY0(), rectangle.getWidth(), rectangle.getHeight(), 1,1,1, 1.0f);
    }

    public static void drawBitmap(int texture, Rectangle rectangle, float a) {
        drawBitmap(texture, rectangle.getX0(), rectangle.getY0(), rectangle.getWidth(), rectangle.getHeight(), 1,1,1, a);
    }

    public static void drawBitmap(int texture, Rectangle rectangle, float r, float g, float b) {
        drawBitmap(texture, rectangle.getX0(), rectangle.getY0(), rectangle.getWidth(), rectangle.getHeight(),r,g,b, 1.0f);
    }

    public static void drawBitmap(int texture, Rectangle rectangle,float r, float g, float b, float a) {
        drawBitmap(texture, rectangle.getX0(), rectangle.getY0(), rectangle.getWidth(), rectangle.getHeight(),r,g,b, a);
    }

    public static void drawBitmap(int bitmap, float x, float y, float width, float height,float r, float g, float b, float a) {

        if (currentMode!= Mode.DRAW_BITMAPS)
            BicycleDebugger.e(TAG, "Incorrect drawing mode");
        else {
            createRectangle(x,y,width,height);
            textureShader.setMatrix(resultMatrix,0);
            textureShader.setColor(r, g, b, a);
            glBindTexture(GL_TEXTURE_2D, bitmap);
            drawOneRectangle();
        }

    }

    public static void drawRect(float x, float y, float x1, float y1, float r, float g, float b, float a) {
        drawRectInside(x, y, x1 - x, y1 - y, r, g, b, a);
    }

    private static void drawRectInside(float x, float y, float width, float height, float r, float g, float b, float a) {
        //Будем рисовать только в том случае, если режим рисовки -- прямоугольники
        if (currentMode!= Mode.DRAW_RECTANGLES)
            BicycleDebugger.e(TAG, "Incorrect drawing mode");
        else {
            //Задаем матрицы, которые преобразуют прямоугольник из VBO к необходимому прямоугольнику
            createRectangle(x,y,width,height);
            //Задаем матрицу для шейдера
            fillColorShader.setMatrix(resultMatrix,0);
            //Задаем цвет
            fillColorShader.setColor(r,g,b,a);

            //Рисуем
            drawOneRectangle();
        }
    }

}
