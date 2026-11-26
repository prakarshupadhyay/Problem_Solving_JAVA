package com.dualracer.core;

import com.dualracer.utils.Constants;

public class GameConfig {

    public enum Difficulty {
        EASY("Easy", 0.75, 1.2),
        NORMAL("Normal", 1.0, 1.0),
        HARD("Hard", 1.35, 0.8);

        private final String label;
        private final double trafficDensityMultiplier;
        private final double spawnIntervalMultiplier;

        Difficulty(String label, double trafficDensityMultiplier, double spawnIntervalMultiplier) {
            this.label = label;
            this.trafficDensityMultiplier = trafficDensityMultiplier;
            this.spawnIntervalMultiplier = spawnIntervalMultiplier;
        }

        public String getLabel() {
            return label;
        }

        public double getTrafficDensityMultiplier() {
            return trafficDensityMultiplier;
        }

        public double getSpawnIntervalMultiplier() {
            return spawnIntervalMultiplier;
        }
    }

    private double matchDuration = Constants.DEFAULT_MATCH_DURATION;
    private Difficulty difficulty = Difficulty.NORMAL;
    private int soundVolume = 80; // 0 to 100
    private int musicVolume = 70; // 0 to 100
    private boolean soundEnabled = true;
    private boolean musicEnabled = true;
    private boolean fullscreen = false;

    public GameConfig() {}

    public double getMatchDuration() {
        return matchDuration;
    }

    public void setMatchDuration(double matchDuration) {
        this.matchDuration = matchDuration;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(int soundVolume) {
        this.soundVolume = Math.max(0, Math.min(100, soundVolume));
    }

    public int getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(int musicVolume) {
        this.musicVolume = Math.max(0, Math.min(100, musicVolume));
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public void setMusicEnabled(boolean musicEnabled) {
        this.musicEnabled = musicEnabled;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }
}
