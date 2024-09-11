package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class BufferWriter extends Thread {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private TextArea textArea;
    private boolean running = true;

    public BufferWriter (TextArea textArea){
        this.textArea = textArea;
    }

    public void addToBufffer(String msg){
        try {
            queue.add(msg);   
        } catch (Exception e) {
            //inves de encher o log por causa de alguma exception, só interrompe a Thread, assim evita de encher nosso console
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        while (running || !queue.isEmpty()) {
            try {
                String msg = queue.take();
                Platform.runLater(() -> textArea.appendText(msg));
            } catch (Exception e) {
                //isso aqui é importante pra não dar problema pra JVM interrepomper a força bruta.
                //e também se não estiver nada no queue, só para o loop e quando chamarem novamente ele entra no loop.
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    public void stopWrite(){
        running = false;
        interrupt();
    }
}
