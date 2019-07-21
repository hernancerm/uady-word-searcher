package hercerm.iindices.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * Provides only the static method {@link #download(String, String)} for downloading
 * resources given a valid URL. Java NIO is used to avoid buffering.
 *
 * @version 24.05.19
 * @author Hernán J. Cervera Manzanilla
 */
public class FileDownloader {

    /**
     * Downloads a resource from the internet.
     *
     * @param resourceURL URL of the resource.
     * @param outPath path to write the downloaded resource into.
     */
    public static void download(final String resourceURL, final String outPath) {
        try {
            final URL URL = new URL(resourceURL);

            ReadableByteChannel readableByteChannel = Channels.newChannel(URL.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(outPath);
            FileChannel fileChannel = fileOutputStream.getChannel();

            fileOutputStream.getChannel()
                    .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch(IOException e) {
            System.err.println("Unable to download file from " + resourceURL);
            System.exit(1);
        }
    }
}
