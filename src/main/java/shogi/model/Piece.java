package shogi.model;

/**
 * Representation of a shogi piece.
 */
public enum Piece {

    /**
     * The king for the lower ranked player or challenger, also known as gyokushō which means jeweled general.
     */
    LIGHT_KING,

    /**
     * The king for the higher ranked player or challenger, also known as ōshō which means king general.
     */
    DARK_KING,

    /**
     * The rook of the starter player, also known as hisha which means flying chariot.
     */
    LIGHT_ROOK,

    /**
     * The rook of the second player, also known as hisha which means flying chariot.
     */
    DARK_ROOK,

    /**
     * The promoted rook "dragon" of the starter player, also known as ryūō which means dragon king.
     */
    LIGHT_PROMOTED_ROOK,

    /**
     * The promoted rook "dragon" of the second player, also known as ryūō which means dragon king.
     */
    DARK_PROMOTED_ROOK,

    /**
     * The bishop of the starter player, also known as kakugyō which means angle mover.
     */
    LIGHT_BISHOP,

    /**
     * The bishop of the second player, also known as kakugyō which means angle mover.
     */
    DARK_BISHOP,

    /**
     * The promoted bishop "horse" of the starter player, also known as ryūma or ryūme which means dragon horse.
     */
    LIGHT_PROMOTED_BISHOP,

    /**
     * The promoted bishop "horse" of the second player, also known as ryūma or ryūme which means dragon horse.
     */
    DARK_PROMOTED_BISHOP,

    /**
     * The gold general "gold" of the starter player, also known as kinshō which means gold general.
     */
    LIGHT_GOLD,

    /**
     * The gold general "gold" of the second player, also known as kinshō which means gold general.
     */
    DARK_GOLD,

    /**
     * The silver general "silver" of the starter player, also known as ginshō which means silver general.
     */
    LIGHT_SILVER,

    /**
     * The silver general "silver" of the second player, also known as ginshō which means silver general.
     */
    DARK_SILVER,

    /**
     * The promoted silver of the starter player, also known as narigin which means promoted silver.
     */
    LIGHT_PROMOTED_SILVER,

    /**
     * The promoted silver of the second player, also known as narigin which means promoted silver.
     */
    DARK_PROMOTED_SILVER,

    /**
     * The knight of the starter player, also known as keima which means katsura horse.
     */
    LIGHT_KNIGHT,

    /**
     * The knight of the second player, also known as keima which means katsura horse.
     */
    DARK_KNIGHT,

    /**
     * The promoted knight of the starter player, also known as narikei which means promoted katsura.
     */
    LIGHT_PROMOTED_KNIGHT,

    /**
     * The promoted knight of the second player, also known as narikei which means promoted katsura.
     */
    DARK_PROMOTED_KNIGHT,

    /**
     * The lance of the starter player, also known as kyōsha which means incense chariot.
     */
    LIGHT_LANCE,

    /**
     * The lance of the second player, also known as kyōsha which means incense chariot.
     */
    DARK_LANCE,

    /**
     * The promoted lance of the starter player, also known as narikyō which means promoted incense.
     */
    LIGHT_PROMOTED_LANCE,

    /**
     * The promoted lance of the second player, also known as narikyō which means promoted incense.
     */
    DARK_PROMOTED_LANCE,

    /**
     * The pawn of the starter player, also known as fuhyō which means foot soldier.
     */
    LIGHT_PAWN,

    /**
     * The pawn of the second player, also known as fuhyō which means foot soldier.
     */
    DARK_PAWN,

    /**
     * The promoted pawn "tokin" of the starter player, also known as tokin which means reaches gold.
     */
    LIGHT_PROMOTED_PAWN,

    /**
     * The promoted pawn "tokin" of the second player, also known as tokin which means reaches gold.
     */
    DARK_PROMOTED_PAWN,

    /**
     * The empty space which is not on the board.
     */
    NONE,

    /**
     * The empty space which is on the board.
     */
    EMPTY
}
