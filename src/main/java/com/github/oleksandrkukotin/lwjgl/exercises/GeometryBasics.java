package com.github.oleksandrkukotin.lwjgl.exercises;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.Callback;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

public class GeometryBasics {

    private long window;

    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWFramebufferSizeCallback fbCallback;
    private Callback debugProc;

    public void run() {
        initializeGLFW();
        setupAndInitializeOpenGLContext();
        createWindow();
        render();
    }

    private void initializeGLFW() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
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
        float[] vertices = {
                0.5f, 0.0f, 0.0f,
                -0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
        };

        FloatBuffer vertexBuffer = memAllocFloat(vertices.length);
        vertexBuffer.put(vertices).flip();

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);

            glColor3f(1.0f, 1.0f, 0.5f);

            glEnableClientState(GL_VERTEX_ARRAY);
            glVertexPointer(3, GL_FLOAT, 0, vertexBuffer);
            glDrawArrays(GL_TRIANGLES, 0, 3);
            glDisableClientState(GL_VERTEX_ARRAY);

            glfwSwapBuffers(window);
        }

        memFree(vertexBuffer);
        glfwDestroyWindow(window);
        keyCallback.free();
        fbCallback.free();
        if (debugProc != null)
            debugProc.free();
        glfwTerminate();
    }

    public static void main(String[] args) {
        new GeometryBasics().run();
    }
}
