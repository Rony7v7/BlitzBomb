package model;

/**
 * Represents a bomb object.
 */
public class Bomb {
    private boolean isDetonated;

    /**
     * Constructs a new Bomb object.
     */
    public Bomb() {

    }

    /**
     * Checks if the bomb is detonated.
     * 
     * @return true if the bomb is detonated, false otherwise.
     */
    public boolean isDetonated() {
        return isDetonated;
    }

    /**
     * Sets the detonation status of the bomb.
     * 
     * @param value the detonation status to set.
     */
    public void setDetonated(boolean value) {
        this.isDetonated = value;
    }
}
