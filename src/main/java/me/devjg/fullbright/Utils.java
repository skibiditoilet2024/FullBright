package me.devjg.fullbright;

public class Utils {
    public static float lerp(float from, float to, float by) {
        return (float) ((from * (1.0 - by)) + (to * by));
    }

    public static float map(float x, float inputStart, float inputEnd, float outputStart, float outputEnd) {
        return (x - inputStart) / (inputEnd - inputStart) * (outputEnd - outputStart) + outputStart;
    }

    public static float getTransitionSpeed() {
        return (float) 3.0 * FB.transitionSpeed / 500;
    }

    public static boolean inRange(float a, float b, float epsilon) {
        return Math.abs(a - b) < epsilon;
    }
}
