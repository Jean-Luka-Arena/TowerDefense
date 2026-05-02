package org.example.model.player;

public class Player {

    private int money;
    private int score;

    public Player() {
        this.money = 100;
        this.score = 0;
    }

    public boolean canAfford(int cost) {
        return money >= cost;
    }

    public void spend(int amount) {
        if (!canAfford(amount)) {
            throw new IllegalStateException("No alcanza con ese dinero");
        }
        money -= amount;
    }

    public void earnMoney(int amount) {
        money += amount;
    }

    public void addScore(int points) {
        score += points;
    }

    public int getMoney() { return money; }
    public int getScore() { return score; }
}
