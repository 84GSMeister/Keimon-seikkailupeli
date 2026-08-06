#version 120

uniform float      time;
uniform sampler2D  sampler;
varying vec2       tex_coords;
uniform float      fade;

void main() {
    float red = 0.0+sin(time*0.5);
	float green = 0.0+cos(time*0.4);
	float blue = 0.0-sin(time*0.3);
	vec4 color = vec4(red*0.5, green*0.5, blue*0.5, 0.0);
	float redNeg = 0.0+sin(time*1.0);
	float greenNeg = 0.0+cos(time*0.8);
	float blueNeg = 0.0-sin(time*0.6);
    vec4 colorNeg = vec4(redNeg*0.5, greenNeg*0.5, blueNeg*0.5, 0.0);
	vec4 fadeColor = vec4(fade, fade, fade, 0);
	gl_FragColor = texture2D(sampler, tex_coords) + color - colorNeg - fadeColor;
}