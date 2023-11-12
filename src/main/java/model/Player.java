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

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return the avatar
     */
    public Avatar getAvatar() {
        return avatar;
    }

    /**
     * @param avatar the avatar to set
     */
    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

}
