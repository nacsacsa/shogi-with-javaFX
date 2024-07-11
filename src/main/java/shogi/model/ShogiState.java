package shogi.model;

import shogi.model.utils.TwoPhaseMoveState;

public class ShogiState implements TwoPhaseMoveState<Posititon> {

    /**
     * Applies the move provided to the state. This method should be called if
     * and only if {@link #isLegalMove(Posititon, Posititon)} returns {@code true}.
     *
     * @param from represents where to move from
     * @param to   represents where to move to
     */
    @Override
    public void makeMove(Posititon from, Posititon to) {

    }

    /**
     * {@return whether it is possible to make a move from the argument
     * specified}
     *
     * @param from represents where to move from
     */
    @Override
    public boolean isLegalToMoveFrom(Posititon from) {
        return false;
    }

    /**
     * {@return whether the move provided can be applied to the state}
     *
     * @param from represents where to move from
     * @param to   represents where to move to
     */
    @Override
    public boolean isLegalMove(Posititon from, Posititon to) {
        return false;
    }


    /**
     * {@return the status of the game}
     */
    @Override
    public Status getStatus() {
        return null;
    }

    /**
     * {@return the player who moves next}
     */
    @Override
    public Player getNextPlayer() {
        return null;
    }

    /**
     * {@return whether the game is over}
     */
    @Override
    public boolean isGameOver() {
        return false;
    }
}
