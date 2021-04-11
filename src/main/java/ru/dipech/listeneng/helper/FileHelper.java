package ru.dipech.listeneng.helper;

import java.io.InputStream;
import java.nio.file.Path;

public interface FileHelper {

    Path create(InputStream stream);

}
