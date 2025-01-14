package com.github.oleksandrkukotin.lwjgl.exercises;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryUtil.memAddress;

public class GeometryBasics {

    private long window;
    private int width, height;

    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWFramebufferSizeCallback fbCallback;
    private Callback debugProc;

    public void run() {
        try {
            initializeGLFW();
            setupAndInitializeOpenGLContext();
            createWindow();
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
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(1920/2, 1080/2, "Hello World!", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, true);
            }
        });
        glfwSetFramebufferSizeCallback(window, fbCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                if (w > 0 && h > 0) {
                    width = w;
                    height = h;
                }
            }
        });

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        try (MemoryStack frame = MemoryStack.stackPush()) {
            IntBuffer framebufferSize = frame.mallocInt(2);
            nglfwGetFramebufferSize(window, memAddress(framebufferSize), memAddress(framebufferSize) + 4);
            width = framebufferSize.get(0);
            height = framebufferSize.get(1);
        }
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void createWindow() {
        window = glfwCreateWindow(1920/2, 1080/2, "Geometry Basics", 0, 0);
        if (window == 0) {
            throw new IllegalStateException("Unable to create window");
        }
    }

    private void setupAndInitializeOpenGLContext() {
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();
    }

    private void render() {
        int vbo = glGenBuffers();
        int ibo = glGenBuffers();
        float[] vertices = {
                0.1f, 0.0f, 0.0f,
                -0.1f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
        };

        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices.length).put(vertices).flip(), GL_STATIC_DRAW);
        glEnableClientState(GL_VERTEX_ARRAY);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glVertexPointer(2, GL_FLOAT, 0, 0L);

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);

            glColor3f(1.0f, 1.0f, 0.5f);

            glEnableClientState(GL_VERTEX_ARRAY);
            glDrawArrays(GL_TRIANGLES, 0, 3);
            glDisableClientState(GL_VERTEX_ARRAY);

            glfwSwapBuffers(window);
        }
    }

    public static void main(String[] args) {
        new GeometryBasics().run();
    }
}
