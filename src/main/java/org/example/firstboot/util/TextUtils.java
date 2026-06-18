
package org.example.firstboot.util;

public class TextUtils {

    public static String normalize(
            String text) {

        if (text == null) {
            return "";
        }

        return text
                .toLowerCase()
                .trim();
    }
}

