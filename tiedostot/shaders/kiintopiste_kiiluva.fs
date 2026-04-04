#version 120

uniform float      time;
uniform sampler2D  sampler;
varying vec2       tex_coords;
uniform float      fade;
uniform vec4       addcolor;
uniform vec4       subcolor;
uniform vec4       himmennys;

void main() {
	vec4 fadeColor = vec4(fade, fade, fade, 0);
	gl_FragColor = texture2D(sampler, tex_coords) + addcolor - subcolor - himmennys - fadeColor;
}