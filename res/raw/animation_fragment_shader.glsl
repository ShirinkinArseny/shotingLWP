precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
uniform vec4 u_Color;

//То, на чем строится анимация
//Высота текстуры
uniform int u_Height;
//Номер кадра
uniform int u_Position;

void main()
{
    //Задаем текстурные координаты
    vec2 coordinates = v_TextureCoordinates;
    coordinates.x += u_Position*u_Height;
    gl_FragColor = texture2D(u_TextureUnit, coordinates);
    //Задаем прозрачность
    gl_FragColor *= u_Color;
    gl_FragColor.rgb *= u_Color.a;
}