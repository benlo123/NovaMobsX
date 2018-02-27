package com.pikycz.novamobs.components;

/**
 * from PureEntitiesX TODO
 */
/*public class CanPanic {

    private float normalSpeed = (float) 1.0;
    private float panicSpeed = (float) 1.2;

    private int panicCounter;
    private boolean panicEnabled = true;
    private int panicTicks = 100;

    public void setPanicSpeed(float panicSpeed) {
        this.panicSpeed = panicSpeed;
    }

    public float getPanicSpeed() {
        return this.panicSpeed;
    }

    public void setNormalSpeed(float normalSpeed) {
        this.normalSpeed = normalSpeed;
    }

    public float getNormalSpeed() {
        return this.normalSpeed;
    }

    public boolean panicTick(int tickDiff) {
        if (this.isInPanic()) {
            if (this.panicCounter.isTicksExpired(tickDiff)) {
                this.unsetInPanic();
                return false; // not in panic anymore
            }
            return true; // Still panicking
        }
        return false; // Not panicking
    }

    public boolean isInPanic() {
        return this.panicCounter != null;
    }

    public void setInPanic() {
        this.panicCounter = new TickCounter(this.panicTicks); // x ticks in panic

        if (!this instanceof CanBreed && (this instanceof CanBreed && !this.getBreedingComponent().isBaby())) {
            this.speed = this.getPanicSpeed();
        }
        this.moveTime = this.panicTicks; // move for x ticks
    }

    public void unsetInPanic() {
        this.panicCounter = null;

        this.speed = this.getNormalSpeed();
        this.setTarget(null);
    }

    public boolean panicEnabled() {
        return this.panicEnabled;
    }

}*/
