import io.github.abhimanbhau.constants.Configuration;
import io.github.abhimanbhau.filesystemnative.iFileSystem;
import io.github.abhimanbhau.logging.Logger;

import java.io.IOException;

public class Demonstration {
    public static void main(String[] args) throws IOException {
        Configuration config = new Configuration(4, 16, 16, "/Users/akolte/ifs.bin", "abe");
        iFileSystem fileSystem = new iFileSystem(config);
        fileSystem.finishFileSystem();
        System.out.println("LOL");
        Logger.getInstance().Finish();
    }
}
