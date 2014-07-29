//Позиция
attribute vec4 a_Position;
//Позиция в текстуре
attribute vec2 a_TextureCoordinates;
//Для передачи фрагментному шейдеру
varying vec2 v_TextureCoordinates;

//Матрица для преобразования в СК OpenGL
uniform mat4 u_Matrix;

void main()
{
    v_TextureCoordinates = (a_TextureCoordinates + vec2(1.0,1.0)) * 0.5;
    gl_Position = u_Matrix * a_Position;
}