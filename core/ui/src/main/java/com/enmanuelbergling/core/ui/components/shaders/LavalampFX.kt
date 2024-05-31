package com.enmanuelbergling.core.ui.components.shaders

import org.intellij.lang.annotations.Language

@Language("AGSL")
val LavalampFXShader = """
    //this is breaking the animation
//    #extension GL_OES_standart_derivates : enable
    
    uniform float2 resolution;
    uniform float time;
    
    // Number of circles.
    const int CIRCLES = 16;

    // Circles radius (same radius for all circles).
    float R = 0.1;

    // Apply anti-aliasing.
    bool AA = true;

    float3 DrawMeta(float2 c[CIRCLES], float2 uv) {
        
        float plotArea = .0; // Amount of circles contribution.
        
        // For each circle (with R radius), sum the "contribution".
        for(int i = 0; i < CIRCLES; i++)
            plotArea += (R * R) / dot(uv - c[i], uv - c[i]);

        // Interpolate background colour with metaballs colour.
        return mix(float3(0.05),                         // Dark colour.
               float3(1.0, uv.y, sin(time + uv.y)), // Metaball colour.
                   
               // If anti-aliasing is enabled, 
               // Anti-aliasing interpolation value (from FabriceNeyret2).
                AA? smoothstep(0., 1.5, (plotArea-1.) / min(1.,  abs(dFdx(plotArea)) + abs(dFdy(plotArea)) )) : //fwidth(plotArea) or abs(dFdx(plotArea)) + abs(dFdy(plotArea))
                step(1.0, plotArea) //else, fixed interpolation value.
        );
    }


    float4 main(in float2 fragCoord ){
       
        float3 col;         // Declare final colour floattor.
        float2 c[CIRCLES];  // Declare an array of circles origin point.

        // Center and normalize coordinates (no squash).
        float2 uv = (-resolution.xy + 2.0 * fragCoord) / resolution.y;
        
        // First half of points movement behaviour.
        for(int i = 0; i < CIRCLES / 2; i++) {
            c[i].x = sin((time * float(i + 1)) *.3);
            c[i].y = 0.3;
        }

        // Second half of points movement behaviour.
        for(int i = CIRCLES / 2; i < CIRCLES; i++) {
            c[i].x = cos((time * float(i + 1)) *.1);
            c[i].y = -.3 * cos((time * float(i + 3)) *.2);
        }
        
        col = DrawMeta(c, uv);      // Pass points state to draw metaballs.
        float4 fragColor = float4(col, 1.0); // Display.
        
        return fragColor;
    }
""".trimIndent()