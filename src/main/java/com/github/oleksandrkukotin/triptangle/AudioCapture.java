package com.github.oleksandrkukotin.triptangle;

import javax.sound.sampled.TargetDataLine;

public class AudioCapture {

    private final TargetDataLine line;

    public AudioCapture(TargetDataLine line) {
        this.line = line;
    }
}
