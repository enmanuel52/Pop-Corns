package com.enmanuelbergling.core.ui.components.shaders

import org.intellij.lang.annotations.Language


@Language("AGSL")
val BubbleGum = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
     
     float roundLookingBlob(float2 fragCoord, float2 tPos, float r) {
         float2 pos = fragCoord.xy/resolution.yy - float2(0.5);
         pos.x -= ((resolution.x-resolution.y)/resolution.y)/2.0;
         return pow(max(1.0-length(pos-tPos), 0.0) , r);
     }

     float4 main( in float2 fragCoord )
     {
      float v = 0.0 
             + roundLookingBlob(fragCoord * 0.2,float2(sin(time)* 2.0, cos(time)*0.004), 10.0)
          + roundLookingBlob(fragCoord,float2(sin(time*0.6)*0.2, cos(time)*0.3), 7.0)
          + roundLookingBlob(fragCoord,float2(cos(time*0.8)*0.3, sin(time*1.1)*0.04), 5.0)
          + roundLookingBlob(fragCoord,float2(cos(time*0.2)*0.2, sin(time*0.9)*0.05), 8.0)
          + roundLookingBlob(fragCoord,float2(cos(time*1.2)*0.2, 2.0 *sin(time*0.9)*0.05), 8.0)
             + roundLookingBlob(fragCoord,float2(cos(time*0.3)*0.4, sin(time*1.1)*0.4), 5.0)
          + roundLookingBlob(fragCoord,float2(sin(time*0.6)*0.9, cos(time)*0.3), 7.0)
          + roundLookingBlob(fragCoord,float2(sin(time*0.6)*0.3, cos(time)*0.8), 7.0)
             + roundLookingBlob(fragCoord,float2(cos(time*0.3)*0.9, sin(time*0.1)*0.4), 3.0)
             ;
         v = clamp((v-0.5)*1000.0, 0.0, 1.0);
         //v = 1.0;
         //float color = cos(time * fragCoord.x) * 1.0;
         //v *= color;
         //float r = 1.0 + fragCoord.y *sin(time * 0.5) +  0.0001 * (2.0 * sin(time * 1.0)) ;
         float r = 
             -1.0 * 1.0 *sin(time) 
             - 2.0* cos(1.0 * time) * fragCoord.x / resolution.x * fragCoord.y / resolution.y;
         float g = 0.0 - 0.5 * cos(2.0 * time) *  fragCoord.y / resolution.y; //1.0* sin(time) - r + 0.8;
         float b = 4.0 + sin(time) - g + 0.8;
         
        float4 fragColor = float4(r * v, v * g, v * b, 1.);
        if(fragColor == float4(0., 0., 0., 1.)){
            fragColor = float4(backgroundColor.rgb, 1.);
        }
        
        return fragColor;
     }
""".trimIndent()