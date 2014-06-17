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
    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = u_Matrix * a_Position;
}