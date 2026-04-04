#version 120

attribute vec3      vertices;
attribute vec2      textures;
varying vec2        tex_coords;
varying vec2        frag_coords;

uniform mat4        projection;
uniform mat4        transformWorld;
uniform mat4        transformObject;

void main() {
    tex_coords = textures;
	frag_coords = tex_coords/2.0;
    //gl_Position = projection * transformWorld * transformObject * vec4(vertices, 1);
    gl_Position = projection * vec4(vertices, 1);
}