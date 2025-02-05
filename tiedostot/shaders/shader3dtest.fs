#version 120

uniform sampler2D sampler;

uniform vec4 color;
uniform vec4 addcolor;
uniform vec4 subcolor;
uniform vec4 himmennys;

varying vec2 tex_coords;

void main() {
    gl_FragColor = texture2D(sampler, tex_coords) + addcolor - subcolor - himmennys;
}