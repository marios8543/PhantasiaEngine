package com.tza.phantasia.Utilities;

import com.tza.phantasia.Phantasia;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ResourceUtils {

    public static URL getResource(String resourceName){
        URL url = Phantasia.class.getClassLoader().getResource(resourceName);
        if (Objects.isNull(url))
            Logger.getLogger("RESOURCE").log(Level.WARNING, String.format("Resource %s is null", resourceName));
        return url;
    }

    public static InputStreamReader fileReader(String resourceName) throws IOException {
        return new InputStreamReader(getResource(resourceName).openStream());
    }
}
