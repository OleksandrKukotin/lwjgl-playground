package com.github.oleksandrkukotin.lwjgl.core;

import com.github.oleksandrkukotin.lwjgl.geometry.matrices.exception.ShaderCompileException;
import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static glm_.Java.glm;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.memAddress;

public class SimpleCameraController {

    private static final String VERTEX_SHADER_SOURCE = """
            #version 330 core
            layout(location = 0) in vec2 position;
            
            uniform mat4 model;
            uniform mat4 view;
            uniform mat4 projection;
            
            void main() {
                gl_Position = projection * view * model * vec4(position, 0.0, 1.0);
            }
            """;

    private static final String FRAGMENT_SHADER_SOURCE = """
            #version 330 core
            out vec4 FragColor;
            uniform vec3 color;
            void main() {
                FragColor = vec4(color, 1.0);
            }
            """;

    private long window;
    private int width;
    private int height;

    private Vec3 cameraPos = new Vec3(0.0f, 0.0f, 3.0f);
    private Vec3 cameraFront = new Vec3(0.0f, 0.0f, -1.0f);
    private Vec3 cameraUp = new Vec3(0.0f, 1.0f, 3.0f);
    private float cameraSpeed = 0.1f;
    private float yaw = -90.0f;
    private float pitch = 0.0f;
    private float sensitivity = 0.1f;

    private GLFWKeyCallback keyCallback;
    private GLFWFramebufferSizeCallback framebufferSizeCallback;
    private Callback debugProc;

    public void run() {
        try {
            initializeGLFW();
            render();

            glfwDestroyWindow(window);
            keyCallback.free();
            framebufferSizeCallback.free();
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
            IntBuffer frameBufferSize = frame.mallocInt(2);
            nglfwGetFramebufferSize(window, memAddress(frameBufferSize), memAddress(frameBufferSize) + 4);
            width = frameBufferSize.get(0);
            height = frameBufferSize.get(1);
        }
        setupAndInitializeOpenGLContext();
    }

    private void setHints() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
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
                if (action == GLFW_PRESS || action == GLFW_REPEAT) {
                    switch (key) {
                        case GLFW_KEY_UP:
                            cameraPos = cameraPos.plus(cameraFront.times(cameraSpeed));
                            break;
                        case GLFW_KEY_DOWN:
                            cameraPos = cameraPos.minus(cameraFront.times(cameraSpeed));
                            break;
                        case GLFW_KEY_LEFT:
                            cameraPos = cameraPos.minus(cameraFront.cross(cameraUp).normalize().times(cameraSpeed));
                            break;
                        case GLFW_KEY_RIGHT:
                            cameraPos = cameraPos.plus(cameraFront.cross(cameraUp).normalize().times(cameraSpeed));
                            break;
                        case GLFW_KEY_ESCAPE:
                            glfwSetWindowShouldClose(window, true);
                            break;
                    }
                }
            }
        };
        glfwSetKeyCallback(window, keyCallback);

        framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                if (w > 0 && h > 0) {
                    width = w;
                    height = h;
                }
            }
        };
        glfwSetFramebufferSizeCallback(window, framebufferSizeCallback);
    }

    private void setupAndInitializeOpenGLContext() {
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void render() {
        GL.createCapabilities();
        debugProc = GLUtil.setupDebugMessageCallback();

        glClearColor(0.2f, 0.1f, 0.5f, 0.0f);

        int shaderProgram = createShaderProgram();

        int vbo = glGenBuffers();
        int ebo = glGenBuffers();
        int vao = glGenVertexArrays();
        float[] vertices = {
                0.0f, 0.0f,
                0.5f, 1.0f,
                1.0f, 0.0f,

                0.0f, 0.0f,
                -0.5f, -1.0f,
                -1.0f, 0.0f
        };
        int[] indices = {0, 1, 2, 3, 4, 5};
        bindBuffersForTriangles(vao, vbo, ebo, vertices, indices);

        glUseProgram(shaderProgram);

        float color = 0.0f;
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glViewport(0, 0, width, height);

            Mat4 model = new Mat4(1.0f);
            model.rotate(color, new Vec3(0.0f, 0.0f, color));
            model.scale(new Vec3(1.0f, 1.0f, 1.0f));

            Mat4 view = glm.lookAt(cameraPos, cameraPos.plus(cameraFront), cameraUp);
            int viewLocation = glGetUniformLocation(shaderProgram, "view");
            glUniformMatrix4fv(viewLocation, false, view.to(BufferUtils.createFloatBuffer(16)));

            Mat4 projection = glm.perspective(
                    (float) Math.toRadians(45.0f),
                    (float) width / height,
                    0.1f,
                    100.0f
            );
            int projectionLocation = glGetUniformLocation(shaderProgram, "projection");
            glUniformMatrix4fv(projectionLocation, false, projection.to(BufferUtils.createFloatBuffer(16)));

            FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
            model.to(matrixBuffer);
            int modelLocation = glGetUniformLocation(shaderProgram, "model");
            glUniformMatrix4fv(modelLocation, false, matrixBuffer);

            int colorLocation = glGetUniformLocation(shaderProgram, "color");
            glUniform3f(colorLocation, 0.5f, Math.abs((float) Math.sin(color)), Math.abs((float) Math.cos(color)));

            glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0L);

            glfwSwapBuffers(window);
            glfwPollEvents();
            color += 0.05f;
        }
    }

    private int createShaderProgram() {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, VERTEX_SHADER_SOURCE);
        glCompileShader(vertexShader);
        checkShaderCompileStatus(vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, FRAGMENT_SHADER_SOURCE);
        glCompileShader(fragmentShader);
        checkShaderCompileStatus(fragmentShader);

        int program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            throw new ShaderCompileException("Error during shader program compilation occurred: "
                    + glGetProgramInfoLog(program));
        }

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        return program;
    }

    private void checkShaderCompileStatus(int vertexShader) {
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new ShaderCompileException("Error during shader compilation occurred: " + glGetShaderi(vertexShader, GL_COMPILE_STATUS));
        }
    }

    private void bindBuffersForTriangles(int vao, int vbo, int ebo, float[] vertices, int[] indices) {
        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices.length).put(vertices).flip(), GL_STATIC_DRAW);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * Float.BYTES, 0L);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createIntBuffer(indices.length).put(indices).flip(), GL_STATIC_DRAW);
    }

    public static void main(String[] args) {
        new SimpleCameraController().run();
    }
}
