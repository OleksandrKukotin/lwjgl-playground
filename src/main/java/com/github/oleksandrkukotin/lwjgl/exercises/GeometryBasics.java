package com.github.oleksandrkukotin.lwjgl.exercises;

import org.lwjgl.opengl.GL;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

public class GeometryBasics {

    private long window;

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

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
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
        glfwTerminate();
    }

    public static void main(String[] args) {
        new GeometryBasics().run();
    }
}
