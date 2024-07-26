package shogi.model.utils;

/**
 * Represents the states of a Chess like game such as: chess (obviously) or shogi.
 * This interface introduces the checkmate check, which helps with determination
 * where a {@code piece} can move.
 *
 * @param <T> represents the moves that can be applied to the states
 */
public interface ChessTypeGameState<T> extends TwoPhaseMoveState<T>{

    /**
     * {@return if the {@code player} king is in checkmate or not}
     *
     * @param checkMateTable the table which contains the enemy player's pieces hit radius
     */
    boolean isCheckMate(boolean[][] checkMateTable);
}
