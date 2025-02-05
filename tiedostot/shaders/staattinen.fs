#version 400

uniform sampler2D sampler;

in vec2 tex_coords;

layout (location = 0) out vec4 myOutputColor;

void main() {
    gl_FragColor = texture2D(sampler, tex_coords);
}