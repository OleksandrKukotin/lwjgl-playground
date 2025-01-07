package com.github.oleksandrkukotin.audiotripmaker;

import javax.sound.sampled.*;

public class SystemMixerCapture {

    public static void main(String[] args) {
        try {
            // List all available mixers
            Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
            for (int i = 0; i < mixerInfos.length; i++) {
                String output = String.format("%d. %s%n%s%n", i, mixerInfos[i].getName(), mixerInfos[i].getDescription());
                System.out.println(output);
            }

            // Select a specific mixer (change the index to your desired mixer)
            Mixer mixer = AudioSystem.getMixer(mixerInfos[4]); // Replace 0 with the appropriate index
            mixer.open();

            // Specify audio format
            AudioFormat format = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    44100.0f, // Sampling rate
                    16,       // Bits per sample
                    2,        // Channels (1 for mono, 2 for stereo)
                    4,        // Frame size (2 bytes per sample x 2 channels)
                    44100.0f, // Frame rate
                    true     // Little-endian (use `true` for big-endian)
            );

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // Need to investigate if I can capture audio from the Playback devices
            TargetDataLine line = (TargetDataLine) mixer.getLine(info);
            line.open(format);
            line.start();

            byte[] buffer = new byte[4096];
            while (true) {
                int bytesRead = line.read(buffer, 0, buffer.length);
                if (isSilent(buffer)) {
                    System.out.println("Captured silence");
                } else {
                    System.out.println("Captured " + bytesRead + " bytes of audio");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isSilent(byte[] buffer) {
        for (byte b : buffer) {
            if (b != 0 && b != -1 && b != 1) {
                return false; // Non-silent data found
            }
        }
        return true; // Buffer contains only silence
    }
}
