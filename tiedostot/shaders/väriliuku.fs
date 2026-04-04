#version 120

uniform float      time;
uniform sampler2D  sampler;
varying vec2       tex_coords;
varying vec3       vertex_coords;
uniform float      fade;

void main() {
    vec2 frag_coords = vertex_coords.xy;
	vec2 uv = frag_coords*2;
    vec3 col = 0.5 + 0.5*cos(time+uv.xyx+vec3(0,2,4));
	vec4 color = vec4(col,0.0);
	gl_FragColor = vec4(col, 0.5) - fade;
}