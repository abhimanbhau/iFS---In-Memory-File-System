import io.github.abhimanbhau.constants.Configuration;
import io.github.abhimanbhau.exception.iFSDiskFullException;
import io.github.abhimanbhau.filesystemnative.iFileSystem;
import io.github.abhimanbhau.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Demonstration {
    public static void main(String[] args) throws IOException {
        Configuration config = new Configuration(4, 16, 16, "/Users/akolte/ifs.bin", "abe");
        iFileSystem fileSystem = new iFileSystem(config);

        for (File f : new File("/Users/akolte/ifs_data1").listFiles()) {
            if (f.isDirectory())
                continue;
            System.out.println(f.getPath());
            byte[] data = Files.readAllBytes(Paths.get(f.getPath()));
            try {
                fileSystem.createFile(data, "");
            } catch (iFSDiskFullException e) {
                e.printStackTrace();
            }
        }


//        fileSystem.createFile();
//        fileSystem.createFile();
//
//        fileSystem.deleteFile();
//        fileSystem.deleteFile();
//
//        fileSystem.createDirectory();
//        fileSystem.deleteDirectory();

//        byte[] readFile = fileSystem.readFile();

        fileSystem.finishFileSystem();
        System.out.println("LOL");
        Logger.getInstance().Finish();
    }
}
