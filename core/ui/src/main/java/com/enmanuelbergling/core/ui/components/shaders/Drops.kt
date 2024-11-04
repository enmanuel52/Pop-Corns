package com.enmanuelbergling.core.ui.components.shaders

import org.intellij.lang.annotations.Language

@Language("AGSL")
val VDropTunnel = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    
    // V-Drop - Del 19/11/2019 - (Tunnel mix - Enjoy)
    // vertical version: https://www.shadertoy.com/view/tdGXWm
    const float PI= 3.14159;

    float vDrop(float2 uv,float t)
    {
        uv.x = uv.x*128.0;						// H-Count
        float dx = fract(uv.x);
        uv.x = floor(uv.x);
        uv.y *= 0.05;							// stretch
        float o=sin(uv.x*215.4);				// offset
        float s=cos(uv.x*33.1)*.3 +.7;			// speed
        float trail = mix(95.0,35.0,s);			// trail length
        float yv = fract(uv.y + t*s + o) * trail;
        yv = 1.0/yv;
        yv = smoothstep(0.0,1.0,yv*yv);
        yv = sin(yv*PI)*(s*5.0);
        float d2 = sin(dx*PI);
        return yv*(d2*d2);
    }

    float4 main( in float2 fragCoord )
    {
        float2 p = (fragCoord.xy - 0.5 * resolution.xy) / resolution.y;
        float d = length(p)+0.1;
    	p = float2(atan(p.x, p.y) / PI, 2.5 / d);
//        if (iMouse.z>0.5)
//        	p.y *= 0.5;
        float t =  time*0.4;
        float3 col = float3(1.55,0.65,.225) * vDrop(p,t);	// red
        col += float3(0.55,0.75,1.225) * vDrop(p,t+0.33);	// blue
        col += float3(0.45,1.15,0.425) * vDrop(p,t+0.66);	// green
    	float4 fragColor = float4(col*(d*d), 1.0);
    
        //value doesn't change
        if( fragColor == float4(0., 0., 0., 1.) ){
            return float4(backgroundColor.rgb, 1.0);
        } else{
            return fragColor;
        }
    }
""".trimIndent()