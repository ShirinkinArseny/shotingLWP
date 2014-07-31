precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
uniform vec4 u_Color;

//Для, собственно, трешолда
uniform float u_Th;
uniform float u_ThLVL;
uniform float u_V2;

void main()
{
    //Задаем текстурные координаты
    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);
    gl_FragColor.rgb /= gl_FragColor.a;
    //Задаем цвет
    gl_FragColor *= u_Color;
    //Переопределяем альфу по графику
    float src = gl_FragColor.a;
    if (src<u_V2) {
        src = 0.0;
    }
    else {
        if (src>u_Th) {
            src *= u_ThLVL / u_Th;
        }
        else {
            float k = u_ThLVL / (u_Th - u_V2);
            float b = -k*u_V2;
            src = k*src + b;
        }
    }
    gl_FragColor.a = src;
    gl_FragColor.rgb *= src;
}