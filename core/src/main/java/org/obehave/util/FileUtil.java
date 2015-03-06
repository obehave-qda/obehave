package org.obehave.util;

import java.io.File;

/**
 * @author Markus Möslinger
 */
public class FileUtil {
    private FileUtil() {
        throw new AssertionError(I18n.get("exception.constructor.utility"));
    }

    public static File removeSuffixIfThere(File file, String suffix) {
        final String absolutePath = file.getAbsolutePath();
        if (!absolutePath.endsWith(suffix)) {
            return file;
        } else {
            return new File(absolutePath.substring(0, absolutePath.lastIndexOf(suffix)));
        }
    }
}
