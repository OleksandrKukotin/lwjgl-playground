# **TripTangle**

**TripTangle** is a real-time audio visualization tool built with **Java** and **OpenGL**, designed to turn system audio into captivating visual experiences. Inspired by retro visualizers from the Windows Media Player and Winamp days, TripTangle brings the trippy magic back, powered by modern programming practices.

## **Features**
- 🎵 **Audio Capture**: Utilizes the `javax.sound.sampled` API to process real-time system audio.
- 🎨 **OpenGL Rendering**: Stunning visuals rendered through LWJGL (Lightweight Java Game Library).
- 🌟 **Extensible Design**: Modular architecture makes it easy to add new features or visuals.

## **Project Structure**
```plaintext
src/main/java
├── com.github.oleksandrkukotin
│   ├── helloworld               // Sample LWJGL demos
│   │   └── DrawElementsDemo.java
│   └── triptangle               // TripTangle's main codebase
│       ├── AudioCapture.java    // Captures and processes audio input
│       ├── Renderer.java        // OpenGL-based visual rendering
│       └── TripTangleApplication.java // Main class connecting AudioCapture and Renderer
```

## **Getting Started**
### **Prerequisites**
1. **Java Development Kit (JDK 11+)**
2. **Gradle** (or use the provided Gradle wrapper)
3. **LWJGL** (Lightweight Java Game Library) - already included via Gradle.

### **Setup Instructions**
1. Clone the repository:
   ```bash
   git clone https://github.com/<your-username>/lwjgl-playground.git
   cd lwjgl-playground
   ```

2. Build the project using Gradle:
   ```bash
   ./gradlew build
   ```

3. Run the application:
   ```bash
   ./gradlew run
   ```

4. Enjoy the trippy visuals synced with your system's audio output.

---

## **How It Works**
- **AudioCapture**:
    - Captures audio data using the `javax.sound.sampled` API.
    - Processes the data (e.g., amplitude) for visualization.

- **Renderer**:
    - Renders the processed data into visual effects using OpenGL (via LWJGL).
    - Includes dynamic shapes, rotations, and color changes.

- **TripTangleApplication**:
    - Acts as the central coordinator between audio capture and rendering.

---

## **Development Notes**
This repository also includes some basic OpenGL demos for practice and experimentation. Feel free to explore the `helloworld` folder if you're just getting started with LWJGL.

---

## **Future Plans**
- 🎛️ **FFT Analysis**: Visualize audio frequencies for more advanced effects.
- ✨ **Shaders**: Use modern OpenGL shaders for smoother, more complex visuals.
- 🎨 **Custom Effects**: Add customizable visualization options.
- 🌐 **Cross-Platform Audio**: Improve compatibility across different operating systems.

---

## **Contributing**
Contributions are welcome! Here’s how you can help:
1. Fork the repo.
2. Create a feature branch.
3. Submit a pull request with your improvements.

---

## **Acknowledgments**
- Built using **LWJGL** and **Java Sound API**.
- Inspired by classic media player visualizers.