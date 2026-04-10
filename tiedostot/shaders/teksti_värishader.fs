#version 120

uniform float      time;
uniform sampler2D  sampler;
varying vec2       tex_coords;
uniform float      fade;

void main() {
    vec4 color = vec4(0.5*(-sin(time*2)), 0.5*(sin(time*2)), 0.5*(cos(time*2)), 0.0);
	vec4 fadeColor = vec4(fade, fade, fade, 0);
	gl_FragColor = texture2D(sampler, tex_coords) - color - fadeColor;
}