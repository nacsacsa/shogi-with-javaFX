package shogi.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import shogi.model.utils.TwoPhaseMoveState;

import java.util.ArrayList;

public class ShogiState implements TwoPhaseMoveState<Position> {


    public static final int BOARD_SIZE = 9;
    private Player player;
    private final ReadOnlyObjectWrapper<Piece>[][] board;
    private Piece[][] tempBoard;
    public boolean[][] checkMateBoard = new boolean[BOARD_SIZE][BOARD_SIZE];
    private boolean[][] tempCheckMateBoard = new boolean[BOARD_SIZE][BOARD_SIZE];
    private ArrayList<Piece> player1Pieces;
    private ArrayList<Piece> player2Pieces;

    public ShogiState() {
        this.player = Player.PLAYER_1;
        tempBoard = new Piece[9][9];
        board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = new ReadOnlyObjectWrapper<>(
                        switch (row) {
                            case 0 -> darkBackLineFill(col);
                            case 1 -> darkSecondLine(col);
                            case 2 -> Piece.DARK_PAWN;
                            case 6 -> Piece.LIGHT_PAWN;
                            case 7 -> lightSecondLine(col);
                            case 8 -> lightBackLineFill(col);
                            default -> Piece.EMPTY;
                        }
                );
            }
        }
        createCheckMate(checkMateBoard ,board, player);
        checkMateReset(tempCheckMateBoard);
    }

    private Piece darkBackLineFill(int col) {
        Piece piece;
        switch (col) {
            case 0, 8 -> piece = Piece.DARK_LANCE;
            case 1, 7 -> piece = Piece.DARK_KNIGHT;
            case 2, 6 -> piece = Piece.DARK_SILVER;
            case 3, 5 -> piece = Piece.DARK_GOLD;
            case 4 -> piece = Piece.DARK_KING;
            default -> piece = Piece.EMPTY;
        }
        return piece;
    }

    private Piece darkSecondLine(int col) {
        Piece piece;
        switch (col) {
            case 1 -> piece = Piece.DARK_ROOK;
            case 7 -> piece = Piece.DARK_BISHOP;
            default -> piece = Piece.EMPTY;
        }
        return piece;
    }

    private Piece lightBackLineFill(int col) {
        Piece piece;
        switch (col) {
            case 0, 8 -> piece = Piece.LIGHT_LANCE;
            case 1, 7 -> piece = Piece.LIGHT_KNIGHT;
            case 2, 6 -> piece = Piece.LIGHT_SILVER;
            case 3, 5 -> piece = Piece.LIGHT_GOLD;
            case 4 -> piece = Piece.LIGHT_KING;
            default -> piece = Piece.EMPTY;
        }
        return piece;
    }

    private Piece lightSecondLine(int col) {
        Piece piece;
        switch (col) {
            case 1 -> piece = Piece.LIGHT_BISHOP;
            case 7 -> piece = Piece.LIGHT_ROOK;
            default -> piece = Piece.EMPTY;
        }
        return piece;
    }

    public ReadOnlyObjectProperty<Piece> getProperty(int row, int col) {
        return board[row][col].getReadOnlyProperty();
    }

    public Piece getPiece(int row, int col) {
        if (isOnBoard(new Position(row, col))) {
            return board[row][col].get();
        }
        return Piece.NONE;
    }

    public Piece tempGetPiece(int row, int col) {
        if (isOnBoard(new Position(row, col))) {
            return tempBoard[row][col];
        }
        return Piece.NONE;
    }

    private boolean isOnBoard(Position position) {
        return position.row() >= 0 && position.row() < BOARD_SIZE && position.col() >= 0 && position.col() < BOARD_SIZE;
    }

    private void createCheckMate(boolean[][] checkMateBoard, ReadOnlyObjectWrapper<Piece>[][] board, Player player) {
        checkMateReset(checkMateBoard);
        switch (player) {
            case PLAYER_1 -> fillDarkCheckMateTable(checkMateBoard, board);
            case PLAYER_2 -> fillLightCheckMateTable(checkMateBoard, board);
        }
    }

    private void createCheckMate(boolean[][] tempCheckMateBoard, Piece[][] tempBoard, Player player) {
        checkMateReset(tempCheckMateBoard);
        switch (player) {
            case PLAYER_1 -> fillDarkCheckMateTable(tempCheckMateBoard, tempBoard);
            case PLAYER_2 -> fillLightCheckMateTable(tempCheckMateBoard, tempBoard);
        }
    }

    private void fillDarkCheckMateTable(boolean[][] tempCheckMateBoard, Piece[][] tempBoard) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isTempDarkPiece(new Position(row, col))) {
                    switch (tempBoard[row][col]) {
                        case DARK_KING -> kingHitZone(new Position(row, col), tempCheckMateBoard);
                        case DARK_KNIGHT -> darkKnightHitZone(new Position(row, col), tempCheckMateBoard);
                        case DARK_LANCE -> darkLanceHitZoneTemp(new Position(row, col), tempCheckMateBoard);
                        case DARK_SILVER -> darkSilverHitZone(new Position(row, col), tempCheckMateBoard);
                        case DARK_ROOK -> rookHitZoneTemp(new Position(row, col), tempCheckMateBoard);
                        case DARK_PAWN -> darkPawnHitZone(new Position(row, col), tempCheckMateBoard);
                        case DARK_BISHOP -> bishopHitZoneTemp(new Position(row, col), tempCheckMateBoard);
                        case DARK_PROMOTED_BISHOP -> promotedBishopHitZoneTemp(new Position(row, col), tempCheckMateBoard);
                        case DARK_PROMOTED_ROOK -> promotedRookHitZoneTemp(new Position(row, col), tempCheckMateBoard);
                        case DARK_PROMOTED_SILVER, DARK_GOLD, DARK_PROMOTED_KNIGHT, DARK_PROMOTED_LANCE, DARK_PROMOTED_PAWN -> darkGoldHitZone(new Position(row, col), tempCheckMateBoard);
                    }
                }
            }
        }
    }

    private void fillDarkCheckMateTable(boolean[][] checkMateBoard, ReadOnlyObjectWrapper<Piece>[][] board) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isDarkPiece(new Position(row, col))) {
                    switch (board[row][col].get()) {
                        case DARK_KING -> kingHitZone(new Position(row, col), checkMateBoard);
                        case DARK_KNIGHT -> darkKnightHitZone(new Position(row, col), checkMateBoard);
                        case DARK_LANCE -> darkLanceHitZone(new Position(row, col), checkMateBoard);
                        case DARK_SILVER -> darkSilverHitZone(new Position(row, col), checkMateBoard);
                        case DARK_ROOK -> rookHitZone(new Position(row, col), checkMateBoard);
                        case DARK_PAWN -> darkPawnHitZone(new Position(row, col), checkMateBoard);
                        case DARK_BISHOP -> bishopHitZone(new Position(row, col), checkMateBoard);
                        case DARK_PROMOTED_BISHOP -> promotedBishopHitZone(new Position(row, col), checkMateBoard);
                        case DARK_PROMOTED_ROOK -> promotedRookHitZone(new Position(row, col), checkMateBoard);
                        case DARK_PROMOTED_SILVER, DARK_GOLD, DARK_PROMOTED_KNIGHT, DARK_PROMOTED_LANCE, DARK_PROMOTED_PAWN -> darkGoldHitZone(new Position(row, col), checkMateBoard);
                    }
                }
            }
        }
    }

    private void fillLightCheckMateTable(boolean[][] tempCheckMateBoard, Piece[][] tempBoard) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isTempLightPiece(new Position(row, col))) {
                    switch (tempBoard[row][col]) {
                        case LIGHT_KING -> kingHitZone(new Position(row, col), tempCheckMateBoard);
                        case LIGHT_KNIGHT -> lightKnightHitZone(new Position(row, col), tempCheckMateBoard);
                        case LIGHT_LANCE -> lightLanceHitZoneTemp(new Position(row, col), tempCheckMateBoard);
                        case LIGHT_SILVER -> lightSilverHitZone(new Position(row, col), tempCheckMateBoard);
                        case LIGHT_ROOK -> rookHitZoneTemp(new Position(row, col), tempCheckMateBoard);
                        case LIGHT_PAWN -> lightPawnHitZone(new Position(row, col), tempCheckMateBoard);
                        case LIGHT_BISHOP -> bishopHitZoneTemp(new Position(row, col), tempCheckMateBoard);
                        case LIGHT_PROMOTED_BISHOP -> promotedBishopHitZoneTemp(new Position(row, col), tempCheckMateBoard);
                        case LIGHT_PROMOTED_ROOK -> promotedRookHitZoneTemp(new Position(row, col), tempCheckMateBoard);
                        case LIGHT_PROMOTED_SILVER, LIGHT_GOLD, LIGHT_PROMOTED_KNIGHT, LIGHT_PROMOTED_LANCE, LIGHT_PROMOTED_PAWN -> lightGoldHitZone(new Position(row, col), tempCheckMateBoard);
                    }
                }
            }
        }
    }

    private void fillLightCheckMateTable(boolean[][] checkMateBoard, ReadOnlyObjectWrapper<Piece>[][] board) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isLightPiece(new Position(row, col))) {
                    switch (board[row][col].get()) {
                        case LIGHT_KING -> kingHitZone(new Position(row, col), checkMateBoard);
                        case LIGHT_KNIGHT -> lightKnightHitZone(new Position(row, col), checkMateBoard);
                        case LIGHT_LANCE -> lightLanceHitZone(new Position(row, col), checkMateBoard);
                        case LIGHT_SILVER -> lightSilverHitZone(new Position(row, col), checkMateBoard);
                        case LIGHT_ROOK -> rookHitZone(new Position(row, col), checkMateBoard);
                        case LIGHT_PAWN -> lightPawnHitZone(new Position(row, col), checkMateBoard);
                        case LIGHT_BISHOP -> bishopHitZone(new Position(row, col), checkMateBoard);
                        case LIGHT_PROMOTED_BISHOP -> promotedBishopHitZone(new Position(row, col), checkMateBoard);
                        case LIGHT_PROMOTED_ROOK -> promotedRookHitZone(new Position(row, col), checkMateBoard);
                        case LIGHT_PROMOTED_SILVER, LIGHT_GOLD, LIGHT_PROMOTED_KNIGHT, LIGHT_PROMOTED_LANCE, LIGHT_PROMOTED_PAWN -> lightGoldHitZone(new Position(row, col), checkMateBoard);
                    }
                }
            }
        }
    }

    private void kingHitZone(Position position, boolean[][] checkMateBoard) {
        if (isOnBoard(new Position(position.row() + 1, position.col() + 1))) {
            checkMateBoard[position.row() + 1][position.col() + 1] = true;
        }
        if (isOnBoard(new Position(position.row() + 1, position.col() - 1))) {
            checkMateBoard[position.row() + 1][position.col() - 1] = true;
        }
        if (isOnBoard(new Position(position.row() + 1, position.col()))) {
            checkMateBoard[position.row() + 1][position.col()] = true;
        }
        if (isOnBoard(new Position(position.row() - 1, position.col()))) {
            checkMateBoard[position.row() - 1][position.col()] = true;
        }
        if (isOnBoard(new Position(position.row() - 1, position.col() - 1))) {
            checkMateBoard[position.row() - 1][position.col() - 1] = true;
        }
        if (isOnBoard(new Position(position.row() - 1, position.col() + 1))) {
            checkMateBoard[position.row() - 1][position.col() + 1] = true;
        }
        if (isOnBoard(new Position(position.row(), position.col() + 1))) {
            checkMateBoard[position.row()][position.col() + 1] = true;
        }
        if (isOnBoard(new Position(position.row(), position.col() - 1))) {
            checkMateBoard[position.row()][position.col() - 1] = true;
        }
    }

    private void darkKnightHitZone(Position position, boolean[][] checkMateBoard) {
        if (isOnBoard(new Position(position.row() + 2, position.col() + 1))) {
            checkMateBoard[position.row() + 2][position.col() + 1] = true;
        }
        if (isOnBoard(new Position(position.row() + 2, position.col() - 1))) {
            checkMateBoard[position.row() + 2][position.col() - 1] = true;
        }
    }

    private void lightKnightHitZone(Position position, boolean[][] checkMateBoard) {
        if (isOnBoard(new Position(position.row() - 2, position.col() + 1))) {
            checkMateBoard[position.row() - 2][position.col() + 1] = true;
        }
        if (isOnBoard(new Position(position.row() - 2, position.col() - 1))) {
            checkMateBoard[position.row() - 2][position.col() - 1] = true;
        }
    }

    private void rookHitZone(Position position, boolean[][] checkMateBoard) {
        boolean legalMove = false;

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row(), position.col() + i))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row(), position.col() + j) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row()][position.col() + i] = true;
            }
            legalMove = false;
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() + i, position.col()))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() + j, position.col()) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() + i][position.col()] = true;
            }
            legalMove = false;
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row(), position.col() - i))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row(), position.col() - j) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row()][position.col() - i] = true;
            }
            legalMove = false;
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() - i, position.col()))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() - j, position.col()) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() - i][position.col()] = true;
            }
            legalMove = false;
        }
    }

    private void rookHitZoneTemp(Position position, boolean[][] checkMateBoard) {
        boolean legalMove = false;

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row(), position.col() + i))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(tempGetPiece(position.row(), position.col() + j) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row()][position.col() + i] = true;
            }
            legalMove = false;
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() + i, position.col()))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(tempGetPiece(position.row() + j, position.col()) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() + i][position.col()] = true;
            }
            legalMove = false;
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row(), position.col() - i))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(tempGetPiece(position.row(), position.col() - j) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row()][position.col() - i] = true;
            }
            legalMove = false;
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() - i, position.col()))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(tempGetPiece(position.row() - j, position.col()) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() - i][position.col()] = true;
            }
            legalMove = false;
        }
    }

    private void bishopHitZone(Position position, boolean[][] checkMateBoard) {
        boolean legalMove = false;

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() + i, position.col() + i))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() + j, position.col() + j) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() + i][position.col() + i] = true;
            }
            legalMove = false;
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() - i, position.col() - i))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() - j, position.col() - j) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() - i][position.col() - i] = true;
            }
            legalMove = false;
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() + i, position.col() - i))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() + j, position.col() - j) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() + i][position.col() - i] = true;
            }
            legalMove = false;
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() - i, position.col() + i))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() - j, position.col() + j) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() - i][position.col() + i] = true;
            }
            legalMove = false;
        }
    }

    private void bishopHitZoneTemp(Position position, boolean[][] checkMateBoard) {
        boolean legalMove = false;

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() + i, position.col() + i))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(tempGetPiece(position.row() + j, position.col() + j) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() + i][position.col() + i] = true;
            }
            legalMove = false;
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() - i, position.col() - i))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(tempGetPiece(position.row() - j, position.col() - j) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() - i][position.col() - i] = true;
            }
            legalMove = false;
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() + i, position.col() - i))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(tempGetPiece(position.row() + j, position.col() - j) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() + i][position.col() - i] = true;
            }
            legalMove = false;
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() - i, position.col() + i))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(tempGetPiece(position.row() - j, position.col() + j) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() - i][position.col() + i] = true;
            }
            legalMove = false;
        }
    }

    private void darkLanceHitZone(Position position, boolean[][] checkMateBoard) {
        boolean legalMove = false;

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() + i, position.col()))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() + j, position.col()) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() + i][position.col()] = true;
            }
            legalMove = false;
        }
    }

    private void darkLanceHitZoneTemp(Position position, boolean[][] checkMateBoard) {
        boolean legalMove = false;

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() + i, position.col()))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(tempGetPiece(position.row() + j, position.col()) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() + i][position.col()] = true;
            }
            legalMove = false;
        }
    }

    private void lightLanceHitZone(Position position, boolean[][] checkMateBoard) {
        boolean legalMove = false;

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() - i, position.col()))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() - j, position.col()) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() - i][position.col()] = true;
            }
            legalMove = false;
        }
    }

    private void lightLanceHitZoneTemp(Position position, boolean[][] checkMateBoard) {
        boolean legalMove = false;

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() - i, position.col()))) {
                if (i == 1) {
                    legalMove = true;
                }
                for (var j = 1; j < i; j++) {
                    if (!(tempGetPiece(position.row() - j, position.col()) == Piece.EMPTY)) {
                        legalMove = false;
                        break;
                    }
                    legalMove = true;
                }
            }
            if (legalMove) {
                checkMateBoard[position.row() - i][position.col()] = true;
            }
            legalMove = false;
        }
    }

    private void darkPawnHitZone(Position position, boolean[][] checkMateBoard) {
        if (isOnBoard(new Position(position.row() + 1, position.col()))) {
            checkMateBoard[position.row() + 1][position.col()] = true;
        }
    }

    private void lightPawnHitZone(Position position, boolean[][] checkMateBoard) {
        if (isOnBoard(new Position(position.row() - 1, position.col()))) {
            checkMateBoard[position.row() - 1][position.col()] = true;
        }
    }

    private void darkSilverHitZone(Position position, boolean[][] checkMateBoard) {
        if (isOnBoard(new Position(position.row() + 1, position.col()))) {
            checkMateBoard[position.row() + 1][position.col()] = true;
        }
        if (isOnBoard(new Position(position.row() + 1, position.col() + 1))) {
            checkMateBoard[position.row() + 1][position.col() + 1] = true;
        }
        if (isOnBoard(new Position(position.row() + 1, position.col() - 1))) {
            checkMateBoard[position.row() + 1][position.col() - 1] = true;
        }
        if (isOnBoard(new Position(position.row() - 1, position.col() - 1))) {
            checkMateBoard[position.row() - 1][position.col() - 1] = true;
        }
        if (isOnBoard(new Position(position.row() - 1, position.col() + 1))) {
            checkMateBoard[position.row() - 1][position.col() + 1] = true;
        }
    }

    private void lightSilverHitZone(Position position, boolean[][] checkMateBoard) {
        if (isOnBoard(new Position(position.row() - 1, position.col()))) {
            checkMateBoard[position.row() - 1][position.col()] = true;
        }
        if (isOnBoard(new Position(position.row() - 1, position.col() + 1))) {
            checkMateBoard[position.row() - 1][position.col() + 1] = true;
        }
        if (isOnBoard(new Position(position.row() - 1, position.col() - 1))) {
            checkMateBoard[position.row() - 1][position.col() - 1] = true;
        }
        if (isOnBoard(new Position(position.row() + 1, position.col() - 1))) {
            checkMateBoard[position.row() + 1][position.col() - 1] = true;
        }
        if (isOnBoard(new Position(position.row() + 1, position.col() + 1))) {
            checkMateBoard[position.row() + 1][position.col() + 1] = true;
        }
    }

    private void promotedBishopHitZoneTemp(Position position, boolean[][] checkMateBoard) {
        kingHitZone(position, checkMateBoard);
        bishopHitZoneTemp(position, checkMateBoard);
    }

    private void promotedBishopHitZone(Position position, boolean[][] checkMateBoard) {
        kingHitZone(position, checkMateBoard);
        bishopHitZone(position, checkMateBoard);
    }

    private void promotedRookHitZoneTemp(Position position, boolean[][] checkMateBoard) {
        kingHitZone(position, checkMateBoard);
        rookHitZoneTemp(position, checkMateBoard);
    }

    private void darkGoldHitZone(Position position, boolean[][] checkMateBoard) {
        if (isOnBoard(new Position(position.row() + 1, position.col()))) {
            checkMateBoard[position.row() + 1][position.col()] = true;
        }
        if (isOnBoard(new Position(position.row() + 1, position.col() + 1))) {
            checkMateBoard[position.row() + 1][position.col() + 1] = true;
        }
        if (isOnBoard(new Position(position.row() + 1, position.col() - 1))) {
            checkMateBoard[position.row() + 1][position.col() - 1] = true;
        }
        if (isOnBoard(new Position(position.row(), position.col() - 1))) {
            checkMateBoard[position.row()][position.col() - 1] = true;
        }
        if (isOnBoard(new Position(position.row(), position.col() + 1))) {
            checkMateBoard[position.row()][position.col() + 1] = true;
        }
        if (isOnBoard(new Position(position.row() - 1, position.col()))) {
            checkMateBoard[position.row() - 1][position.col()] = true;
        }
    }

    private void promotedRookHitZone(Position position, boolean[][] checkMateBoard) {
        kingHitZone(position, checkMateBoard);
        rookHitZone(position, checkMateBoard);
    }

    private void lightGoldHitZone(Position position, boolean[][] checkMateBoard) {
        if (isOnBoard(new Position(position.row() - 1, position.col()))) {
            checkMateBoard[position.row() - 1][position.col()] = true;
        }
        if (isOnBoard(new Position(position.row() - 1, position.col() + 1))) {
            checkMateBoard[position.row() - 1][position.col() + 1] = true;
        }
        if (isOnBoard(new Position(position.row() - 1, position.col() - 1))) {
            checkMateBoard[position.row() - 1][position.col() - 1] = true;
        }
        if (isOnBoard(new Position(position.row(), position.col() - 1))) {
            checkMateBoard[position.row()][position.col() - 1] = true;
        }
        if (isOnBoard(new Position(position.row(), position.col() + 1))) {
            checkMateBoard[position.row()][position.col() + 1] = true;
        }
        if (isOnBoard(new Position(position.row() + 1, position.col()))) {
            checkMateBoard[position.row() + 1][position.col()] = true;
        }
    }

    private boolean isLightPiece(Position position) {
        return getPiece(position.row(), position.col()) == Piece.LIGHT_KING
                || getPiece(position.row(), position.col()) == Piece.LIGHT_GOLD
                || getPiece(position.row(), position.col()) == Piece.LIGHT_SILVER
                || getPiece(position.row(), position.col()) == Piece.LIGHT_KNIGHT
                || getPiece(position.row(), position.col()) == Piece.LIGHT_LANCE
                || getPiece(position.row(), position.col()) == Piece.LIGHT_ROOK
                || getPiece(position.row(), position.col()) == Piece.LIGHT_BISHOP
                || getPiece(position.row(), position.col()) == Piece.LIGHT_PAWN
                || getPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_ROOK
                || getPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_SILVER
                || getPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_PAWN
                || getPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_LANCE
                || getPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_KNIGHT
                || getPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_BISHOP;
    }

    private boolean isTempLightPiece(Position position) {
        return tempGetPiece(position.row(), position.col()) == Piece.LIGHT_KING
                || tempGetPiece(position.row(), position.col()) == Piece.LIGHT_GOLD
                || tempGetPiece(position.row(), position.col()) == Piece.LIGHT_SILVER
                || tempGetPiece(position.row(), position.col()) == Piece.LIGHT_KNIGHT
                || tempGetPiece(position.row(), position.col()) == Piece.LIGHT_LANCE
                || tempGetPiece(position.row(), position.col()) == Piece.LIGHT_ROOK
                || tempGetPiece(position.row(), position.col()) == Piece.LIGHT_BISHOP
                || tempGetPiece(position.row(), position.col()) == Piece.LIGHT_PAWN
                || tempGetPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_ROOK
                || tempGetPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_SILVER
                || tempGetPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_PAWN
                || tempGetPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_LANCE
                || tempGetPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_KNIGHT
                || tempGetPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_BISHOP;
    }

    private boolean isDarkPiece(Position position) {
        return getPiece(position.row(), position.col()) == Piece.DARK_KING
                || getPiece(position.row(), position.col()) == Piece.DARK_GOLD
                || getPiece(position.row(), position.col()) == Piece.DARK_SILVER
                || getPiece(position.row(), position.col()) == Piece.DARK_KNIGHT
                || getPiece(position.row(), position.col()) == Piece.DARK_LANCE
                || getPiece(position.row(), position.col()) == Piece.DARK_ROOK
                || getPiece(position.row(), position.col()) == Piece.DARK_BISHOP
                || getPiece(position.row(), position.col()) == Piece.DARK_PAWN
                || getPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_ROOK
                || getPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_SILVER
                || getPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_PAWN
                || getPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_LANCE
                || getPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_KNIGHT
                || getPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_BISHOP;
    }

    private boolean isTempDarkPiece(Position position) {
        return tempGetPiece(position.row(), position.col()) == Piece.DARK_KING
                || tempGetPiece(position.row(), position.col()) == Piece.DARK_GOLD
                || tempGetPiece(position.row(), position.col()) == Piece.DARK_SILVER
                || tempGetPiece(position.row(), position.col()) == Piece.DARK_KNIGHT
                || tempGetPiece(position.row(), position.col()) == Piece.DARK_LANCE
                || tempGetPiece(position.row(), position.col()) == Piece.DARK_ROOK
                || tempGetPiece(position.row(), position.col()) == Piece.DARK_BISHOP
                || tempGetPiece(position.row(), position.col()) == Piece.DARK_PAWN
                || tempGetPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_ROOK
                || tempGetPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_SILVER
                || tempGetPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_PAWN
                || tempGetPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_LANCE
                || tempGetPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_KNIGHT
                || tempGetPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_BISHOP;
    }

    private void checkMateReset(boolean[][] board) {
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = false;
            }
        }
    }

    /**
     * {@return whether it is checkmate or not.
     * @param {@code checkMateBoard} the board which
     * contains the possible spaces where the player can {@link #makeMove(Position, Position)}.
     */
    public boolean isCheckMate(boolean[][] checkMateBoard) {
        switch (getNextPlayer()) {
            case PLAYER_1 -> {
                return isKingInCheckMate(Piece.LIGHT_KING, checkMateBoard);
            }
            case PLAYER_2 -> {
                return isKingInCheckMate(Piece.DARK_KING, checkMateBoard);
            }
        }
        throw new ArithmeticException("No player found!");
    }

    private boolean isCheckMateTemp(boolean[][] checkMateBoard) {
        switch (getNextPlayer()) {
            case PLAYER_1 -> {
                return isKingInCheckMateTemp(Piece.LIGHT_KING, checkMateBoard);
            }
            case PLAYER_2 -> {
                return isKingInCheckMateTemp(Piece.DARK_KING, checkMateBoard);
            }
        }
        throw new ArithmeticException("No player found!");
    }

    private boolean isKingInCheckMate(Piece piece, boolean[][] checkMateBoard) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (getPiece(row, col) == piece) {
                    return checkMateBoard[row][col];
                }
            }
        }
        return false;
    }

    private boolean isKingInCheckMateTemp(Piece piece, boolean[][] checkMateBoard) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (tempGetPiece(row, col) == piece) {
                    return checkMateBoard[row][col];
                }
            }
        }
        return false;
    }

    /**
     * Applies the move provided to the state. This method should be called if
     * and only if {@link #isLegalMove(Position, Position)} returns {@code true}.
     *
     * @param from represents where to move from
     * @param to   represents where to move to
     */
    @Override
    public void makeMove(Position from, Position to) {
        if (isEnemyTerritory(to)) {
            board[to.row()][to.col()].set(upgradePiece(from));
        }
        else {
            board[to.row()][to.col()].set(getPiece(from.row(), from.col()));
        }
        board[from.row()][from.col()].set(Piece.EMPTY);
        player = player.opponent();
        createCheckMate(checkMateBoard, board, player);
    }

    private boolean isEnemyTerritory(Position to) {
        switch (getNextPlayer()) {
            case PLAYER_1 -> {
                return to.row() <= 2;
            }
            case PLAYER_2 -> {
                return to.row() >= 6;
            }
        }
        return false;
    }

    private Piece upgradePiece(Position from) {
        switch (getPiece(from.row(), from.col())) {
            case DARK_PAWN -> {
                return Piece.DARK_PROMOTED_PAWN;
            }
            case DARK_LANCE -> {
                return Piece.DARK_PROMOTED_LANCE;
            }
            case LIGHT_PAWN -> {
                return Piece.LIGHT_PROMOTED_PAWN;
            }
            case DARK_KNIGHT -> {
                return Piece.DARK_PROMOTED_KNIGHT;
            }
            case DARK_SILVER -> {
                return Piece.DARK_PROMOTED_SILVER;
            }
            case LIGHT_LANCE -> {
                return Piece.LIGHT_PROMOTED_LANCE;
            }
            case LIGHT_KNIGHT -> {
                return Piece.LIGHT_PROMOTED_KNIGHT;
            }
            case LIGHT_SILVER -> {
                return Piece.LIGHT_PROMOTED_SILVER;
            }
            case DARK_ROOK -> {
                return Piece.DARK_PROMOTED_ROOK;
            }
            case LIGHT_ROOK -> {
                return Piece.LIGHT_PROMOTED_ROOK;
            }
            case DARK_BISHOP -> {
                return Piece.DARK_PROMOTED_BISHOP;
            }
            case LIGHT_BISHOP -> {
                return Piece.LIGHT_PROMOTED_BISHOP;
            }
            default -> {
                return getPiece(from.row(), from.col());
            }
        }
    }

    /**
     * {@return whether it is possible to make a move from the argument
     * specified}
     *
     * @param from represents where to move from
     */
    @Override
    public boolean isLegalToMoveFrom(Position from) {
        return isOnBoard(from) && !isEmpty(from) && isPlayerPiece(from) && pieceCanMove(from) && isCheckMateEvasionMove(from) && isCheckMateOver(from) && isNotCausesCheckMate(from);
    }

    private boolean isEmpty(Position position) {
        if (isOnBoard(position)) {
            return getPiece(position.row(), position.col()) == Piece.EMPTY;
        }
        return false;
    }

    private boolean pieceCanMove(Position position) {
        Piece piece = getPiece(position.row(), position.col());
        switch (piece) {
            case LIGHT_KING, DARK_KING, LIGHT_PROMOTED_BISHOP, DARK_PROMOTED_BISHOP, LIGHT_PROMOTED_ROOK, DARK_PROMOTED_ROOK -> {
                return kingCanMove(position);
            }
            case LIGHT_KNIGHT -> {
                return lightKnightCanMove(position);
            }
            case DARK_KNIGHT -> {
                return darkKnightCanMove(position);
            }
            case LIGHT_LANCE, LIGHT_PAWN -> {
                return lightPawnAndLanceCanMove(position);
            }
            case DARK_LANCE, DARK_PAWN -> {
                return darkPawnAndLanceCanMove(position);
            }
            case LIGHT_SILVER -> {
                return lightSilverCanMove(position);
            }
            case DARK_SILVER -> {
                return darkSilverCanMove(position);
            }
            case LIGHT_BISHOP, DARK_BISHOP -> {
                return bishopCanMove(position);
            }
            case LIGHT_ROOK, DARK_ROOK -> {
                return rookCanMove(position);
            }
            case LIGHT_PROMOTED_KNIGHT, LIGHT_PROMOTED_LANCE, LIGHT_PROMOTED_PAWN, LIGHT_PROMOTED_SILVER, LIGHT_GOLD ->
            {
                return lightGoldCanMove(position);
            }
            case DARK_PROMOTED_KNIGHT, DARK_PROMOTED_LANCE, DARK_PROMOTED_PAWN, DARK_PROMOTED_SILVER, DARK_GOLD -> {
                return darkGoldCanMove(position);
            }
            default -> {
                return false;
            }
        }
    }

    private boolean kingCanMove(Position position) {
        return isEmpty(new Position(position.row() + 1, position.col() + 1))
                || isEmpty(new Position(position.row() + 1, position.col() - 1))
                || isEmpty(new Position(position.row() + 1, position.col()))
                || isEmpty(new Position(position.row() - 1, position.col()))
                || isEmpty(new Position(position.row() - 1, position.col() - 1))
                || isEmpty(new Position(position.row() - 1, position.col() + 1))
                || isEmpty(new Position(position.row(), position.col() + 1))
                || isEmpty(new Position(position.row(), position.col() - 1))

                || isEnemyPiece(new Position(position.row() + 1, position.col() + 1))
                || isEnemyPiece(new Position(position.row() + 1, position.col() - 1))
                || isEnemyPiece(new Position(position.row() + 1, position.col()))
                || isEnemyPiece(new Position(position.row() - 1, position.col()))
                || isEnemyPiece(new Position(position.row() - 1, position.col() - 1))
                || isEnemyPiece(new Position(position.row() - 1, position.col() + 1))
                || isEnemyPiece(new Position(position.row(), position.col() + 1))
                || isEnemyPiece(new Position(position.row(), position.col() - 1));
    }

    private boolean lightKnightCanMove(Position position) {
        return isEmpty(new Position(position.row() - 2, position.col() + 1))
                || isEmpty(new Position(position.row() -2 , position.col() - 1))

                || isEnemyPiece(new Position(position.row() - 2, position.col() + 1))
                || isEnemyPiece(new Position(position.row() -2 , position.col() - 1));
    }

    private boolean darkKnightCanMove(Position position) {
        return isEmpty(new Position(position.row() + 2, position.col() + 1))
                || isEmpty(new Position(position.row() +2 , position.col() - 1))

                || isEnemyPiece(new Position(position.row() + 2, position.col() + 1))
                || isEnemyPiece(new Position(position.row() + 2 , position.col() - 1));
    }

    private boolean lightPawnAndLanceCanMove(Position position) {
        return isEmpty(new Position(position.row() - 1, position.col()))
                || isEnemyPiece(new Position(position.row() - 1, position.col()));
    }

    private boolean darkPawnAndLanceCanMove(Position position) {
        return isEmpty(new Position(position.row() + 1, position.col()))
                || isEnemyPiece(new Position(position.row() + 1, position.col()));
    }

    private boolean lightSilverCanMove(Position position) {
        return isEmpty(new Position(position.row() - 1, position.col()))
                || isEmpty(new Position(position.row() - 1, position.col() - 1))
                || isEmpty(new Position(position.row() - 1, position.col() + 1))
                || isEmpty(new Position(position.row() + 1, position.col() - 1))
                || isEmpty(new Position(position.row() + 1, position.col() + 1))

                || isEnemyPiece(new Position(position.row() - 1, position.col()))
                || isEnemyPiece(new Position(position.row() - 1, position.col() - 1))
                || isEnemyPiece(new Position(position.row() - 1, position.col() + 1))
                || isEnemyPiece(new Position(position.row() + 1, position.col() - 1))
                || isEnemyPiece(new Position(position.row() + 1, position.col() + 1));
    }

    private boolean darkSilverCanMove(Position position) {
        return isEmpty(new Position(position.row() + 1, position.col()))
                || isEmpty(new Position(position.row() + 1, position.col() - 1))
                || isEmpty(new Position(position.row() + 1, position.col() + 1))
                || isEmpty(new Position(position.row() - 1, position.col() - 1))
                || isEmpty(new Position(position.row() - 1, position.col() + 1))

                || isEnemyPiece(new Position(position.row() + 1, position.col()))
                || isEnemyPiece(new Position(position.row() + 1, position.col() - 1))
                || isEnemyPiece(new Position(position.row() + 1, position.col() + 1))
                || isEnemyPiece(new Position(position.row() - 1, position.col() - 1))
                || isEnemyPiece(new Position(position.row() - 1, position.col() + 1));
    }

    private boolean bishopCanMove(Position position) {
        return isEmpty(new Position(position.row() + 1, position.col() + 1))
                || isEmpty(new Position(position.row() - 1, position.col() - 1))
                || isEmpty(new Position(position.row() - 1, position.col() + 1))
                || isEmpty(new Position(position.row() + 1, position.col() - 1))

                || isEnemyPiece(new Position(position.row() + 1, position.col() + 1))
                || isEnemyPiece(new Position(position.row() - 1, position.col() - 1))
                || isEnemyPiece(new Position(position.row() - 1, position.col() + 1))
                || isEnemyPiece(new Position(position.row() + 1, position.col() - 1));
    }

    private boolean rookCanMove(Position position) {
        return isEmpty(new Position(position.row(), position.col()))
                || isEmpty(new Position(position.row() + 1, position.col()))
                || isEmpty(new Position(position.row() - 1, position.col()))
                || isEmpty(new Position(position.row(), position.col() + 1))
                || isEmpty(new Position(position.row(), position.col() - 1))

                || isEnemyPiece(new Position(position.row() + 1, position.col()))
                || isEnemyPiece(new Position(position.row() - 1, position.col()))
                || isEnemyPiece(new Position(position.row(), position.col() + 1))
                || isEnemyPiece(new Position(position.row(), position.col() - 1));
    }

    private boolean lightGoldCanMove(Position position) {
        return isEmpty(new Position(position.row() - 1, position.col()))
                || isEmpty(new Position(position.row() - 1, position.col() - 1))
                || isEmpty(new Position(position.row() - 1, position.col() + 1))
                || isEmpty(new Position(position.row(), position.col() - 1))
                || isEmpty(new Position(position.row(), position.col() + 1))
                || isEmpty(new Position(position.row() + 1, position.col()))

                || isEnemyPiece(new Position(position.row() - 1, position.col()))
                || isEnemyPiece(new Position(position.row() - 1, position.col() - 1))
                || isEnemyPiece(new Position(position.row() - 1, position.col() + 1))
                || isEnemyPiece(new Position(position.row(), position.col() - 1))
                || isEnemyPiece(new Position(position.row(), position.col() + 1))
                || isEnemyPiece(new Position(position.row() + 1, position.col()));
    }

    private boolean darkGoldCanMove(Position position) {
        return isEmpty(new Position(position.row() + 1, position.col()))
                || isEmpty(new Position(position.row() + 1, position.col() - 1))
                || isEmpty(new Position(position.row() + 1, position.col() + 1))
                || isEmpty(new Position(position.row(), position.col() - 1))
                || isEmpty(new Position(position.row(), position.col() + 1))
                || isEmpty(new Position(position.row() - 1, position.col()))

                || isEnemyPiece(new Position(position.row() + 1, position.col()))
                || isEnemyPiece(new Position(position.row() + 1, position.col() - 1))
                || isEnemyPiece(new Position(position.row() + 1, position.col() + 1))
                || isEnemyPiece(new Position(position.row(), position.col() - 1))
                || isEnemyPiece(new Position(position.row(), position.col() + 1))
                || isEnemyPiece(new Position(position.row() - 1, position.col()));
    }

    private boolean isEnemyPiece(Position position) {
        if (getPiece(position.row(), position.col()) == Piece.EMPTY || getPiece(position.row(), position.col()) == Piece.NONE) {
            return false;
        }
        return !isPlayerPiece(position);
    }

    private boolean isCheckMateEvasionMove(Position from) {
        if (!isCheckMate(checkMateBoard)) {
            return true;
        }
        return isCheckMateOver(from);
    }

    private boolean isCheckMateOver(Position position) {
        Piece piece = board[position.row()][position.col()].get();
        switch (piece) {
            case LIGHT_KING, DARK_KING -> {
                return isKingMoveEndCheckMate(position);
            }
            case LIGHT_BISHOP, DARK_BISHOP -> {
                return isBishopMoveEndCheckMate(position);
            }
            case LIGHT_ROOK, DARK_ROOK -> {
                return isRookMoveEndCheckMate(position);
            }
            case LIGHT_KNIGHT -> {
                return isLightKnightMoveEndCheckMate(position);
            }
            case DARK_KNIGHT -> {
                return isDarkKnightMoveEndCheckMate(position);
            }
            case LIGHT_PAWN -> {
                return isLightPawnMoveEndCheckMate(position);
            }
            case DARK_PAWN -> {
                return isDarkPawnMoveEndCheckMate(position);
            }
            case LIGHT_LANCE -> {
                return isLightLanceMoveEndCheckMate(position);
            }
            case DARK_LANCE -> {
                return isDarkLanceMoveEndCheckMate(position);
            }
            case LIGHT_SILVER -> {
                return isLightSilverMoveEndCheckMate(position);
            }
            case DARK_SILVER -> {
                return isDarkSilverMoveEndCheckMate(position);
            }
            case LIGHT_PROMOTED_BISHOP, DARK_PROMOTED_BISHOP -> {
                return isPromotedBishopMoveEndCheckMate(position);
            }
            case LIGHT_PROMOTED_ROOK, DARK_PROMOTED_ROOK -> {
                return isPromotedRookMoveEndCheckMate(position);
            }
            case LIGHT_GOLD, LIGHT_PROMOTED_KNIGHT, LIGHT_PROMOTED_LANCE, LIGHT_PROMOTED_PAWN, LIGHT_PROMOTED_SILVER ->
            {
                return isLightGoldMoveEndCheckMate(position);
            }
            case DARK_GOLD, DARK_PROMOTED_KNIGHT, DARK_PROMOTED_LANCE, DARK_PROMOTED_PAWN, DARK_PROMOTED_SILVER -> {
                return isDarkGoldMoveEndCheckMate(position);
            }
            default -> {
                return false;
            }
        }
    }

    private boolean isKingMoveEndCheckMate(Position position) {
        Position pos1 = new Position(position.row() + 1, position.col() + 1);
        Position pos2 = new Position(position.row() + 1, position.col() - 1);
        Position pos3 = new Position(position.row() + 1, position.col());
        Position pos4 = new Position(position.row(), position.col() + 1);
        Position pos5 = new Position(position.row(), position.col() - 1);
        Position pos6 = new Position(position.row() - 1, position.col() - 1);
        Position pos7 = new Position(position.row() - 1, position.col() + 1);
        Position pos8 = new Position(position.row() - 1, position.col());
        return moveToEndCheckMate(position, pos1) || moveToEndCheckMate(position, pos2) ||
                moveToEndCheckMate(position, pos3) || moveToEndCheckMate(position, pos4) ||
                moveToEndCheckMate(position, pos5) || moveToEndCheckMate(position, pos6) ||
                moveToEndCheckMate(position, pos7) || moveToEndCheckMate(position, pos8);
    }

    private boolean isBishopMoveEndCheckMate(Position position) {
        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() + i, position.col() + i))) {
                if (i == 1) {
                    if (moveToEndCheckMate(position, new Position(position.row() + i, position.col() + i))) {
                        return true;
                    }
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() + j, position.col() + j) == Piece.EMPTY)) {
                        break;
                    }
                    if (moveToEndCheckMate(position, new Position(position.row() + i, position.col() + i))) {
                        return true;
                    }
                }
            }
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() - i, position.col() - i))) {
                if (i == 1) {
                    if (moveToEndCheckMate(position, new Position(position.row() - i, position.col() - i))) {
                        return true;
                    }
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() - j, position.col() - j) == Piece.EMPTY)) {
                        break;
                    }
                    if (moveToEndCheckMate(position, new Position(position.row() - i, position.col() - i))) {
                        return true;
                    }
                }
            }
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() + i, position.col() - i))) {
                if (i == 1) {
                    if (moveToEndCheckMate(position, new Position(position.row() + i, position.col() - i))) {
                        return true;
                    }
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() + j, position.col() - j) == Piece.EMPTY)) {
                        break;
                    }
                    if (moveToEndCheckMate(position, new Position(position.row() + i, position.col() - i))) {
                        return true;
                    }
                }
            }
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() -  i, position.col() + i))) {
                if (i == 1) {
                    if (moveToEndCheckMate(position, new Position(position.row() - i, position.col() + i))) {
                        return true;
                    }
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() - j, position.col() + j) == Piece.EMPTY)) {
                        break;
                    }
                    if (moveToEndCheckMate(position, new Position(position.row() - i, position.col() + i))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isRookMoveEndCheckMate(Position position) {
        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row(), position.col() + i))) {
                if (i == 1) {
                    if (moveToEndCheckMate(position, new Position(position.row(), position.col() + i))) {
                        return true;
                    }
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row(), position.col() + j) == Piece.EMPTY)) {
                        break;
                    }
                    if (moveToEndCheckMate(position, new Position(position.row(), position.col() + i))) {
                        return true;
                    }
                }
            }
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() + i, position.col()))) {
                if (i == 1) {
                    if (moveToEndCheckMate(position, new Position(position.row() + i, position.col()))) {
                        return true;
                    }
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() + j, position.col()) == Piece.EMPTY)) {
                        break;
                    }
                    if (moveToEndCheckMate(position, new Position(position.row() + i, position.col()))) {
                        return true;
                    }
                }
            }
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row(), position.col() - i))) {
                if (i == 1) {
                    if (moveToEndCheckMate(position, new Position(position.row(), position.col() - i))) {
                        return true;
                    }
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row(), position.col() - j) == Piece.EMPTY)) {
                        break;
                    }
                    if (moveToEndCheckMate(position, new Position(position.row(), position.col() - i))) {
                        return true;
                    }
                }
            }
        }

        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() -  i, position.col()))) {
                if (i == 1) {
                    if (moveToEndCheckMate(position, new Position(position.row() - i, position.col()))) {
                        return true;
                    }
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() - j, position.col()) == Piece.EMPTY)) {
                        break;
                    }
                    if (moveToEndCheckMate(position, new Position(position.row() - i, position.col()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isLightKnightMoveEndCheckMate(Position position) {
        Position pos1 = new Position(position.row() - 2, position.col() + 1);
        Position pos2 = new Position(position.row() - 2, position.col() - 1);
        return moveToEndCheckMate(position, pos1) || moveToEndCheckMate(position, pos2);
    }

    private boolean isDarkKnightMoveEndCheckMate(Position position) {
        Position pos1 = new Position(position.row() + 2, position.col() + 1);
        Position pos2 = new Position(position.row() + 2, position.col() - 1);
        return moveToEndCheckMate(position, pos1) || moveToEndCheckMate(position, pos2);
    }

    private boolean isLightPawnMoveEndCheckMate(Position position) {
        return moveToEndCheckMate(position, new Position(position.row() - 1, position.col()));
    }

    private boolean isDarkPawnMoveEndCheckMate(Position position) {
        return moveToEndCheckMate(position, new Position(position.row() + 1, position.col()));
    }

    private boolean isLightLanceMoveEndCheckMate(Position position) {
        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() -  i, position.col()))) {
                if (i == 1) {
                    if (moveToEndCheckMate(position, new Position(position.row() - i, position.col()))) {
                        return true;
                    }
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() - j, position.col()) == Piece.EMPTY)) {
                        break;
                    }
                    if (moveToEndCheckMate(position, new Position(position.row() - i, position.col()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isDarkLanceMoveEndCheckMate(Position position) {
        for (var i = 1; i < BOARD_SIZE; i++) {
            if (isOnBoard(new Position(position.row() +  i, position.col()))) {
                if (i == 1) {
                    if (moveToEndCheckMate(position, new Position(position.row() + i, position.col()))) {
                        return true;
                    }
                }
                for (var j = 1; j < i; j++) {
                    if (!(getPiece(position.row() + j, position.col()) == Piece.EMPTY)) {
                        break;
                    }
                    if (moveToEndCheckMate(position, new Position(position.row() + i, position.col()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isLightSilverMoveEndCheckMate(Position position) {
        Position pos1 = new Position(position.row() - 1, position.col() + 1);
        Position pos2 = new Position(position.row() - 1, position.col() - 1);
        Position pos3 = new Position(position.row() - 1, position.col());
        Position pos4 = new Position(position.row() + 1, position.col() - 1);
        Position pos5 = new Position(position.row() + 1, position.col() + 1);
        return moveToEndCheckMate(position, pos1) || moveToEndCheckMate(position, pos2)
                || moveToEndCheckMate(position, pos3) || moveToEndCheckMate(position, pos4)
                || moveToEndCheckMate(position, pos5);
    }

    private boolean isDarkSilverMoveEndCheckMate(Position position) {
        Position pos1 = new Position(position.row() + 1, position.col() + 1);
        Position pos2 = new Position(position.row() + 1, position.col() - 1);
        Position pos3 = new Position(position.row() + 1, position.col());
        Position pos4 = new Position(position.row() - 1, position.col() - 1);
        Position pos5 = new Position(position.row() - 1, position.col() + 1);
        return moveToEndCheckMate(position, pos1) || moveToEndCheckMate(position, pos2)
                || moveToEndCheckMate(position, pos3) || moveToEndCheckMate(position, pos4)
                || moveToEndCheckMate(position, pos5);
    }

    private boolean isPromotedBishopMoveEndCheckMate(Position position) {
        return isBishopMoveEndCheckMate(position) || isKingMoveEndCheckMate(position);
    }

    private boolean isPromotedRookMoveEndCheckMate(Position position) {
        return isRookMoveEndCheckMate(position) || isKingMoveEndCheckMate(position);
    }

    private boolean isLightGoldMoveEndCheckMate(Position position) {
        Position pos1 = new Position(position.row() - 1, position.col() + 1);
        Position pos2 = new Position(position.row() - 1, position.col() - 1);
        Position pos3 = new Position(position.row() - 1, position.col());
        Position pos4 = new Position(position.row(), position.col() - 1);
        Position pos5 = new Position(position.row(), position.col() + 1);
        Position pos6 = new Position(position.row() + 1, position.col());
        return moveToEndCheckMate(position, pos1) || moveToEndCheckMate(position, pos2)
                || moveToEndCheckMate(position, pos3) || moveToEndCheckMate(position, pos4)
                || moveToEndCheckMate(position, pos5) || moveToEndCheckMate(position, pos6);
    }

    private boolean isDarkGoldMoveEndCheckMate(Position position) {
        Position pos1 = new Position(position.row() + 1, position.col() + 1);
        Position pos2 = new Position(position.row() + 1, position.col() - 1);
        Position pos3 = new Position(position.row() + 1, position.col());
        Position pos4 = new Position(position.row(), position.col() - 1);
        Position pos5 = new Position(position.row(), position.col() + 1);
        Position pos6 = new Position(position.row() - 1, position.col());
        return moveToEndCheckMate(position, pos1) || moveToEndCheckMate(position, pos2)
                || moveToEndCheckMate(position, pos3) || moveToEndCheckMate(position, pos4)
                || moveToEndCheckMate(position, pos5) || moveToEndCheckMate(position, pos6);
    }

    private boolean moveToEndCheckMate(Position from, Position to) {
        tempBoard = copyBoard();
        if (isOnBoard(to) || isEnemyPiece(to) || isEmpty(to)) {
            tempBoard[to.row()][to.col()] = (getPiece(from.row(), from.col()));
            tempBoard[from.row()][from.col()] = (Piece.EMPTY);
            createCheckMate(tempCheckMateBoard, tempBoard, player);
            return !isCheckMateTemp(tempCheckMateBoard);
        }
        return false;
    }

    private Piece[][] copyBoard() {
        Piece[][] tempBoard = new Piece[BOARD_SIZE][BOARD_SIZE];
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                tempBoard[i][j] = getPiece(i,j);
            }
        }
        return tempBoard;
    }

    private boolean isNotCausesCheckMate(Position position) {
        Piece piece = board[position.row()][position.col()].get();
        switch (piece) {
            case LIGHT_KING, DARK_KING -> {
                return isKingCanMove(position);
            }
            case LIGHT_BISHOP, DARK_BISHOP -> {
                return isBishopNotMoveToCheckMate(position);
            }
            case LIGHT_ROOK, DARK_ROOK -> {
                return isRookNotMoveToCheckMate(position);
            }
            case LIGHT_KNIGHT -> {
                return isLightKnightNotMoveToCheckMate(position);
            }
            case DARK_KNIGHT -> {
                return isDarkKnightNotMoveToCheckMate(position);
            }
            case LIGHT_PAWN -> {
                return isLightPawnNotMoveToCheckMate(position);
            }
            case DARK_PAWN -> {
                return isDarkPawnNotMoveToCheckMate(position);
            }
            case LIGHT_LANCE -> {
                return isLightLanceNotMoveToCheckMate(position);
            }
            case DARK_LANCE -> {
                return isDarkLanceNotMoveToCheckMate(position);
            }
            case LIGHT_SILVER -> {
                return isLightSilverNotMoveToCheckMate(position);
            }
            case DARK_SILVER -> {
                return isDarkSilverNotMoveToCheckMate(position);
            }
            case LIGHT_PROMOTED_BISHOP, DARK_PROMOTED_BISHOP -> {
                return isPromotedBishopNotMoveToCheckMate(position);
            }
            case LIGHT_PROMOTED_ROOK, DARK_PROMOTED_ROOK -> {
                return isPromotedRookNotMoveToCheckMate(position);
            }
            case LIGHT_GOLD, LIGHT_PROMOTED_KNIGHT, LIGHT_PROMOTED_LANCE, LIGHT_PROMOTED_PAWN, LIGHT_PROMOTED_SILVER ->
            {
                return isLightGoldNotMoveToCheckMate(position);
            }
            case DARK_GOLD, DARK_PROMOTED_KNIGHT, DARK_PROMOTED_LANCE, DARK_PROMOTED_PAWN, DARK_PROMOTED_SILVER -> {
                return isDarkGoldNotMoveToCheckMate(position);
            }
            default -> {
                return false;
            }
        }
    }

    private boolean isKingCanMove(Position from) {
        boolean move1 = false;
        boolean move2 = false;
        boolean move3 = false;
        boolean move4 = false;
        boolean move5 = false;
        boolean move6 = false;
        boolean move7 = false;
        boolean move8 = false;

        if (isOnBoard(new Position(from.row() + 1, from.col() + 1)) && (isEmpty(new Position(from.row() + 1, from.col() + 1)) || isEnemyPiece(new Position(from.row() + 1, from.col() + 1)))) {
            move1 = !checkMateBoard[from.row() + 1][from.col() + 1];
        }
        if (isOnBoard(new Position(from.row() + 1, from.col() - 1)) && (isEmpty(new Position(from.row() + 1, from.col() - 1)) || isEnemyPiece(new Position(from.row() + 1, from.col() - 1)))) {
            move2 = !checkMateBoard[from.row() + 1][from.col() - 1];
        }
        if (isOnBoard(new Position(from.row() + 1, from.col())) && (isEmpty(new Position(from.row() + 1, from.col())) || isEnemyPiece(new Position(from.row() + 1, from.col())))) {
            move3 = !checkMateBoard[from.row() + 1][from.col()];
        }
        if (isOnBoard(new Position(from.row(), from.col() + 1)) && (isEmpty(new Position(from.row(), from.col() + 1)) || isEnemyPiece(new Position(from.row(), from.col() + 1)))) {
            move4 = !checkMateBoard[from.row()][from.col() + 1];
        }
        if (isOnBoard(new Position(from.row() - 1, from.col() - 1)) && (isEmpty(new Position(from.row() - 1, from.col() - 1)) || isEnemyPiece(new Position(from.row() - 1, from.col() - 1)))) {
            move5 = !checkMateBoard[from.row() - 1][from.col() - 1];
        }
        if (isOnBoard(new Position(from.row() - 1, from.col() + 1)) && (isEmpty(new Position(from.row() - 1, from.col() + 1)) || isEnemyPiece(new Position(from.row() - 1, from.col() + 1)))) {
            move6 = !checkMateBoard[from.row() - 1][from.col() + 1];
        }
        if (isOnBoard(new Position(from.row() - 1, from.col())) && (isEmpty(new Position(from.row() - 1, from.col())) || isEnemyPiece(new Position(from.row() - 1, from.col())))) {
            move7 = !checkMateBoard[from.row() - 1][from.col()];
        }
        if (isOnBoard(new Position(from.row(), from.col() - 1)) && (isEmpty(new Position(from.row(), from.col() - 1)) || isEnemyPiece(new Position(from.row(), from.col() - 1)))) {
            move8 = !checkMateBoard[from.row()][from.col() - 1];
        }
        return move1 || move2 || move3 || move4 || move5 || move6 || move7 || move8;
    }

    private boolean isRookNotMoveToCheckMate(Position position) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isLegalMoveWithRook(position, new Position(row, col))) {
                    if (moveToEndCheckMate(position, new Position(row, col)) && getPiece(row, col) != Piece.LIGHT_KING && getPiece(row, col) != Piece.DARK_KING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isBishopNotMoveToCheckMate(Position position) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isLegalMoveWithBishop(position, new Position(row, col))) {
                    if (moveToEndCheckMate(position, new Position(row, col)) && getPiece(row, col) != Piece.LIGHT_KING && getPiece(row, col) != Piece.DARK_KING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isLegalMoveWithBishop(Position from, Position to) {
        return isLegalToMoveWithBishopRightUp(from, to) || isLegalToMoveWithBishopRightDown(from, to)
                || isLegalToMoveWithBishopLeftUp(from, to) || isLegalToMoveWithBishopLeftDown(from, to);
    }

    private boolean isLegalToMoveWithBishopLeftUp(Position from, Position to) {
        for (var i = 1; i < BOARD_SIZE; i++) {
            if (from.row() == to.row() + i && from.col() == to.col() + i) {
                for (var j = 1; j < (from.col() - to.col()); j++) {
                    if (!isEmpty(new Position(from.row() - j, from.col() - j))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean isLegalToMoveWithBishopLeftDown(Position from, Position to) {
        for (var i = 1; i < BOARD_SIZE; i++) {
            if (from.row() == to.row() - i && from.col() == to.col() + i) {
                for (var j = 1; j < (to.row()- from.row()); j++) {
                    if (!isEmpty(new Position(from.row() + j, from.col() - j))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean isLegalToMoveWithBishopRightDown(Position from, Position to) {
        for (var i = 1; i < BOARD_SIZE; i++) {
            if (from.row() == to.row() - i && from.col() == to.col() - i) {
                for (var j = 1; j < (to.row() - from.row()); j++) {
                    if (!isEmpty(new Position(from.row() + j, from.col() + j))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean isLegalToMoveWithBishopRightUp(Position from, Position to) {
        for (var i = 1; i < BOARD_SIZE; i++) {
            if (from.row() == to.row() + i && from.col() == to.col() - i) {
                for (var j = 1; j < (to.col() - from.col()); j++) {
                    if (!isEmpty(new Position(from.row() - j, from.col() + j))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean isLegalMoveWithRook(Position from, Position to) {
        return isLegalToMoveWithRookRight(from, to) || isLegalToMoveWithRookLeft(from, to)
                || isLegalToMoveWithRookUp(from, to) || isLegalToMoveWithRookDown(from, to);
    }

    private boolean isLegalToMoveWithRookDown(Position from, Position to) {
        if (from.col() != to.col()) {
            return false;
        }
        if (from.row() > to.row()) {
            return false;
        }
        for (var i = from.row() + 1; i < to.row(); i++) {
            if (!isEmpty(new Position(i, from.col()))) {
                return false;
            }
        }
        return true;
    }

    private boolean isLegalToMoveWithRookUp(Position from, Position to) {
        if (from.col() != to.col()) {
            return false;
        }
        if (from.row() < to.row()) {
            return false;
        }
        for (var i = from.row() - 1; i > to.row(); i--) {
            if (!isEmpty(new Position(i, from.col()))) {
                return false;
            }
        }
        return true;
    }

    private boolean isLegalToMoveWithRookLeft(Position from, Position to) {
        if (from.row() != to.row()) {
            return false;
        }
        if (from.col() < to.col()) {
            return false;
        }
        for (var i = from.col() - 1; i > to.col(); i--) {
            if (!isEmpty(new Position(from.row(), i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isLegalToMoveWithRookRight(Position from, Position to) {
        if (from.row() != to.row()) {
            return false;
        }
        if (from.col() > to.col()) {
            return false;
        }
        for (var i = from.col() + 1; i < to.col(); i++) {
            if (!isEmpty(new Position(from.row(), i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isDarkKnightNotMoveToCheckMate(Position position) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isLegalMoveWithDarkKnight(position, new Position(row, col))) {
                    if (moveToEndCheckMate(position, new Position(row, col)) && getPiece(row, col) != Piece.LIGHT_KING && getPiece(row, col) != Piece.DARK_KING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isLightKnightNotMoveToCheckMate(Position position) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isLegalMoveWithLightKnight(position, new Position(row, col))) {
                    if (moveToEndCheckMate(position, new Position(row, col)) && getPiece(row, col) != Piece.LIGHT_KING && getPiece(row, col) != Piece.DARK_KING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isLegalMoveWithDarkKnight(Position from, Position to) {
        return (from.row() == to.row() - 2 && from.col() == to.col() + 1)
                || (from.row() == to.row() - 2 && from.col() == to.col() - 1);
    }

    private boolean isLegalMoveWithLightKnight(Position from, Position to) {
        return (from.row() == to.row() + 2 && from.col() == to.col() + 1)
                || (from.row() == to.row() + 2 && from.col() == to.col() - 1);
    }

    private boolean isLightPawnNotMoveToCheckMate(Position position) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isLegalMoveWithLightPawn(position, new Position(row, col))) {
                    if (moveToEndCheckMate(position, new Position(row, col)) && getPiece(row, col) != Piece.LIGHT_KING && getPiece(row, col) != Piece.DARK_KING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isDarkPawnNotMoveToCheckMate(Position position) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isLegalMoveWithDarkPawn(position, new Position(row, col))) {
                    if (moveToEndCheckMate(position, new Position(row, col)) && getPiece(row, col) != Piece.LIGHT_KING && getPiece(row, col) != Piece.DARK_KING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isLegalMoveWithLightPawn(Position from, Position to) {
        return (from.row() == to.row() + 1 && from.col() == to.col());
    }

    private boolean isLegalMoveWithDarkPawn(Position from, Position to) {
        return (from.row() == to.row() - 1 && from.col() == to.col());
    }

    private boolean isLightLanceNotMoveToCheckMate(Position position) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isLegalMoveWithLightLance(position, new Position(row, col))) {
                    if (moveToEndCheckMate(position, new Position(row, col)) && getPiece(row, col) != Piece.LIGHT_KING && getPiece(row, col) != Piece.DARK_KING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isDarkLanceNotMoveToCheckMate(Position position) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isLegalMoveWithDarkLance(position, new Position(row, col))) {
                    if (moveToEndCheckMate(position, new Position(row, col)) && getPiece(row, col) != Piece.LIGHT_KING && getPiece(row, col) != Piece.DARK_KING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isLegalMoveWithLightLance(Position from, Position to) {
        return isLegalToMoveWithRookUp(from, to);
    }

    private boolean isLegalMoveWithDarkLance(Position from, Position to) {
        return isLegalToMoveWithRookDown(from, to);
    }

    private boolean isLightSilverNotMoveToCheckMate(Position position) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isLegalMoveWithLightSilver(position, new Position(row, col))) {
                    if (moveToEndCheckMate(position, new Position(row, col)) && getPiece(row, col) != Piece.LIGHT_KING && getPiece(row, col) != Piece.DARK_KING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isDarkSilverNotMoveToCheckMate(Position position) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isLegalMoveWithDarkSilver(position, new Position(row, col))) {
                    if (moveToEndCheckMate(position, new Position(row, col)) && getPiece(row, col) != Piece.LIGHT_KING && getPiece(row, col) != Piece.DARK_KING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isLegalMoveWithLightSilver(Position from, Position to) {
        return (from.row() == to.row() + 1 && from.col() == to.col() + 1)
                || (from.row() == to.row() + 1 && from.col() == to.col() - 1)
                || (from.row() == to.row() + 1 && from.col() == to.col())
                || (from.row() == to.row() - 1 && from.col() == to.col() - 1)
                || (from.row() == to.row() - 1 && from.col() == to.col() + 1);
    }

    private boolean isLegalMoveWithDarkSilver(Position from, Position to) {
        return (from.row() == to.row() - 1 && from.col() == to.col() + 1)
                || (from.row() == to.row() - 1 && from.col() == to.col() - 1)
                || (from.row() == to.row() - 1 && from.col() == to.col())
                || (from.row() == to.row() + 1 && from.col() == to.col() - 1)
                || (from.row() == to.row() + 1 && from.col() == to.col() + 1);
    }

    private boolean isPromotedBishopNotMoveToCheckMate(Position position) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isLegalMoveWithPromotedBishop(position, new Position(row, col))) {
                    if (moveToEndCheckMate(position, new Position(row, col)) && getPiece(row, col) != Piece.LIGHT_KING && getPiece(row, col) != Piece.DARK_KING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isPromotedRookNotMoveToCheckMate(Position position) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isLegalMoveWithPromotedRook(position, new Position(row, col))) {
                    if (moveToEndCheckMate(position, new Position(row, col)) && getPiece(row, col) != Piece.LIGHT_KING && getPiece(row, col) != Piece.DARK_KING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isLegalMoveWithKing(Position from, Position to) {
        return ((from.row() == to.row() - 1 && from.col() == to.col() - 1))
                || ((from.row() == to.row() - 1 && from.col() == to.col() + 1))
                || ((from.row() == to.row() - 1 && from.col() == to.col()))
                || ((from.row() == to.row() + 1 && from.col() == to.col()))
                || ((from.row() == to.row() && from.col() == to.col() - 1))
                || ((from.row() == to.row() && from.col() == to.col() + 1))
                || ((from.row() == to.row() + 1 && from.col() == to.col() + 1))
                || ((from.row() == to.row() + 1 && from.col() == to.col() - 1));
    }

    private boolean isLightGoldNotMoveToCheckMate(Position position) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isLegalMoveWithLightGold(position, new Position(row, col))) {
                    if (moveToEndCheckMate(position, new Position(row, col)) && getPiece(row, col) != Piece.LIGHT_KING && getPiece(row, col) != Piece.DARK_KING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isDarkGoldNotMoveToCheckMate(Position position) {
        for (var row = 0; row < BOARD_SIZE; row++) {
            for (var col = 0; col < BOARD_SIZE; col++) {
                if (isLegalMoveWithDarkGold(position, new Position(row, col))) {
                    if (moveToEndCheckMate(position, new Position(row, col)) && getPiece(row, col) != Piece.LIGHT_KING && getPiece(row, col) != Piece.DARK_KING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isLegalMoveWithLightGold(Position from, Position to) {
        return (from.row() == to.row() + 1 && from.col() == to.col() + 1)
                || (from.row() == to.row() + 1 && from.col() == to.col() - 1)
                || (from.row() == to.row() + 1 && from.col() == to.col())
                || (from.row() == to.row() && from.col() == to.col() - 1)
                || (from.row() == to.row() && from.col() == to.col() + 1)
                || (from.row() == to.row() - 1 && from.col() == to.col());
    }

    private boolean isLegalMoveWithDarkGold(Position from, Position to) {
        return (from.row() == to.row() - 1 && from.col() == to.col() + 1)
                || (from.row() == to.row() - 1 && from.col() == to.col() - 1)
                || (from.row() == to.row() - 1 && from.col() == to.col())
                || (from.row() == to.row() && from.col() == to.col() - 1)
                || (from.row() == to.row() && from.col() == to.col() + 1)
                || (from.row() == to.row() + 1 && from.col() == to.col());
    }

    private boolean isLegalMoveWithPromotedBishop(Position from, Position to) {
        return isLegalMoveWithBishop(from, to) || isLegalMoveWithKing(from, to);
    }

    private boolean isLegalMoveWithPromotedRook(Position from, Position to) {
        return isLegalMoveWithRook(from, to) || isLegalMoveWithKing(from, to);
    }

    /**
     * {@return whether the move provided can be applied to the state}
     *
     * @param from represents where to move from
     * @param to   represents where to move to
     */
    @Override
    public boolean isLegalMove(Position from, Position to) {
        return isLegalToMoveFrom(from) && isOnBoard(to) && (isEmpty(to) || isEnemyPiece(to)) && isLegalToMoveWithPiece(from, to) && isLegalToMoveIfCheckMate(from, to);
    }

    private boolean isLegalToMoveWithPiece(Position from, Position to) {
        switch (getPiece(from.row(), from.col())) {
            case LIGHT_KING, DARK_KING -> {
                return isLegalMoveWithKing(from, to);
            }
            case LIGHT_BISHOP, DARK_BISHOP -> {
                return isLegalMoveWithBishop(from, to);
            }
            case LIGHT_ROOK, DARK_ROOK -> {
                return isLegalMoveWithRook(from, to);
            }
            case LIGHT_KNIGHT -> {
                return isLegalMoveWithLightKnight(from, to);
            }
            case DARK_KNIGHT -> {
                return isLegalMoveWithDarkKnight(from, to);
            }
            case LIGHT_PAWN -> {
                return isLegalMoveWithLightPawn(from, to);
            }
            case DARK_PAWN -> {
                return isLegalMoveWithDarkPawn(from, to);
            }
            case LIGHT_LANCE -> {
                return isLegalMoveWithLightLance(from, to);
            }
            case DARK_LANCE -> {
                return isLegalMoveWithDarkLance(from, to);
            }
            case LIGHT_SILVER -> {
                return isLegalMoveWithLightSilver(from, to);
            }
            case DARK_SILVER -> {
                return isLegalMoveWithDarkSilver(from, to);
            }
            case LIGHT_PROMOTED_BISHOP, DARK_PROMOTED_BISHOP -> {
                return isLegalMoveWithPromotedBishop(from, to);
            }
            case LIGHT_PROMOTED_ROOK, DARK_PROMOTED_ROOK -> {
                return isLegalMoveWithPromotedRook(from, to);
            }
            case LIGHT_GOLD, LIGHT_PROMOTED_KNIGHT, LIGHT_PROMOTED_LANCE, LIGHT_PROMOTED_PAWN, LIGHT_PROMOTED_SILVER ->
            {
                return isLegalMoveWithLightGold(from, to);
            }
            case DARK_GOLD, DARK_PROMOTED_KNIGHT, DARK_PROMOTED_LANCE, DARK_PROMOTED_PAWN, DARK_PROMOTED_SILVER -> {
                return isLegalMoveWithDarkGold(from, to);
            }
            default -> {
                return false;
            }
        }
    }

    private boolean isLegalToMoveIfCheckMate(Position from, Position to) {
        return moveToEndCheckMate(from, to);
    }

    /**
     * {@return the status of the game}
     */
    @Override
    public Status getStatus() {
        if (!isGameOver()) {
            return Status.IN_PROGRESS;
        }
        return player == Player.PLAYER_2 ? Status.PLAYER_1_WINS : Status.PLAYER_2_WINS;
    }

    /**
     * {@return the player who moves next}
     */
    @Override
    public Player getNextPlayer() {
        return player;
    }

    /**
     * {@return whether the game is over}
     */
    @Override
    public boolean isGameOver() {
        return !isPlayerCanMove();
    }

    private boolean isPlayerCanMove() {
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                if (isPlayerPiece(new Position(i, j))) {
                    if (isLegalToMoveFrom(new Position(i, j))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isPlayerPiece(Position position) {
        if (Player.PLAYER_1 == player) {
            return getPiece(position.row(), position.col()) == Piece.LIGHT_PAWN ||
                    getPiece(position.row(), position.col()) == Piece.LIGHT_LANCE ||
                    getPiece(position.row(), position.col()) == Piece.LIGHT_SILVER ||
                    getPiece(position.row(), position.col()) == Piece.LIGHT_GOLD ||
                    getPiece(position.row(), position.col()) == Piece.LIGHT_KNIGHT ||
                    getPiece(position.row(), position.col()) == Piece.LIGHT_BISHOP ||
                    getPiece(position.row(), position.col()) == Piece.LIGHT_ROOK ||
                    getPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_BISHOP ||
                    getPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_KNIGHT ||
                    getPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_LANCE ||
                    getPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_PAWN ||
                    getPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_SILVER ||
                    getPiece(position.row(), position.col()) == Piece.LIGHT_PROMOTED_ROOK ||
                    getPiece(position.row(), position.col()) == Piece.LIGHT_KING;
        }
        else
            return getPiece(position.row(), position.col()) == Piece.DARK_PAWN ||
                    getPiece(position.row(), position.col()) == Piece.DARK_LANCE ||
                    getPiece(position.row(), position.col()) == Piece.DARK_SILVER ||
                    getPiece(position.row(), position.col()) == Piece.DARK_GOLD ||
                    getPiece(position.row(), position.col()) == Piece.DARK_KNIGHT ||
                    getPiece(position.row(), position.col()) == Piece.DARK_BISHOP ||
                    getPiece(position.row(), position.col()) == Piece.DARK_ROOK ||
                    getPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_BISHOP ||
                    getPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_KNIGHT ||
                    getPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_LANCE ||
                    getPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_PAWN ||
                    getPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_SILVER ||
                    getPiece(position.row(), position.col()) == Piece.DARK_PROMOTED_ROOK ||
                    getPiece(position.row(), position.col()) == Piece.DARK_KING;
    }
}
