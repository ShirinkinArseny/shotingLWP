//Позиция (матрицы нинужны)
attribute vec4 a_Position;
attribute vec2 a_TexturePosition;
varying vec2 v_TexturePosition;
void main()
{
    gl_Position = a_Position;
    v_TexturePosition = a_TexturePosition;
}