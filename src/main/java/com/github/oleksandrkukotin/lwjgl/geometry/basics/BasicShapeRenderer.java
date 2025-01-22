package com.github.oleksandrkukotin.lwjgl.geometry.basics;

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

public class BasicShapeRenderer {

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
        int vbo = glGenBuffers();
        int ebo = glGenBuffers();
        float[] vertices = {
                0.0f, 0.0f,
                0.5f, 1.0f,
                1.0f, 0.0f,

                -0.1f, 0.0f,
                -0.5f, -1.0f,
                -1.0f, 0.0f
        };
        int[] indices = {
                0, 1, 2, 3, 4, 5
        };
        bindBuffersForTriangles(vbo, ebo, vertices, indices);

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
            float red = 0.1f;
            float green = Math.abs((float) Math.sin(color));
            float blue = Math.abs((float) Math.cos(color));
            drawTriangle(vbo, ebo, red, green, blue, indices);

            glfwSwapBuffers(window); // Swap the color buffers
            glfwPollEvents();
            color += 0.02f;
        }
    }

    private void bindBuffersForTriangles(int vbo, int ebo, float[] vertices, int[] indices) {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices.length).put(vertices).flip(), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createIntBuffer(indices.length).put(indices).flip(), GL_STATIC_DRAW);
    }

    private void drawTriangle(int vbo, int ebo, float red, float green, float blue, int[] indices) {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glVertexPointer(2, GL_FLOAT, 0, 0L);
        glColor3f(red, green, blue);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0L);
    }

    public static void main(String[] args) {
        new BasicShapeRenderer().run();
    }
}
