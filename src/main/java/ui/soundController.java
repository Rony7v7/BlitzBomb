package ui;

public class soundController {
    private String path;
    private boolean isLoop;

    public soundController(String path, boolean isLoop) {
        this.path = path;
        this.isLoop = isLoop;
    }

    public String getPath() {
        return path;
    }

    public boolean isLoop() {
        return isLoop;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setLoop(boolean loop) {
        isLoop = loop;
    }

    public void play() { // este metodo se encarga de reproducir el sonido

        // try {
        //     AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(path));
        //     Clip clip = AudioSystem.getClip();
        //     clip.open(audioInputStream);
        //     clip.start();
        // } catch (UnsupportedAudioFileException | IOException  | javax.sound.sampled.LineUnavailableException e) {
            
        //     e.printStackTrace();
        // }
            
    }
}
