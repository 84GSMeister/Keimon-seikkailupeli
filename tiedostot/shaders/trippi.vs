#version 120

attribute vec3      vertices;
attribute vec2      textures;
varying vec2        tex_coords;
varying vec3        vertex_coords;

uniform mat4        projection;

void main() {
    tex_coords = textures;
	vertex_coords = vertices;
    gl_Position = projection * vec4(vertices, 1);
}