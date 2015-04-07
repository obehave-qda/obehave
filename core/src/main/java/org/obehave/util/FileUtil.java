package org.obehave.util;

import java.io.File;

/**
 * Util class for file handling. Candidate for future refactoring (moving the methods to the class where they are used)
 */
public class FileUtil {
    private FileUtil() {
        throw new AssertionError(I18n.get("exception.constructor.utility"));
    }

    /**
     * Returns the absolute path of {@code file}, excluding a given {@code suffix}.
     * <p/>
     * Examples for suffix {@code .txt}:<br/>
     * <ul>
     *     <li>{@code new File("/hello.txt")} will be returned as {@code "/hello"}</li>
     *     <li>{@code new File("/hello")} will also be returned as {@code "/hello"}, since the given suffix wasn't there to remove</li>
     * </ul>
     * @param file the file to get the absolute path from
     * @param suffix a suffix to remove, if there
     * @return the absolute path of a file, represanted as a {@link String}
     */
    public static String removeSuffixIfThere(File file, String suffix) {
        final String absolutePath = file.getAbsolutePath();
        if (!absolutePath.endsWith(suffix)) {
            return absolutePath;
        } else {
            return absolutePath.substring(0, absolutePath.lastIndexOf(suffix));
        }
    }

    public static String removeSuffixFromFileNameIfThere(File file, String suffix) {
        final String fileName = file.getName();
        if (!fileName.endsWith(suffix)) {
            return fileName;
        } else {
            return fileName.substring(0, fileName.lastIndexOf(suffix));
        }
    }

    public static boolean isDatabaseFileLocked(File file) {
        final File lockFile = new File(file.getAbsolutePath().replaceAll("\\.h2\\.db$", ".lock.db"));

        return lockFile.exists();
    }
}
