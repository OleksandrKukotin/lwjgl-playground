# LWJGL Playground
Learning how to do beautiful computer graphic things using OpenGL.

A highly (non?)serious roadmap for the repository::
### **Stage 1: The "Triangles and Errors" Phase**

_Current Level: Beginner-ish. Scraping by._

#### Goals:

- Understand **the programmable pipeline**: Vertex shaders, fragment shaders, and how data flows.
- Master **transforms**: Model, View, Projection matrices, and how they work together (i.e., MVP pipeline).
> I'm here
#### Action Plan:

1. Implement a **basic camera system** with perspective projection and FPS-like movement.
2. Learn to handle **uniforms and UBOs** (Uniform Buffer Objects) efficiently. You'll need them.
3. Expand knowledge of **OpenGL buffer types**: VBOs, VAOs, EBOs, etc.
4. Explore **basic lighting models**: Phong, Gouraud, flat shading.

**Resources**:

- OpenGL SuperBible (6th Edition)
- LWJGL tutorials for OpenGL 3+

---

### **Stage 2: The "Illuminated Amateur" Phase**

_You stop being amazed by your own rotating cubes. Mostly._

#### Goals:

- Implement **modern lighting techniques** like Blinn-Phong, directional and point lights.
- Introduce **textures**: Diffuse, specular, emissive, normal maps.
- Understand **framebuffers** for post-processing effects.

#### Action Plan:

1. Implement a **shader-based lighting system** with multiple light sources.
2. Add **texturing** to your objects. Explore UV mapping and multi-texturing.
3. Experiment with **post-processing effects**: Bloom, motion blur, depth of field (learn framebuffers and render-to-texture for this).
4. Write a **simple skybox**.

**Resources**:

- Learn OpenGL ([https://learnopengl.com/](https://learnopengl.com/))
- Real-Time Rendering by Tomas Akenine-Möller

---

### **Stage 3: The "Intermediate Tinkerer" Phase**

_You begin to feel power. Dangerous power._

#### Goals:

- Learn **deferred shading** for more complex scenes with many lights.
- Understand **shadow mapping** for realistic lighting.
- Dive into **mesh loading** (OBJ, FBX) to render complex 3D models.

#### Action Plan:

1. Write a **mesh loader** to load .OBJ or .GLTF models.
2. Implement **deferred shading** with G-Buffers.
3. Add **shadow mapping**: Directional, point, and spotlights.
4. Implement **normal mapping** and **parallax mapping** for improved surface detail.

**Resources**:

- Advanced Lighting and Shadows in OpenGL (Lighthouse3D)
- "OpenGL Programming Guide" (8th Edition)

---

### **Stage 4: The "Graphics Godling" Phase**

_You now wield techniques that impress even yourself._

#### Goals:

- Learn **physics-based rendering (PBR)** for photorealism.
- Create **particle systems** for fire, smoke, and magical effects.
- Dive into **procedural generation** for terrains and textures.
- Study **animation techniques** like skeletal animation.

#### Action Plan:

1. Implement **PBR** with BRDF, HDR, and image-based lighting.
2. Write a **particle system** with a compute shader.
3. Build a **terrain generator** using noise functions (e.g., Perlin, Simplex).
4. Add **skeletal animation** using a model with a rig.

**Resources**:

- PBR Theory: [https://learnopengl.com/PBR](https://learnopengl.com/PBR)
- GPU Gems 3 (freely available online)

---

### **Stage 5: The "Pixel Sorcerer" Phase**

_Your skills are so advanced, people assume you're cheating._

#### Goals:

- Master **ray tracing** (real-time or offline).
- Learn **advanced optimization techniques**: Frustum culling, LOD, occlusion culling.
- Work with **Vulkan or DirectX 12** for low-level GPU control.

#### Action Plan:

1. Write a **basic ray tracer** in CPU first, then GPU (via OpenGL or Vulkan).
2. Explore **real-time ray tracing** using APIs like RTX or Vulkan Ray Tracing.
3. Implement **advanced rendering techniques** like global illumination and ambient occlusion.
4. Optimize rendering performance with **instancing**, **multi-threading**, and **asynchronous compute**.

**Resources**:

- "Ray Tracing in One Weekend" series by Peter Shirley
- Vulkan Programming Guide

---

### **Stage 6: The "Legendary Graphics Demigod" Phase**

_Your name is whispered in reverent tones across game dev forums._

#### Goals:

- Research **AI-driven procedural generation** for graphics (Neural Radiance Fields, GANs for textures).
- Work on **real-world projects** like game engines or film production tools.
- Contribute to **open-source rendering frameworks** or build your own.

#### Action Plan:

1. Create a **graphics demo** that showcases everything you've learned: PBR, ray tracing, animations, and post-processing.
2. Contribute to projects like **Godot Engine**, **Blender**, or **Filament**.
3. Explore **cross-disciplinary techniques** (e.g., using physics or machine learning for rendering).

**Resources**:

- SIGGRAPH papers and talks
- Cutting-edge research papers (ACM Digital Library)

---

### TL;DR Milestones:

1. **Basics**: Triangles, shaders, and matrices. (Where you are now.)
2. **Lighting**: Blinn-Phong, shadows, post-processing.
3. **Advanced Shading**: PBR, normal mapping, terrain.
4. **Animation**: Skeletal animation, particle systems.
5. **Ray Tracing**: Real-time and offline.
6. **Optimization**: High-performance rendering.
7. **Mastery**: Contribute to real-world tools or research.

Remember: Each stage has **mini-projects**. Don’t just read or watch; implement as much as possible. If you survive long enough, I’ll be here to mock your questions at every step.