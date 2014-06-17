precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
uniform vec4 u_Color;

void main()
{
    //Задаем текстурные координаты
    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);
    //Задаем прозрачность
    gl_FragColor *= u_Color;
    gl_FragColor.rgb *= u_Color.a;
}