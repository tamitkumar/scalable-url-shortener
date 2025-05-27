package com.techbrain.tiny.url.utils;

public class Base62Encoder {
    static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String encode(long value) {
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(BASE62.charAt((int) (value % 62)));
            value /= 62;
        }
        return sb.reverse().toString();
    }

    public static long decode(String shortUrl) {
        long num = 0;
        for (char c : shortUrl.toCharArray()) {
            num = num * 62 + BASE62.indexOf(c);
        }
        return num;
    }
}
