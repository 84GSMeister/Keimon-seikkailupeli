#version 120

in vec3 vertices;
in vec2 textures;

varying vec2 tex_coords;

uniform mat4 projection;

uniform mat4 transformWorld;
uniform mat4 transformObject;

void main() {
    tex_coords = textures;
    //gl_Position = projection * transformWorld * transformObject * vec4(vertices, 1);
    gl_Position = projection * vec4(vertices, 1);
}