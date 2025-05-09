public abstract class Player {
    private String name;
    private int wins;
    private int loses;
    private double winRate;
    //X ya da O
    private char symbol;

    public Player(String name, char symbol)
    {
        this.name = name;
        this.symbol = symbol;
    }

    public abstract void makeMove(char[][] board);

    public String getName()
    {
        return this.name;
    }
    public int getWins()
    {
        return this.wins;
    }
    public int getLoses()
    {
        return this.loses;
    }
    public double getWinRate()
    {
        return this.winRate;
    }
    public char getSymbol()
    {
        return this.symbol;
    }

    public void incrementWins()
    {
        ++wins;
    }
    public void incrementLoses()
    {
        ++loses;
    }
    public void calculateWinrate()
    {
        if (wins + loses == 0)
            winRate = 0;
        else
            winRate = (double) (wins * 100) / (wins + loses);
    }
}
