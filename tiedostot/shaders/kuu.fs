#version 120

uniform float      time;
uniform sampler2D  sampler;
varying vec2       tex_coords;
uniform float      fade;

void main() {
    vec4 color = vec4(1.0+sin(time*0.731), 1.0+cos(time*0.985), 1.0-sin(time*0.421), 0.0);
	vec4 fadeColor = vec4(fade, fade, fade, 0);
	gl_FragColor = texture2D(sampler, tex_coords) - color - fadeColor;
}