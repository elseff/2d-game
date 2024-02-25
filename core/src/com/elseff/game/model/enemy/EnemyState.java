package com.elseff.game.model.enemy;

public enum EnemyState {

    IDLE("Idle"),
    SEES_PLAYER("Sees player"),
    LOST_PLAYER("Lost player");

    private final String name;

    EnemyState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
