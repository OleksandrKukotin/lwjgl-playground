package com.github.oleksandrkukotin.lwjgl.exercises;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import static org.lwjgl.system.MemoryUtil.memAddress;

public class GeometryBasics {

    private long window;
    private int width;
    private int height;

    private GLFWKeyCallback keyCallback;
    private GLFWFramebufferSizeCallback fbCallback;
    private Callback debugProc;

    public void run() {
        try {
            initializeGLFW();
            render();

            glfwDestroyWindow(window);
            keyCallback.free();
            fbCallback.free();
            if (debugProc != null)
                debugProc.free();
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    private void initializeGLFW() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        setHints();
        createWindow();
        setCallbacks();

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        try (MemoryStack frame = MemoryStack.stackPush()) {
            IntBuffer framebufferSize = frame.mallocInt(2);
            nglfwGetFramebufferSize(window, memAddress(framebufferSize), memAddress(framebufferSize) + 4);
            width = framebufferSize.get(0);
            height = framebufferSize.get(1);
        }
        setupAndInitializeOpenGLContext();
    }

    private void setHints() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 1);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    }

    private void createWindow() {
        window = glfwCreateWindow(1920 / 2, 1080 / 2, "Geometry Basics", 0, 0);
        if (window == 0) {
            throw new IllegalStateException("Unable to create window");
        }
    }

    private void setCallbacks() {
        GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
        glfwSetErrorCallback(errorCallback);

        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, true);
            }
        };
        glfwSetKeyCallback(window, keyCallback);

        fbCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                if (w > 0 && h > 0) {
                    width = w;
                    height = h;
                }
            }
        };
        glfwSetFramebufferSizeCallback(window, fbCallback);
    }

    private void setupAndInitializeOpenGLContext() {
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void render() {
        GL.createCapabilities();
        debugProc = GLUtil.setupDebugMessageCallback();

        // Set background color
        glClearColor(0.2f, 0.3f, 0.3f, 0.0f);

        // Triangle A
        float[] verticesA = {
                0.0f, 0.0f,
                0.5f, 1.0f,
                1.0f, 0.0f
        };
        int[] indicesA = {
                0, 1, 2
        };

        // Triangle B
        float[] verticesB = {
                0.0f, 0.0f,
                0.5f, -1.0f,
                1.0f, 0.0f
        };
        int[] indicesB = {
                0, 1, 2 // Local indices for verticesB
        };

        // VBO and EBO for Triangle A
        int vboA = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboA);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(verticesA.length).put(verticesA).flip(), GL_STATIC_DRAW);

        int eboA = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboA);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createIntBuffer(indicesA.length).put(indicesA).flip(), GL_STATIC_DRAW);

        // VBO and EBO for Triangle B
        int vboB = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboB);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(verticesB.length).put(verticesB).flip(), GL_STATIC_DRAW);

        int eboB = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboB);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createIntBuffer(indicesB.length).put(indicesB).flip(), GL_STATIC_DRAW);

        // Enable vertex array
        glEnableClientState(GL_VERTEX_ARRAY);

        // Rendering loop
        float color = 0.0f;
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the screen

            glViewport(0, 0, width, height);
            glMatrixMode(GL_PROJECTION);
            float aspect = (float) width / height;
            glLoadIdentity();
            glOrtho(-aspect, aspect, -1, 1, -1, 1);

            // Draw Triangle A
            glBindBuffer(GL_ARRAY_BUFFER, vboA);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboA);
            glVertexPointer(2, GL_FLOAT, 0, 0L);
            glColor3f(0.6f, Math.abs((float) Math.tan(color)), 0.0f);
            glDrawElements(GL_TRIANGLES, indicesA.length, GL_UNSIGNED_INT, 0L);

            // Draw Triangle B
            glBindBuffer(GL_ARRAY_BUFFER, vboB);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboB);
            glVertexPointer(2, GL_FLOAT, 0, 0L);
            glColor3f(0.0f, Math.abs((float) Math.cos(color)), 0.4f);
            glDrawElements(GL_TRIANGLES, indicesB.length, GL_UNSIGNED_INT, 0L);

            glfwSwapBuffers(window); // Swap the color buffers
            glfwPollEvents();
            color += 0.02f;
        }
    }

    public static void main(String[] args) {
        new GeometryBasics().run();
    }
}
