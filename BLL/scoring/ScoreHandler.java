package BLL.scoring;

import java.util.concurrent.TimeUnit;

/**
 * ScoreHandler controls the point system inside the game.
 * It calculates the total amount of points.
 * An optional option is to convert points to stars by using the static method.
 */
public class ScoreHandler implements ScoringConstants {
    private long startTime;
    private long endTime;

    public ScoreHandler() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Gets when the game starts or was loaded.
     * @return unix time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time by doing an offset of the value given.
     * @param offset any offset in milliseconds
     */
    public void setStartTimeOffset(long offset) {
        this.startTime = startTime - offset;
    }

    /**
     * Gets the end time.
     * @return endtime in milliseconds
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time when winning a game, so the points do not keep decreasing.
     * @param endTime new end time
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * Calculates the total amount of points the player received throughout the game.
     * The formula is:
     * fuelPoints = fuelConsumption * pointDecreaseFuelConsumption
     * timePoints = timeElapsed in minutes * pointDecreasePerMinute
     * points = startScore - fuelPoints - timePoints
     * @param time points at given time
     * @param totalFuelConsumption the amount of fuel the player has used
     * @return score the player obtained
     */
    public int calculatePoints(long time, int totalFuelConsumption) {
        int fuelPoints = totalFuelConsumption * pointDecreaseFuelConsumption;
        int timePoints = (int)TimeUnit.MILLISECONDS.toMinutes(time) * pointDecreasePerMinute;

        return startScore - fuelPoints - timePoints;
    }

    /**
     * By giving the amount of point, it will return a number of stars.
     * @param points total points of the player
     * @return how many stars based on points
     */
    public static int getStars(int points) {
        int stars;

        if(points >= 10000){
            stars = 5;
        } else if (points >= 8000){
            stars = 4;
        } else if (points >= 6000){
            stars = 3;
        } else if (points >= 4000){
            stars = 2;
        } else if (points >= 2000){
            stars = 1;
        } else{
            stars = 0;
        }

        return stars;
    }

    /**
     * Calculates how much time has elapsed since the beginning of the game.
     * The time is in milliseconds.
     * @return a difference between start and real-time.
     */
    public long calculateTimeElapsed() {
        return System.currentTimeMillis() - startTime;
    }
}
