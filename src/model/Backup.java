package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javafx.scene.control.ProgressBar;
import model.exception.BackupException;

public class Backup extends Thread{
    String sourcePath, destinationPath, exclude;
    private static boolean pause = false;
    private static boolean isStop = false;
    private static final Object lock = new Object();
    private static int numFiles = 0;
    private static int i = 0;
    private static ProgressBar progressBar;

    public Backup(String sourcePath, String destinationPath, String exclude, ProgressBar pBar){
       this.sourcePath = sourcePath;
       this.destinationPath = destinationPath;
       this.exclude = exclude;
       progressBar = pBar;
    }

    @Override
    public void run() {
        try {
            isStop = false;
            runBackup();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void pause(){
        synchronized(lock){
            pause = true;
            System.out.println("PAUSED....");
        }
    }
    
    public void unPause(){
        synchronized(lock){
            pause = false;
            lock.notify();
            System.out.println("Resume....");
        }
    }

    public void finish(){
        synchronized(lock){
            System.out.println("Stoping.......");
            pause = false;
            isStop = true;
            lock.notify();
        }
    }

    private void runBackup() throws BackupException {
        sourcePath = sourcePath.trim();
        destinationPath = destinationPath.trim();

        if(sourcePath.length() == 0) throw new BackupException("Error: Source não pode está vazio", null);
        if(destinationPath.length() == 0) throw new BackupException("Error: Destiny não pode está vazio", null);

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
        Files.walk(source, MAX_PRIORITY).forEach((src) -> {
            numFiles++;
        });
        Files.walk(source).forEach((src) -> {
            synchronized(lock){
                while(pause){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                if(isStop) {
                    return;
                }
                i++;
                progressBar.setProgress((float) i/numFiles);

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
            }

        });
    }
}