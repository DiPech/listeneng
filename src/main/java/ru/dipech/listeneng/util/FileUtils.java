package ru.dipech.listeneng.util;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;

public class FileUtils {

    public static String getRelativePath(Path dir, Path path) {
        return StringUtils.removeStart(path.toAbsolutePath().toString(), dir.toAbsolutePath().toString());
    }

}
