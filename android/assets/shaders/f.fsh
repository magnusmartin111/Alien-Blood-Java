varying lowp vec4 v_color;
varying lowp vec2 v_texCoord0;

uniform sampler2D u_sampler2D;
uniform lowp float time;
uniform lowp vec2 resolution;
uniform lowp vec2 offset;
uniform lowp float touches[9];
uniform lowp vec3 colors;

void main() {

	lowp vec2 p = v_texCoord0*8.0 + offset;
	lowp vec2 i = p;
	lowp float c = 0.0;
	lowp float inten = 0.01;
	lowp float r = length(p+vec2(sin(time),sin(time*0.433+2.))*1.);

    lowp vec3 shockParams = vec3(1.25, 0.2, 0.8);

    for (lowp int s = 0; s < 7; s += 3) {

        lowp float timex = touches[(s+2)];
        lowp vec2 center = vec2(touches[s], resolution.y-touches[(s+1)])/resolution*8.0 + offset;
        lowp float distance = distance(p, center);

        if ( (distance <= (timex  + shockParams.z)) && (distance >= (timex - shockParams.z)) ) {
            lowp float diff = (distance - timex);
            lowp float powDiff = 1.0 - pow(abs(diff * shockParams.x), shockParams.y);
            lowp float diffTime = diff  * powDiff;
            lowp vec2 diffUV = normalize(p - center);
            p -= diffUV * diffTime * (10.0 - timex * 2.0) * 2.0;
        }
    }

	for (lowp float n = 0.0; n < 2.0; n++) {
		lowp float t = r-time * (1.0 - (1.9 / (n+1.)));
		t = r-time/(n+0.6);//r-time * (1.0 + (0.5 / float(n+1.)));
		i -= p - vec2(cos(t - i.x-r) + sin(t + i.y),sin(t - i.y) + cos(t + i.x)+r);
		c += 1.5/length(vec2((sin(i.x+t)/inten),(cos(i.y+t)/inten)));
	}

    c *= 5.0;
	c -= 0.006;

    lowp vec4 color = vec4( colors.x * c * p.y, colors.y * c, colors.z * c * p.x, 1.0);

    gl_FragColor = texture2D(u_sampler2D,v_texCoord0) * color;
}