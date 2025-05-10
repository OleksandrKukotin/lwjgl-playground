package com.github.oleksandrkukotin.lwjgl.core;

import glm_.vec3.Vec3;

public class Camera {

    public static final float CAMERA_SPEED = 0.7f;

    private Vec3 position = new Vec3(0.0f, 0.0f, 0.0f);
    private final Vec3 up = new Vec3(0.0f, 1.0f, 0.0f);
    private Vec3 front = new Vec3(0.0f, 0.0f, 1.0f);

    public void updateFront(float pitch, float yaw) {
        this.getFront().x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        this.getFront().y = (float) Math.sin(Math.toRadians(pitch));
        this.getFront().z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        this.setFront(this.getFront().normalize());
    }
    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public Vec3 getUp() {
        return up;
    }

    public Vec3 getFront() {
        return front;
    }

    public void setFront(Vec3 front) {
        this.front = front;
    }
}
