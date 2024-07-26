package shogi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shogi.model.utils.State;

import static org.junit.jupiter.api.Assertions.*;

public class ShogiStateTest {
    ShogiState initial;
    ShogiState inCheckMateState;
    ShogiState inGameOverState;
    ShogiState inGameState;

    @BeforeEach
    public void StartUp() {
        initial = new ShogiState();

        inCheckMateState = new ShogiState();
        inCheckMateState.makeMove(new Position(6, 2), new Position(5, 2));
        inCheckMateState.makeMove(new Position(2, 7), new Position(3, 7));
        inCheckMateState.makeMove(new Position(7, 1), new Position(2, 6));

        inGameOverState = new ShogiState();
        inGameOverState.makeMove(new Position(6, 4), new Position(5, 4));
        inGameOverState.makeMove(new Position(2, 4), new Position(3, 4));
        inGameOverState.makeMove(new Position(7, 7), new Position(7, 4));
        inGameOverState.makeMove(new Position(0, 6), new Position(1, 5));
        inGameOverState.makeMove(new Position(5, 4), new Position(4, 4));
        inGameOverState.makeMove(new Position(3, 4), new Position(4, 4));
        inGameOverState.makeMove(new Position(6, 2), new Position(5, 2));
        inGameOverState.makeMove(new Position(2, 8), new Position(3, 8));
        inGameOverState.makeMove(new Position(7, 1), new Position(4, 4));
        inGameOverState.makeMove(new Position(2, 2), new Position(3, 2));
        inGameOverState.makeMove(new Position(4, 4), new Position(2, 2));

        inGameState = new ShogiState();
        inGameState.makeMove(new Position(6, 7), new Position(5, 7));
        inGameState.makeMove(new Position(2, 7), new Position(3, 7));
        inGameState.makeMove(new Position(5, 7), new Position(4, 7));
        inGameState.makeMove(new Position(3, 7), new Position(4, 7));
        inGameState.makeMove(new Position(7, 7), new Position(4, 7));
        inGameState.makeMove(new Position(0, 5), new Position(1, 6));
        inGameState.makeMove(new Position(4, 7), new Position(1, 7));
        inGameState.makeMove(new Position(0, 6), new Position(1, 7));
        inGameState.makeMove(new Position(6, 2), new Position(5, 2));
        inGameState.makeMove(new Position(2, 1), new Position(3, 1));
    }

    @Test
    public void shogiStateConstructorTest() {
        assertEquals(Piece.DARK_LANCE, initial.getPiece(0, 0));
        assertEquals(Piece.DARK_KNIGHT, initial.getPiece(0, 1));
        assertEquals(Piece.DARK_SILVER, initial.getPiece(0, 2));
        assertEquals(Piece.DARK_GOLD, initial.getPiece(0, 3));
        assertEquals(Piece.DARK_KING, initial.getPiece(0, 4));
        assertEquals(Piece.DARK_GOLD, initial.getPiece(0, 5));
        assertEquals(Piece.DARK_SILVER, initial.getPiece(0, 6));
        assertEquals(Piece.DARK_KNIGHT, initial.getPiece(0, 7));
        assertEquals(Piece.DARK_LANCE, initial.getPiece(0, 8));

        assertEquals(Piece.LIGHT_LANCE, initial.getPiece(8, 0));
        assertEquals(Piece.LIGHT_KNIGHT, initial.getPiece(8, 1));
        assertEquals(Piece.LIGHT_SILVER, initial.getPiece(8, 2));
        assertEquals(Piece.LIGHT_GOLD, initial.getPiece(8, 3));
        assertEquals(Piece.LIGHT_KING, initial.getPiece(8, 4));
        assertEquals(Piece.LIGHT_GOLD, initial.getPiece(8, 5));
        assertEquals(Piece.LIGHT_SILVER, initial.getPiece(8, 6));
        assertEquals(Piece.LIGHT_KNIGHT, initial.getPiece(8, 7));
        assertEquals(Piece.LIGHT_LANCE, initial.getPiece(8, 8));

        for (var col = 0; col < 9; col++) {
            assertEquals(Piece.DARK_PAWN, initial.getPiece(2, col));
        }

        for (var col = 0; col < 9; col++) {
            assertEquals(Piece.LIGHT_PAWN, initial.getPiece(6, col));
        }

        for (var col = 0; col < 9; col++) {
            if (col == 1) {
                assertEquals(Piece.DARK_ROOK, initial.getPiece(1, col));
            }
            else if (col == 7) {
                assertEquals(Piece.DARK_BISHOP, initial.getPiece(1, col));
            }
            else {
                assertEquals(Piece.EMPTY, initial.getPiece(1, col));
            }
        }

        for (var col = 0; col < 9; col++) {
            if (col == 1) {
                assertEquals(Piece.LIGHT_BISHOP, initial.getPiece(7, col));
            }
            else if (col == 7) {
                assertEquals(Piece.LIGHT_ROOK, initial.getPiece(7, col));
            }
            else {
                assertEquals(Piece.EMPTY, initial.getPiece(7, col));
            }
        }

    }

    @Test
    public void makeMoveTest1() {
        initial.makeMove(new Position(6, 1), new Position(5, 1));
        assertEquals(Piece.LIGHT_PAWN, initial.getPiece(5, 1));
        assertEquals(Piece.EMPTY, initial.getPiece(6, 1));
    }
    @Test
    public void makeMoveTest2() {
        initial.makeMove(new Position(6, 1), new Position(5, 1));
        initial.makeMove(new Position(2, 1), new Position(3, 1));
        assertEquals(Piece.DARK_PAWN, initial.getPiece(3, 1));
        assertEquals(Piece.EMPTY, initial.getPiece(2, 1));
    }
    @Test
    public void makeMoveTest3() {
        initial.makeMove(new Position(6, 2), new Position(5, 2));
        initial.makeMove(new Position(2, 1), new Position(3, 1));
        initial.makeMove(new Position(7, 1), new Position(2, 6));
        assertEquals(Piece.LIGHT_PROMOTED_BISHOP, initial.getPiece(2, 6));
        assertEquals(Piece.EMPTY, initial.getPiece(7, 1));
    }

    @Test
    public void getNextPlayerTest1() {
        assertEquals(State.Player.PLAYER_1, initial.getNextPlayer());
        assertEquals(State.Player.PLAYER_1, inGameState.getNextPlayer());
    }
    @Test
    public void getNextPlayerTest2() {
        assertEquals(State.Player.PLAYER_2, inGameOverState.getNextPlayer());
        assertEquals(State.Player.PLAYER_2, inCheckMateState.getNextPlayer());
    }
    @Test
    public void getNextPlayerTest3() {
        initial.makeMove(new Position(6, 1), new Position(5, 1));
        assertEquals(State.Player.PLAYER_2, initial.getNextPlayer());
    }
    @Test
    public void getNextPlayerTest4() {
        initial.makeMove(new Position(6, 0), new Position(2, 0));
        initial.makeMove(new Position(0, 0), new Position(2, 0));
        initial.putPieceToBoard(Piece.LIGHT_PAWN, new Position(3, 0));
        assertEquals(State.Player.PLAYER_2, initial.getNextPlayer());
    }

    @Test
    public void getStatusTest1() {
        assertEquals(State.Status.IN_PROGRESS, initial.getStatus());
        assertEquals(State.Status.IN_PROGRESS, inGameState.getStatus());
        assertEquals(State.Status.IN_PROGRESS, inCheckMateState.getStatus());
    }
    @Test
    public void getStatusTest2() {
        assertEquals(State.Status.PLAYER_1_WINS, inGameOverState.getStatus());
    }

    @Test
    public void isGameOverTest1() {
        assertFalse(initial.isGameOver());
        assertFalse(inGameState.isGameOver());
        assertFalse(inCheckMateState.isGameOver());
    }
    @Test
    public void isGameOverTest2() {
        assertTrue(inGameOverState.isGameOver());
    }

    @Test
    public void isCheckMateTest1() {
        assertFalse(initial.isCheckMate(initial.getCheckMateBoard()));
        assertFalse(inGameState.isCheckMate(inGameState.getCheckMateBoard()));
    }
    @Test
    public void isCheckMateTest2() {
        assertTrue(inCheckMateState.isCheckMate(inCheckMateState.getCheckMateBoard()));
        assertTrue(inGameOverState.isCheckMate(inGameOverState.getCheckMateBoard()));
    }

    @Test
    public void isLegalToMoveFromTest1() {
        for (var row = 0; row < 6; row++) {
            for (var col = 0; col < 9; col++) {
                assertFalse(initial.isLegalToMoveFrom(new Position(row, col)));
            }
        }

        for (var col = 0; col < 9; col++) {
            assertTrue(initial.isLegalToMoveFrom(new Position(6, col)));
        }

        assertFalse(initial.isLegalToMoveFrom(new Position(7, 0)));
        assertFalse(initial.isLegalToMoveFrom(new Position(7, 1)));
        assertFalse(initial.isLegalToMoveFrom(new Position(7, 2)));
        assertFalse(initial.isLegalToMoveFrom(new Position(7, 3)));
        assertFalse(initial.isLegalToMoveFrom(new Position(7, 4)));
        assertFalse(initial.isLegalToMoveFrom(new Position(7, 5)));
        assertFalse(initial.isLegalToMoveFrom(new Position(7, 6)));
        assertTrue(initial.isLegalToMoveFrom(new Position(7, 7)));
        assertFalse(initial.isLegalToMoveFrom(new Position(7, 8)));

        assertTrue(initial.isLegalToMoveFrom(new Position(8, 0)));
        assertFalse(initial.isLegalToMoveFrom(new Position(8, 1)));
        assertTrue(initial.isLegalToMoveFrom(new Position(8, 2)));
        assertTrue(initial.isLegalToMoveFrom(new Position(8, 3)));
        assertTrue(initial.isLegalToMoveFrom(new Position(8, 4)));
        assertTrue(initial.isLegalToMoveFrom(new Position(8, 5)));
        assertTrue(initial.isLegalToMoveFrom(new Position(8, 6)));
        assertFalse(initial.isLegalToMoveFrom(new Position(8, 7)));
        assertTrue(initial.isLegalToMoveFrom(new Position(8, 8)));
    }
    @Test
    public void isLegalToMoveFromTest2() {
        initial.makeMove(new Position(8, 4), new Position(7, 4));
        for (var row = 3; row < 9; row++) {
            for (var col = 0; col < 9; col++) {
                assertFalse(initial.isLegalToMoveFrom(new Position(row, col)));
            }
        }

        for (var col = 0; col < 9; col++) {
            assertTrue(initial.isLegalToMoveFrom(new Position(2, col)));
        }

        assertFalse(initial.isLegalToMoveFrom(new Position(1, 0)));
        assertTrue(initial.isLegalToMoveFrom(new Position(1, 1)));
        assertFalse(initial.isLegalToMoveFrom(new Position(1, 2)));
        assertFalse(initial.isLegalToMoveFrom(new Position(1, 3)));
        assertFalse(initial.isLegalToMoveFrom(new Position(1, 4)));
        assertFalse(initial.isLegalToMoveFrom(new Position(1, 5)));
        assertFalse(initial.isLegalToMoveFrom(new Position(1, 6)));
        assertFalse(initial.isLegalToMoveFrom(new Position(1, 7)));
        assertFalse(initial.isLegalToMoveFrom(new Position(1, 8)));

        assertTrue(initial.isLegalToMoveFrom(new Position(0, 0)));
        assertFalse(initial.isLegalToMoveFrom(new Position(0, 1)));
        assertTrue(initial.isLegalToMoveFrom(new Position(0, 2)));
        assertTrue(initial.isLegalToMoveFrom(new Position(0, 3)));
        assertTrue(initial.isLegalToMoveFrom(new Position(0, 4)));
        assertTrue(initial.isLegalToMoveFrom(new Position(0, 5)));
        assertTrue(initial.isLegalToMoveFrom(new Position(0, 6)));
        assertFalse(initial.isLegalToMoveFrom(new Position(0, 7)));
        assertTrue(initial.isLegalToMoveFrom(new Position(0, 8)));
    }
    @Test
    public void isLegalToMoveFromTest3() {
        for (var row = 0; row < 5; row++) {
            for (var col = 0; col < 9; col++) {
                assertFalse(inGameState.isLegalToMoveFrom(new Position(row, col)));
            }
        }
        assertFalse(inGameState.isLegalToMoveFrom(new Position(5, 0)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(5, 1)));
        assertTrue(inGameState.isLegalToMoveFrom(new Position(5, 2)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(5, 3)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(5, 4)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(5, 5)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(5, 6)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(5, 7)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(5, 8)));

        assertTrue(inGameState.isLegalToMoveFrom(new Position(6, 0)));
        assertTrue(inGameState.isLegalToMoveFrom(new Position(6, 1)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(6, 2)));
        assertTrue(inGameState.isLegalToMoveFrom(new Position(6, 3)));
        assertTrue(inGameState.isLegalToMoveFrom(new Position(6, 4)));
        assertTrue(inGameState.isLegalToMoveFrom(new Position(6, 5)));
        assertTrue(inGameState.isLegalToMoveFrom(new Position(6, 6)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(6, 7)));
        assertTrue(inGameState.isLegalToMoveFrom(new Position(6, 8)));

        assertFalse(inGameState.isLegalToMoveFrom(new Position(7, 0)));
        assertTrue(inGameState.isLegalToMoveFrom(new Position(7, 1)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(7, 2)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(7, 3)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(7, 4)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(7, 5)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(7, 6)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(7, 7)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(7, 8)));

        assertTrue(inGameState.isLegalToMoveFrom(new Position(8, 0)));
        assertTrue(inGameState.isLegalToMoveFrom(new Position(8, 1)));
        assertTrue(inGameState.isLegalToMoveFrom(new Position(8, 2)));
        assertTrue(inGameState.isLegalToMoveFrom(new Position(8, 3)));
        assertTrue(inGameState.isLegalToMoveFrom(new Position(8, 4)));
        assertTrue(inGameState.isLegalToMoveFrom(new Position(8, 5)));
        assertTrue(inGameState.isLegalToMoveFrom(new Position(8, 6)));
        assertFalse(inGameState.isLegalToMoveFrom(new Position(8, 7)));
        assertTrue(inGameState.isLegalToMoveFrom(new Position(8, 8)));
    }
    @Test
    public void isLegalToMoveFromTest4() {
        assertFalse(inCheckMateState.isLegalToMoveFrom(new Position(0, 0)));
        assertFalse(inCheckMateState.isLegalToMoveFrom(new Position(0, 1)));
        assertFalse(inCheckMateState.isLegalToMoveFrom(new Position(0, 2)));
        assertFalse(inCheckMateState.isLegalToMoveFrom(new Position(0, 3)));
        assertTrue(inCheckMateState.isLegalToMoveFrom(new Position(0, 4)));
        assertTrue(inCheckMateState.isLegalToMoveFrom(new Position(0, 5)));
        assertTrue(inCheckMateState.isLegalToMoveFrom(new Position(0, 6)));
        assertTrue(inCheckMateState.isLegalToMoveFrom(new Position(0, 7)));
        assertFalse(inCheckMateState.isLegalToMoveFrom(new Position(0, 8)));

        assertFalse(inCheckMateState.isLegalToMoveFrom(new Position(1, 0)));
        assertTrue(inCheckMateState.isLegalToMoveFrom(new Position(1, 1)));
        assertFalse(inCheckMateState.isLegalToMoveFrom(new Position(1, 2)));
        assertFalse(inCheckMateState.isLegalToMoveFrom(new Position(1, 3)));
        assertFalse(inCheckMateState.isLegalToMoveFrom(new Position(1, 4)));
        assertFalse(inCheckMateState.isLegalToMoveFrom(new Position(1, 5)));
        assertFalse(inCheckMateState.isLegalToMoveFrom(new Position(1, 6)));
        assertTrue(inCheckMateState.isLegalToMoveFrom(new Position(1, 7)));
        assertFalse(inCheckMateState.isLegalToMoveFrom(new Position(1, 8)));

        for (var row = 2; row < 9; row++) {
            for (var col = 0; col < 9; col++) {
                assertFalse(inCheckMateState.isLegalToMoveFrom(new Position(row, col)));
            }
        }
    }
    @Test
    public void isLegalToMoveFromTest5() {
        for (var row = 0; row < 9; row++) {
            for (var col = 0; col < 9; col++) {
                assertFalse(inGameOverState.isLegalToMoveFrom(new Position(row, col)));
            }
        }
    }
    @Test
    public void isLegalToMoveFromTest6() {
        assertFalse(initial.isLegalToMoveFrom(new Position(-1, -1)));
        assertFalse(initial.isLegalToMoveFrom(new Position(-1, 0)));
        assertFalse(initial.isLegalToMoveFrom(new Position(-1, 10)));
        assertFalse(initial.isLegalToMoveFrom(new Position(10, 10)));
        assertFalse(initial.isLegalToMoveFrom(new Position(6, -1)));
        assertFalse(initial.isLegalToMoveFrom(new Position(9, 4)));
    }

    @Test
    public void isLegalMoveTest1() {
        assertTrue(initial.isLegalMove(new Position(6, 0), new Position(5, 0)));
        assertFalse(initial.isLegalMove(new Position(6, 0), new Position(4, 0)));
        assertFalse(initial.isLegalMove(new Position(6, 0), new Position(-1, 0)));
        assertFalse(initial.isLegalMove(new Position(6, 0), new Position(-1, -5)));
        assertFalse(initial.isLegalMove(new Position(6, 0), new Position(9, 0)));
        assertFalse(initial.isLegalMove(new Position(6, 0), new Position(10, 10)));
        assertFalse(initial.isLegalMove(new Position(7, 1), new Position(6, 2)));
        assertFalse(initial.isLegalMove(new Position(7, 1), new Position(5, 3)));
        assertTrue(initial.isLegalMove(new Position(7, 7), new Position(7, 8)));
        assertTrue(initial.isLegalMove(new Position(7, 7), new Position(7, 5)));
        assertFalse(initial.isLegalMove(new Position(7, 7), new Position(7, 1)));
        assertFalse(initial.isLegalMove(new Position(7, 7), new Position(7, 0)));
        assertTrue(initial.isLegalMove(new Position(8, 0), new Position(7, 0)));
        assertFalse(initial.isLegalMove(new Position(8, 0), new Position(6, 0)));
        assertFalse(initial.isLegalMove(new Position(8, 0), new Position(5, 0)));
        assertFalse(initial.isLegalMove(new Position(8, 2), new Position(7, 1)));
        assertTrue(initial.isLegalMove(new Position(8, 2), new Position(7, 2)));
        assertTrue(initial.isLegalMove(new Position(8, 2), new Position(7, 3)));
        assertTrue(initial.isLegalMove(new Position(8, 3), new Position(7, 2)));
        assertTrue(initial.isLegalMove(new Position(8, 3), new Position(7, 3)));
        assertTrue(initial.isLegalMove(new Position(8, 3), new Position(7, 4)));
        assertFalse(initial.isLegalMove(new Position(8, 3), new Position(8, 2)));
        assertFalse(initial.isLegalMove(new Position(8, 3), new Position(8, 4)));
        assertTrue(initial.isLegalMove(new Position(8, 4), new Position(7, 3)));
        assertTrue(initial.isLegalMove(new Position(8, 4), new Position(7, 4)));
        assertTrue(initial.isLegalMove(new Position(8, 4), new Position(7, 5)));
        assertFalse(initial.isLegalMove(new Position(8, 4), new Position(8, 3)));
        assertFalse(initial.isLegalMove(new Position(8, 4), new Position(8, 5)));
    }
    @Test
    public void isLegalMoveTest2() {
        assertFalse(inCheckMateState.isLegalMove(new Position(1, 1), new Position(1, 2)));
        assertFalse(inCheckMateState.isLegalMove(new Position(1, 1), new Position(1, 4)));
        assertFalse(inCheckMateState.isLegalMove(new Position(1, 1), new Position(1, 0)));
        assertTrue(inCheckMateState.isLegalMove(new Position(1, 1), new Position(1, 5)));
        assertFalse(inCheckMateState.isLegalMove(new Position(1, 1), new Position(1, 6)));
        assertTrue(inCheckMateState.isLegalMove(new Position(1, 7), new Position(2, 6)));
        assertFalse(inCheckMateState.isLegalMove(new Position(1, 1), new Position(3, 5)));
        assertTrue(inCheckMateState.isLegalMove(new Position(0, 5), new Position(1, 5)));
        assertTrue(inCheckMateState.isLegalMove(new Position(0, 6), new Position(1, 5)));
        assertTrue(inCheckMateState.isLegalMove(new Position(0, 7), new Position(2, 6)));
    }

    @Test
    public void isLegalToPutPieceToBoardTest1() {
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.LIGHT_PAWN, new Position(4, 0)));
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.LIGHT_GOLD, new Position(4, 0)));
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.LIGHT_SILVER, new Position(4, 0)));
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.LIGHT_ROOK, new Position(4, 0)));
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.LIGHT_BISHOP, new Position(4, 0)));
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.DARK_BISHOP, new Position(4, 0)));
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.LIGHT_LANCE, new Position(4, 0)));
    }
    @Test
    public void isLegalToPutPieceToBoardTest2() {
        initial.makeMove(new Position(6, 0), new Position(2, 0));
        initial.makeMove(new Position(0, 0), new Position(2, 0));
        assertTrue(initial.isLegalToPutPieceToBoard(Piece.LIGHT_PAWN, new Position(3, 0)));
        assertTrue(initial.isLegalToPutPieceToBoard(Piece.LIGHT_PAWN, new Position(4, 0)));
        assertTrue(initial.isLegalToPutPieceToBoard(Piece.LIGHT_PAWN, new Position(5, 0)));
        assertTrue(initial.isLegalToPutPieceToBoard(Piece.LIGHT_PAWN, new Position(6, 0)));
        assertTrue(initial.isLegalToPutPieceToBoard(Piece.LIGHT_PAWN, new Position(7, 0)));
    }
    @Test
    public void isLegalToPutPieceToBoardTest3() {
        initial.makeMove(new Position(6, 0), new Position(2, 0));
        initial.makeMove(new Position(0, 0), new Position(2, 0));
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.LIGHT_PAWN, new Position(3, 1)));
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.LIGHT_PAWN, new Position(3, 2)));
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.LIGHT_PAWN, new Position(3, 3)));
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.LIGHT_PAWN, new Position(3, 4)));
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.LIGHT_PAWN, new Position(3, 5)));
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.LIGHT_PAWN, new Position(3, 6)));
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.LIGHT_PAWN, new Position(3, 7)));
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.LIGHT_PAWN, new Position(3, 8)));
        assertFalse(initial.isLegalToPutPieceToBoard(Piece.LIGHT_PAWN, new Position(2, 0)));
    }

    @Test
    public void putPieceToBoardTest1() {
        initial.makeMove(new Position(6, 0), new Position(2, 0));
        initial.makeMove(new Position(0, 0), new Position(2, 0));
        initial.putPieceToBoard(Piece.LIGHT_PAWN, new Position(3, 0));
        assertEquals(Piece.LIGHT_PAWN, initial.getPiece(3, 0));
    }
    @Test
    public void putPieceToBoardTest2() {
        initial.makeMove(new Position(6, 0), new Position(2, 0));
        initial.makeMove(new Position(0, 0), new Position(2, 0));
        initial.putPieceToBoard(Piece.LIGHT_PAWN, new Position(3, 0));
        initial.putPieceToBoard(Piece.DARK_PAWN, new Position(5, 0));
        assertEquals(Piece.DARK_PAWN, initial.getPiece(5, 0));
    }

    @Test
    public void playerPiecesListTest1() {
        initial.makeMove(new Position(6, 0), new Position(2, 0));
        assertTrue(initial.getPlayer1Pieces().contains(Piece.LIGHT_PAWN));

        initial.makeMove(new Position(2, 1), new Position(6, 1));
        assertTrue(initial.getPlayer2Pieces().contains(Piece.DARK_PAWN));
    }
    @Test
    public void playerPiecesListTest2() {
        initial.makeMove(new Position(6, 0), new Position(1, 1));
        assertTrue(initial.getPlayer1Pieces().contains(Piece.LIGHT_ROOK));
    }
    @Test
    public void playerPiecesListTest3() {
        initial.makeMove(new Position(6, 0), new Position(0, 0));
        assertTrue(initial.getPlayer1Pieces().contains(Piece.LIGHT_LANCE));
    }
    @Test
    public void playerPiecesListTest4() {
        initial.makeMove(new Position(6, 0), new Position(0, 1));
        assertTrue(initial.getPlayer1Pieces().contains(Piece.LIGHT_KNIGHT));
    }
    @Test
    public void playerPiecesListTest5() {
        initial.makeMove(new Position(6, 0), new Position(0, 2));
        assertTrue(initial.getPlayer1Pieces().contains(Piece.LIGHT_SILVER));
    }
    @Test
    public void playerPiecesListTest6() {
        initial.makeMove(new Position(6, 0), new Position(0, 3));
        assertTrue(initial.getPlayer1Pieces().contains(Piece.LIGHT_GOLD));
    }
    @Test
    public void playerPiecesListTest7() {
        initial.makeMove(new Position(6, 0), new Position(1, 0));
        initial.makeMove(new Position(0, 0), new Position(1, 0));
        assertTrue(initial.getPlayer2Pieces().contains(Piece.DARK_PAWN));
    }
}
