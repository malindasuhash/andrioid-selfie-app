package app.com.example.malindasuhash.dailyselfie;

import java.io.Serializable;

/**
 * Simple data transfer object to store the full path and the name of the file.
 */
public class Selfie implements Serializable {
    public String FileFullPath;
    public String FriendlyName;
}
