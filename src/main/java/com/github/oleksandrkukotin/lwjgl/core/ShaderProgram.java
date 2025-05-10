package com.github.oleksandrkukotin.lwjgl.core;

import com.github.oleksandrkukotin.lwjgl.geometry.matrices.exception.ShaderCompileException;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

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

    public int create() {
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
            throw new ShaderCompileException("Error during shader compilation occurred: " + glGetShaderInfoLog(vertexShader));
        }
    }
}
