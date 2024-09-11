package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import model.exception.BackupException;

public class Backup {
    public static void run(String sourcePath, String destinationPath, String exclude ) throws BackupException {

        if(sourcePath == "") throw new BackupException("Error: Source not be empty", null);
        if(sourcePath == "") throw new BackupException("Error: Destiny not be empty", null);

        if(exclude == "") exclude = "null";

        String[] excludes = exclude.split(",");

        //Creating Path with our params
        Path srcDir;
        Path destDir;
        try {
            srcDir = Paths.get(sourcePath);
            destDir = Paths.get(destinationPath);
        } catch (Exception e) {
            throw new BackupException("Error: " + e.getMessage() + " \n CAUSE: ", e.getCause());
        }
        System.out.println("Inicializado...");
        System.out.println();

        //Initialization Backup
        try{
            copyFiles(srcDir, destDir, excludes);
        }catch(IOException e){
            throw new BackupException(e.getMessage(), e.getCause());
        }
        System.out.println("Backup Concluido com sucesso");
    }
    

    private static void copyFiles(Path source, Path destination, String[] excludeDirs) throws IOException{
        Files.walk(source).forEach(src -> {
            Path target = destination.resolve(source.relativize(src));
            for (String exclude : excludeDirs) {
                System.out.println(src.toString() + " ------- " + exclude);
                if (src.toString().equals(exclude)) {
                    return; // Skip this file as it matches the exclusion criteria
                }
            }
            if (Files.isDirectory(src)) {
                try {
                    Files.createDirectories(target);
                } catch (IOException e) {
                    System.err.println("Failed to create directory: " + e.getMessage());
                }
            } else {
                try {
                    Files.copy(src, target, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Arquivo " + src + " copiada para " + target);
                } catch (IOException e) {
                    System.err.println("Erro ao copiar o arquivo: " + e.getMessage());
                }
            }
        });
    }
}
