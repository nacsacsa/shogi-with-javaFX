package shogi.controllers;

import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import shogi.model.Piece;
import shogi.model.Position;
import shogi.model.ShogiState;
import shogi.model.utils.EnumImageStorage;
import shogi.model.utils.ImageStorage;
import shogi.model.utils.TwoPhaseMoveSelector;

public class ShogiStateController {
    
    public Label darkPawnNumberLabel;
    public Label darkKnightNumberLabel;
    public Label darkSilverNumberLabel;
    public Label darkGoldNumberLabel;
    public Label darkBishopNumberLabel;
    public Label darkRookNumberLabel;
    public Label darkLanceNumberLabel;
    public Label lightRookNumberLabel;
    public Label lightSilverNumberLabel;
    public Label lightLanceLabel;
    public Label lightPawnLabel;
    public Label lightBishopLabel;
    public Label lightKnightNumberLabel;
    public Label LightGoldNumberLabel;
    private Piece thePiece;
    private boolean isButtonPressed;

    @FXML
    private GridPane board;
    private final ShogiState model = new ShogiState();
    private final TwoPhaseMoveSelector<Position> selector = new TwoPhaseMoveSelector<>(model);
    private final ImageStorage<Piece> imageStorage = new EnumImageStorage<>(Piece.class);
    @FXML
    private void initialize() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square,j, i);
            }
        }
        selector.phaseProperty().addListener(this::showSelectionPhaseChange);
        thePiece = Piece.EMPTY;
        isButtonPressed = false;
        selector.reset();
    }

    private StackPane createSquare(int row, int col) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        var imageView = new ImageView();
        imageView.setFitWidth(75);
        imageView.setFitHeight(75);
        imageView.imageProperty().bind(
                new ObjectBinding<Image>() {
                    {
                        super.bind(model.getProperty(row, col));
                    }
                    @Override
                    protected Image computeValue() {
                        return imageStorage.get(model.getPiece(row, col));
                    }
                }
            );
        square.getChildren().add(imageView);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        noCheckMateColor();
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        selector.select(new Position(row, col));
        if (isButtonPressed) {
            if (model.isLegalToPutPieceToBoard(thePiece, new Position(row, col))) {
                model.putPieceToBoard(thePiece, new Position(row, col));
                isButtonPressed = false;
                updateRooster();
                if (model.isGameOver()) {
                    switch (model.getNextPlayer()) {
                        case PLAYER_1 -> colorWhiteKing();
                        case PLAYER_2 -> colorBlackKing();
                    }
                    gameOverAlertAndExit();
                }
                colorCheckMate();
                notShowWhereIsPossibleToPutPiece();
            }
            else {
                isButtonPressed = false;
                notShowWhereIsPossibleToPutPiece();
            }
        }
        else {
            if (selector.isReadyToMove()) {
                selector.makeMove();
                updateRooster();
                if (model.isGameOver()) {
                    switch (model.getNextPlayer()) {
                        case PLAYER_1 -> colorWhiteKing();
                        case PLAYER_2 -> colorBlackKing();
                    }
                    gameOverAlertAndExit();
                }
            }
            colorCheckMate();
        }
    }

    private void colorCheckMate() {
        if (model.isCheckMate(model.getCheckMateBoard())) {
            switch (model.getNextPlayer()) {
                case PLAYER_1 -> colorWhiteKing();
                case PLAYER_2 -> colorBlackKing();
            }
        }
        else {
            noCheckMateColor();
        }
    }

    private void updateRooster() {
            int pawn = 0;
            int lance = 0;
            int knight = 0;
            int silver = 0;
            int gold = 0;
            int bishop = 0;
            int rook = 0;
            for (var i = 0; i < model.getPlayer1Pieces().size(); i++) {
                switch (model.getPlayer1Pieces().get(i)) {
                    case LIGHT_PAWN -> pawn++;
                    case LIGHT_LANCE -> lance++;
                    case LIGHT_KNIGHT -> knight++;
                    case LIGHT_SILVER -> silver++;
                    case LIGHT_GOLD -> gold++;
                    case LIGHT_BISHOP -> bishop++;
                    case LIGHT_ROOK -> rook++;
                }
            }
            lightPawnLabel.setText(((Integer)pawn).toString());
            lightLanceLabel.setText(((Integer)lance).toString());
            lightKnightNumberLabel.setText(((Integer)knight).toString());
            lightSilverNumberLabel.setText(((Integer)silver).toString());
            LightGoldNumberLabel.setText(((Integer)gold).toString());
            lightBishopLabel.setText(((Integer)bishop).toString());
            lightRookNumberLabel.setText(((Integer)rook).toString());

        pawn = 0;
        lance = 0;
        knight = 0;
        silver = 0;
        gold = 0;
        bishop = 0;
        rook = 0;
        for (var i = 0; i < model.getPlayer2Pieces().size(); i++) {
            switch (model.getPlayer2Pieces().get(i)) {
                case DARK_PAWN -> pawn++;
                case DARK_LANCE -> lance++;
                case DARK_KNIGHT -> knight++;
                case DARK_SILVER -> silver++;
                case DARK_GOLD -> gold++;
                case DARK_BISHOP -> bishop++;
                case DARK_ROOK -> rook++;
            }
        }
        darkPawnNumberLabel.setText(((Integer)pawn).toString());
        darkLanceNumberLabel.setText(((Integer)lance).toString());
        darkKnightNumberLabel.setText(((Integer)knight).toString());
        darkSilverNumberLabel.setText(((Integer)silver).toString());
        darkGoldNumberLabel.setText(((Integer)gold).toString());
        darkBishopNumberLabel.setText(((Integer)bishop).toString());
        darkRookNumberLabel.setText(((Integer)rook).toString());
    }

    private void noCheckMateColor() {
            for (var i = 0; i < board.getRowCount(); i++) {
                for (var j = 0; j < board.getColumnCount(); j++) {
                    var square = getSquare(new Position(i, j));
                    square.getStyleClass().remove("checkMate");
                }
            }
        }
        private void gameOverAlertAndExit() {
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Game Over");
            alert.setContentText("Game Over");
            alert.showAndWait();
            Platform.exit();
        }
        private void showSelectionPhaseChange(ObservableValue<? extends TwoPhaseMoveSelector.Phase> value, TwoPhaseMoveSelector.Phase oldPhase, TwoPhaseMoveSelector.Phase newPhase) {
            switch (newPhase) {
                case SELECT_FROM -> {}
                case SELECT_TO -> showSelection(selector.getFrom());
                case READY_TO_MOVE -> hideSelection(selector.getFrom(), selector.getTo());
            }
        }

        private void showSelection(Position position) {
            var square = getSquare(position);
            square.getStyleClass().add("selected");
            showWhereIsPossibleToMove(position);
        }

        private void showWhereIsPossibleToMove(Position position) {
            for (var i = 0; i < board.getRowCount(); i++) {
                for (var j = 0; j < board.getColumnCount(); j++) {
                    if (model.isLegalMove(position, new Position(i, j))) {
                        var square = getSquare(new Position(i, j));
                        square.getStyleClass().add("possibleMoves");
                    }
                }
            }
        }

        private void notShowWhereIsPossibleToMove(Position position) {
            for (var i = 0; i < board.getRowCount(); i++) {
                for (var j = 0; j < board.getColumnCount(); j++) {
                    if (model.isLegalMove(position, new Position(i, j))) {
                        var square = getSquare(new Position(i, j));
                        square.getStyleClass().remove("possibleMoves");
                    }
                }
            }
        }

        private void hideSelection(Position from, Position to) {
            hideLastMoves();
            var square = getSquare(from);
            var squareTo = getSquare(to);
            square.getStyleClass().remove("selected");
            notShowWhereIsPossibleToMove(from);
            square.getStyleClass().add("lastMoveTo");
            squareTo.getStyleClass().add("lastMoveTo");
            if (model.isCheckMate(model.getCheckMateBoard()))  {
                noCheckMateColor();
            }
        }

        private void colorBlackKing() {
            for (var i = 0; i < board.getRowCount(); i++) {
                for (var j = 0; j < board.getColumnCount(); j++) {
                    if (model.getPiece(i, j) == Piece.DARK_KING) {
                        var square = getSquare(new Position(i, j));
                        square.getStyleClass().add("checkMate");
                    }
                }
            }
        }

        private void colorWhiteKing() {
            for (var i = 0; i < board.getRowCount(); i++) {
                for (var j = 0; j < board.getColumnCount(); j++) {
                    if (model.getPiece(i, j) == Piece.LIGHT_KING) {
                        var square = getSquare(new Position(i, j));
                        square.getStyleClass().add("checkMate");
                    }
                }
            }
        }

        private void hideLastMoves() {
            for (var i = 0; i < board.getRowCount(); i++) {
                for (var j = 0; j < board.getColumnCount(); j++) {
                    var square = getSquare(new Position(i, j));
                    square.getStyleClass().remove("lastMoveTo");
                }
            }
        }

        private StackPane getSquare(Position position) {
            for (var child : board.getChildren()) {
                if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                    return (StackPane) child;
                }
            }
            throw new AssertionError();
        }

    private void showWhereIsPossibleToPutPiece() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                if (model.isLegalToPutPieceToBoard(thePiece, new Position(i, j))) {
                    var square = getSquare(new Position(i, j));
                    square.getStyleClass().add("possibleMoves");
                }
            }
        }
    }

    private void notShowWhereIsPossibleToPutPiece() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = getSquare(new Position(i, j));
                square.getStyleClass().remove("possibleMoves");
            }
        }
    }

    public void addPawnPieceOnClick(ActionEvent actionEvent) {
            boolean isReady;
            switch (model.getNextPlayer()) {
            case PLAYER_1 -> {
                isReady = model.getPlayer1Pieces().contains(Piece.LIGHT_PAWN);
                thePiece = Piece.LIGHT_PAWN;
            }
            case PLAYER_2 -> {
                isReady = model.getPlayer2Pieces().contains(Piece.DARK_PAWN);
                thePiece = Piece.DARK_PAWN;
            }
            default -> isReady = false;
        }
        if (isReady) {
           selector.isReadyToMove();
           isButtonPressed = true;
           showWhereIsPossibleToPutPiece();
        }
    }

    public void addLancePieceOnClick(ActionEvent actionEvent) {
        boolean isReady;
        switch (model.getNextPlayer()) {
            case PLAYER_1 -> {
                isReady = model.getPlayer1Pieces().contains(Piece.LIGHT_LANCE);
                thePiece = Piece.LIGHT_LANCE;
            }
            case PLAYER_2 -> {
                isReady = model.getPlayer2Pieces().contains(Piece.DARK_LANCE);
                thePiece = Piece.DARK_LANCE;
            }
            default -> isReady = false;
        }
        if (isReady) {
            selector.isReadyToMove();
            isButtonPressed = true;
            showWhereIsPossibleToPutPiece();
        }
    }

    public void addKnightPieceOnClick(ActionEvent actionEvent) {
        boolean isReady;
        switch (model.getNextPlayer()) {
            case PLAYER_1 -> {
                isReady = model.getPlayer1Pieces().contains(Piece.LIGHT_KNIGHT);
                thePiece = Piece.LIGHT_KNIGHT;
            }
            case PLAYER_2 -> {
                isReady = model.getPlayer2Pieces().contains(Piece.DARK_KNIGHT);
                thePiece = Piece.DARK_KNIGHT;
            }
            default -> isReady = false;
        }
        if (isReady) {
            selector.isReadyToMove();
            isButtonPressed = true;
            showWhereIsPossibleToPutPiece();
        }
    }

    public void addSilverPieceOnClick(ActionEvent actionEvent) {
        boolean isReady;
        switch (model.getNextPlayer()) {
            case PLAYER_1 -> {
                isReady = model.getPlayer1Pieces().contains(Piece.LIGHT_SILVER);
                thePiece = Piece.LIGHT_SILVER;
            }
            case PLAYER_2 -> {
                isReady = model.getPlayer2Pieces().contains(Piece.DARK_SILVER);
                thePiece = Piece.DARK_SILVER;
            }
            default -> isReady = false;
        }
        if (isReady) {
            selector.isReadyToMove();
            isButtonPressed = true;
            showWhereIsPossibleToPutPiece();
        }
    }

    public void addGoldPieceOnClick(ActionEvent actionEvent) {
        boolean isReady;
        switch (model.getNextPlayer()) {
            case PLAYER_1 -> {
                isReady = model.getPlayer1Pieces().contains(Piece.LIGHT_GOLD);
                thePiece = Piece.LIGHT_GOLD;
            }
            case PLAYER_2 -> {
                isReady = model.getPlayer2Pieces().contains(Piece.DARK_GOLD);
                thePiece = Piece.DARK_GOLD;
            }
            default -> isReady = false;
        }
        if (isReady) {
            selector.isReadyToMove();
            isButtonPressed = true;
            showWhereIsPossibleToPutPiece();
        }
    }

    public void addBishopPieceOnClick(ActionEvent actionEvent) {
        boolean isReady;
        switch (model.getNextPlayer()) {
            case PLAYER_1 -> {
                isReady = model.getPlayer1Pieces().contains(Piece.LIGHT_BISHOP);
                thePiece = Piece.LIGHT_BISHOP;
            }
            case PLAYER_2 -> {
                isReady = model.getPlayer2Pieces().contains(Piece.DARK_BISHOP);
                thePiece = Piece.DARK_BISHOP;
            }
            default -> isReady = false;
        }
        if (isReady) {
            selector.isReadyToMove();
            isButtonPressed = true;
            showWhereIsPossibleToPutPiece();
        }
    }

    public void addRookPieceOnClick(ActionEvent actionEvent) {
        boolean isReady;
        switch (model.getNextPlayer()) {
            case PLAYER_1 -> {
                isReady = model.getPlayer1Pieces().contains(Piece.LIGHT_ROOK);
                thePiece = Piece.LIGHT_ROOK;
            }
            case PLAYER_2 -> {
                isReady = model.getPlayer2Pieces().contains(Piece.DARK_ROOK);
                thePiece = Piece.DARK_ROOK;
            }
            default -> isReady = false;
        }
        if (isReady) {
            selector.isReadyToMove();
            isButtonPressed = true;
            showWhereIsPossibleToPutPiece();
        }
    }
}
