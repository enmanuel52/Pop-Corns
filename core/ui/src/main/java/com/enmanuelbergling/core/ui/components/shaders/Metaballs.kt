package com.enmanuelbergling.core.ui.components.shaders

import org.intellij.lang.annotations.Language


@Language("AGSL")
val OldSchoolMetaballShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    
    const float PI = 3.141592653589;
    
    float drawCircle(float2 U, float2 P, float r) 
    { 
     return smoothstep(0.0, length(U - P), r);
    }

    float4 main( in float2 fragCoord)
    {
        float2 U = (2.0*fragCoord - resolution.xy)/resolution.y;
        
        
        float2 pos[10];
        float r[10];
        
        float a, b, A, B, delta; // Lissajous parameters
        
        float4 fragColor = float4(0.);
        
        float t = time/2.;
        for(int i=2; i<10; i++)
        {
            float s = 1.0/(mod(float(i),5.)*3.0);
            a = float(i+2); b = float(i+1); A = 1.1*(sin(t))+0.5; B = 0.6*(cos(t))+0.5; delta = PI/2.0;
            
         pos[i] = float2(A*sin(a*t*s+delta), B*cos(b*t*s));  
         r[i] = 0.3*s;
            
            fragColor += float4(drawCircle(U, pos[i], r[i]));
        }
        
        fragColor = min(fragColor, 0.8);
        fragColor = smoothstep(0.2, 0.8, fragColor);

        float3 col = float3(abs(sin(time*0.2)+0.1), 1.0, 0.5);
//        col = hsl2rgb(col);
        fragColor *= 2.*float4(col,1.0);
        
        // Apply gamma correction
        fragColor = float4(pow(fragColor.xyz, float3(0.4545)),1.0);  
        
        return fragColor;
    }
""".trimIndent()


@Language("AGSL")
val Metaball = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    
""".trimIndent()
