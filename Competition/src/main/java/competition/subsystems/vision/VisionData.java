package competition.subsystems.vision;

public class VisionData {
    protected Double yaw;
    protected boolean hasTarget;
    protected Double range;
    protected Double rotation;
    protected Integer mode;

    /**
     * @return the yaw
     */
    public Double getYaw() {
        return yaw;
    }

    /**
     * @param yaw the yaw to set
     */
    public void setYaw(Double yaw) {
        this.yaw = yaw;
    }

    /**
     * @return the hasTarget
     */
    public boolean isHasTarget() {
        return hasTarget;
    }

    /**
     * @param hasTarget the hasTarget to set
     */
    public void setHasTarget(boolean hasTarget) {
        this.hasTarget = hasTarget;
    }

    /**
     * @return the range
     */
    public Double getRange() {
        return range;
    }

    /**
     * @param range the range to set
     */
    public void setRange(Double range) {
        this.range = range;
    }

    /**
     * @return the rotation
     */
    public Double getRotation() {
        return rotation;
    }

    /**
     * @param rotation the rotation to set
     */
    public void setRotation(Double rotation) {
        this.rotation = rotation;
    }

    /**
     * @return the mode
     */
    public Integer getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(Integer mode) {
        this.mode = mode;
    }
    
    
}