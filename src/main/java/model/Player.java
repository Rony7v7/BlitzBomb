package model;

import javafx.scene.canvas.Canvas;

public class Player {

    private String nickname;
    private int score;
    private Avatar avatar;

    public Player(String nickname, int score, Canvas canvas) {
        this.nickname = nickname;
        this.score = 0;
        this.avatar = new Avatar(0, 0, canvas);
    }

    public void paint() {
        avatar.paint();
    }

}
