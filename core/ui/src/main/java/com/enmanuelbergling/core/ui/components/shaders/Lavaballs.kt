package com.enmanuelbergling.core.ui.components.shaders

import org.intellij.lang.annotations.Language

@Language("AGSL")
val LavaBallsShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    
    float GetBallValue(float3 ball, float2 uv) {
        return ball.z/((ball.x-uv.x)*(ball.x-uv.x) + ((ball.y-uv.y)*(ball.y-uv.y)));
    }

    float4 main(in float2 fragCoord ){
        float2 uv = fragCoord/resolution.xy;
        uv.x *= resolution.x / resolution.y;

        float3 ball1 = float3(0.9 + 0.5*cos(0.5*time),  0.5 + 0.3*sin(0.5*time), 0.02);
        float3 ball2 = float3(0.9 + 0.5*cos(0.37*time), 0.5 - 0.3*sin(0.2*time), 0.02);
        float3 ball3 = float3(0.9 + 0.5*cos(0.43*time), 0.5 + 0.3*sin(0.24*time), 0.01);

        float ball= GetBallValue(ball1, uv)+ GetBallValue(ball2, uv)+ GetBallValue(ball3, uv);

        float4 fragColor = float4(0.1, 0.1, 0.1, 1);
        fragColor += smoothstep(0.95, 1.0, ball) * 1.4 * float4(0.5 + 0.5*cos(0.5*time+uv.xyx+float3(0.0,2.0,4.0)), 0);
    
        //value doesn't change
        if( fragColor == float4(0.1, 0.1, 0.1, 1) ){
            return float4(backgroundColor.rgb, 1.0);
        } else{
            return fragColor;
        }
      
    }
""".trimIndent()