package com.enmanuelbergling.core.ui.components.shaders

import org.intellij.lang.annotations.Language

//working fine
@Language("AGSL")
val TileableWaterCaustic = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    layout(color) uniform half4 primaryColor;
    
    const float TAU =6.28318530718;
    const int MAX_ITER= 5;
    bool SHOW_TILING = true;

    float4 main( in float2 fragCoord ) 
    {
     float time2 = time * .5+23.0;
        // uv should be the 0-1 uv of texture...
     float2 uv = fragCoord.xy / resolution.xy;
     
     float2 p = float2(0.);
        
    if (SHOW_TILING)
     {p = mod(uv*TAU*2.0, TAU)-250.0;}
    else
        {p = mod(uv*TAU, TAU)-250.0;}
    
     float2 i = float2(p);
     float c = 1.0;
     float inten = .005;

     for (int n = 0; n < MAX_ITER; n++) 
     {
      float t = time2 * (1.0 - (3.5 / float(n+1)));
      i = p + float2(cos(t - i.x) + sin(t + i.y), sin(t - i.y) + cos(t + i.x));
      c += 1.0/length(float2(p.x / (sin(i.x+t)/inten),p.y / (cos(i.y+t)/inten)));
     }
     c /= float(MAX_ITER);
     c = 1.17-pow(c, 1.4);
     float3 colour = float3(pow(abs(c), 8.0));
        colour = clamp(colour + backgroundColor.rgb, 0.0, 1.0); //default color float3(0.0, 0.35, 0.5)

     if (SHOW_TILING){
     // Flash tile borders...
     float2 pixel = 2.0 / resolution.xy;
     uv *= 2.0;
     float f = floor(mod(time*.5, 2.0));  // Flash value.
     float2 first = step(pixel, uv) * f;      // Rule out first screen pixels and flash.
     uv  = step(fract(uv), pixel);    // Add one line of pixels per tile.
     //colour = mix(colour, float3(1.0, 1.0, 0.0), (uv.x + uv.y) * first.x * first.y); // Yellow line
     }
        
     return float4(colour, 1.0);
    }
""".trimIndent()

@Language("AGSL")
val WavesShader = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    layout(color) uniform half4 primaryColor;
    
//    float3 COLOR1 = float3(0.0, 0.0, 0.3);
//    float3 COLOR2 = float3(0.5, 0.0, 0.0);
//    float BLOCK_WIDTH = 0.01;

    float4 main( in float2 fragCoord )
    {
     float2 uv = fragCoord.xy / resolution.xy;
     
     // To create the BG pattern
//     float3 final_color = float3(1.0);
//     float3 bg_color = float3(0.0);
     float3 wave_color = float3(0.0);
//     
//     float c1 = mod(uv.x, 2.0 * BLOCK_WIDTH);
//     c1 = step(BLOCK_WIDTH, c1);
//     
//     float c2 = mod(uv.y, 2.0 * BLOCK_WIDTH);
//     c2 = step(BLOCK_WIDTH, c2);
//     
//     bg_color = mix(uv.x * COLOR1, uv.y * COLOR2, c1 * c2);
     
     
     // To create the waves
     float wave_width = 0.01;
     uv  = -1.0 + 2.0 * uv;
     uv.y += 0.1;
     for(float i = 0.0; i < 10.0; i++) {
      
      uv.y += (0.07 * sin(uv.x + i/7.0 + time ));
      wave_width = abs(1.0 / (150.0 * uv.y));
      wave_color += float3(wave_width * primaryColor.r, wave_width * primaryColor.g, wave_width * primaryColor.b); //color 1.9, 1, 1.5
     }
     
     
     if(wave_color == float3(0.0)){
        return float4(backgroundColor.rgb, 1.);
     }
     
     return float4(wave_color, 1.0);
    }
""".trimIndent()

//it needs iChannel for texture
@Language("AGSL")
val WavesRemix = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    
    float squared(float value) { return value * value; }

    float getAmp(float frequency) { return texture(iChannel0, float2(frequency / 512.0, 0)).x; }

    float getWeight(float f) {
        return (+ getAmp(f-2.0) + getAmp(f-1.0) + getAmp(f+2.0) + getAmp(f+1.0) + getAmp(f)) / 5.0; }

    float4 main( in float2 fragCoord )
    {    
     float2 uvTrue = fragCoord.xy / resolution.xy;
        float2 uv = -1.0 + 2.0 * uvTrue;
        
     float lineIntensity;
        float glowWidth;
        float3 color = float3(0.0);
        
     for(float i = 0.0; i < 5.0; i++) {
            
      uv.y += (0.2 * sin(uv.x + i/7.0 - time * 0.6));
            float Y = uv.y + getWeight(squared(i) * 20.0) *
                (texture(iChannel0, float2(uvTrue.x, 1)).x - 0.5);
            lineIntensity = 0.4 + squared(1.6 * abs(mod(uvTrue.x + i / 1.3 + time,2.0) - 1.0));
      glowWidth = abs(lineIntensity / (150.0 * Y));
      color += float3(glowWidth * (2.0 + sin(time * 0.13)),
                          glowWidth * (2.0 - sin(time * 0.23)),
                          glowWidth * (2.0 - cos(time * 0.19)));
     } 
     
     return float4(color, 1.0);
    }
""".trimIndent()

//it needs iChannel for texture
@Language("AGSL")
val WavesSoundVirtualizer = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    
    float squared(float value) { return value * value; }

    float getAmp(float frequency) { return texture(iChannel0, float2(frequency / 512.0, 0)).x; }

    float getWeight(float f) {
        return (+ getAmp(f-2.0) + getAmp(f-1.0) + getAmp(f+2.0) + getAmp(f+1.0) + getAmp(f)) / 5.0; }

    void main( in float2 fragCoord )
    {    
     float2 uvTrue = fragCoord.xy / resolution.xy;
        float2 uv = -1.0 + 2.0 * uvTrue;
        
     float lineIntensity;
        float glowWidth;
        float3 color = float3(0.0);
        
        float i = 0.0;
        
     for(float i = 0.0; i < 5.0; i++) {
            
      uv.y += (0.2 * sin(uv.x + i/7.0 - time * 0.6));
            float Y = uv.y + getWeight(squared(i) * 20.0) *
                (texture(iChannel0, float2(uvTrue.x, 1)).x - 0.5);
            lineIntensity = 0.4 + squared(1.6 * abs(mod(uvTrue.x + i / 1.3 + time,2.0) - 1.0));
      glowWidth = abs(lineIntensity / (150.0 * Y));
      color += float3(glowWidth * (2.0 + sin(time * 0.13)),
                          glowWidth * (2.0 - sin(time * 0.23)),
                          glowWidth * (2.0 - cos(time * 0.19)));
     } 
     
     return float4((color / 2.0) * (getWeight(squared(i) * 20.0) * 2.5), 1.0);
    }
""".trimIndent()