#version 400

uniform sampler2D sampler;

uniform vec4 color;

in vec2 tex_coords;

void main() {
    //gl_FragColor = texture2D(sampler, tex_coords);
    gl_FragColor = color;
}