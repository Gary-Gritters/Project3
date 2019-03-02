package p3;

public enum Player {
    BLACK, WHITE;

    /**
     * Return the {@code p3.Player} whose turn is next.
     *
     * @return the {@code p3.Player} whose turn is next
     */
    public Player next() {
        if (this == BLACK)
            return WHITE;
        else
            return BLACK;

        //	return this == BLACK ? WHITE : BLACK;
    }
}