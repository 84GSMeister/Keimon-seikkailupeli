#version 120

in vec3 vertices;
in vec2 textures;
varying vec2 tex_coords;

uniform mat4 projection;

void main() {
    tex_coords = textures;
    gl_Position = projection * vec4(vertices, 1);
}