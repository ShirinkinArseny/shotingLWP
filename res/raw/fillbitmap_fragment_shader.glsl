precision mediump float;

//Текстурные координаты
varying vec2 v_TexturePosition;
//Текстура
uniform sampler2D u_TextureUnit;

//Размеры текстуры
uniform vec2 u_TextureDimensions;
//Смещение по dx
uniform float u_DX;

void main() {
    vec2 rawPosition = v_TexturePosition;
    rawPosition.x -= u_DX;
    vec2 colorPosition = rawPosition / u_TextureDimensions;
    gl_FragColor = texture2D(u_TextureUnit, colorPosition);
}