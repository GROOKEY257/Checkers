public class pieces {
    private int xCor;
    private int yCor;
    private boolean isKing;
    private int player; // 0 for Player 1, 1 for Player 2

    public pieces() {
        xCor = 0;
        yCor = 0;
        isKing = false;
        player = 0;
    }

    public pieces(int x, int y, int player) {
        xCor = x;
        yCor = y;
        isKing = false;
        this.player = player;
    }

    // Getters and setters for coordinates
    public int getX() {
        return xCor;
    }

    public int getY() {
        return yCor;
    }

    public void setX(int x) {
        xCor = x;
    }

    public void setY(int y) {
        yCor = y;
    }

    // King status (for promotion)
    public boolean isKing() {
        return isKing;
    }

    public void setKing(boolean king) {
        isKing = king;
    }

    // Get the player associated with the piece (0 = Player 1, 1 = Player 2)
    public int getPlayer() {
        return player;
    }
}
