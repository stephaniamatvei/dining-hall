package md.utm.dininghall.service.utils;

public class NumberUtils {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
