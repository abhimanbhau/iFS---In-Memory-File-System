import io.github.abhimanbhau.constants.Configuration;
import io.github.abhimanbhau.exception.iFSDirectoryNotEmptyException;
import io.github.abhimanbhau.exception.iFSDiskFullException;
import io.github.abhimanbhau.exception.iFSFileNotFoundException;
import io.github.abhimanbhau.filesystemnative.iFileSystem;
import io.github.abhimanbhau.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.DataFormatException;

public class Demonstration {
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/akolte/ifs.bin");
        file.delete();

        Configuration config = new Configuration(4, 16, 16, "/Users/akolte/ifs.bin", "abe");
        iFileSystem fileSystem = new iFileSystem(config);
        for (File f : new File("/Users/akolte/ifs_data1").listFiles()) {
            if (f.isDirectory())
                continue;
            //System.out.println(f.getPath());
            byte[] data = Files.readAllBytes(Paths.get(f.getPath()));
            try {
                fileSystem.createFile(data, f.getPath().substring(f.getPath().lastIndexOf('/')));
            } catch (iFSDiskFullException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n\nFileSystem after adding files");
        for (String s : fileSystem.listAllFiles()) {
            System.out.println(s);
        }

        fileSystem.deleteFile("/abe.ppts");
        //fileSystem.deleteFile("/text.txt");
        fileSystem.deleteFile("/project.pdf");

        System.out.println("\n\nAfter Deleting");
        for (String s : fileSystem.listAllFiles()) {
            System.out.println(s);
        }

        System.out.println("\n\nDIRECTORIES");
        fileSystem.createDirectory("/ABHIMAN", "/ABHIMAN");
        for(String s : fileSystem.listAllDirectories())
        {
            System.out.println(s);
        }

        System.out.println("ADDING FILES TO ABOVE DIRECTORY");
        for (File f : new File("/Users/akolte/ifs_data1").listFiles()) {
            if (f.isDirectory())
                continue;
            //System.out.println(f.getPath());
            byte[] data = Files.readAllBytes(Paths.get(f.getPath()));
            try {
                fileSystem.createFile(data, "/ABHIMAN" +
                        f.getPath().substring(f.getPath().lastIndexOf('/')));
            } catch (iFSDiskFullException e) {
                System.out.println(e.getMessage());
            }
        }

        for (String s : fileSystem.listAllFiles()) {
            System.out.println(s);
        }

        System.out.println("\n\nDelete Directory");
        try {
            fileSystem.deleteDirectory("/ABHIMAN");
        } catch (iFSDirectoryNotEmptyException e) {
            System.out.println(e.getMessage());
        } catch (iFSFileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        fileSystem.deleteFile("/ABHIMAN/.DS_Store");
        fileSystem.deleteFile("/ABHIMAN/abe.pptx");
        fileSystem.deleteFile("/ABHIMAN/drivers.ppt");
        fileSystem.deleteFile("/ABHIMAN/drivers2.ppt");
        fileSystem.deleteFile("/ABHIMAN/project.pdf");
        fileSystem.deleteFile("/ABHIMAN/text.txt");

        System.out.println("After deleting files individually");



        for (String s : fileSystem.listAllFiles()) {
            System.out.println(s);
        }

        try {
            fileSystem.deleteDirectory("/ABHIMAN");
        } catch (iFSDirectoryNotEmptyException e) {
            System.out.println(e.getMessage());
        } catch (iFSFileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("\nList of directories");
        for(String s : fileSystem.listAllDirectories())
        {
            System.out.println(s);
        }
        try {
            byte[] readFile = fileSystem.readFile("/text.txt");
            System.out.println(new String(readFile));
        } catch (DataFormatException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n\n\nfinish");
        fileSystem.finishFileSystem();
        //System.out.println("fin");
        Logger.getInstance().Finish();
    }
}
