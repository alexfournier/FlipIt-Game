package model;

import java.util.Observable;

/**
 * Model the weird and whacky game called FlipIt!
 *
 * FlipIt! is a very simple game, loosely based on the game of Othello.
 * The objective of FlipIt! is to "flip" all tiles from a false (unhappy)
 * state to a true (happy) state.
 * But be aware: when you flip a tile, its immediate up, down, left,
 * and right neighbours are also flipped!
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @version 1.0
 */
public final class FlipItModel extends Observable implements FlipItConstants
                                                           , Gameable {

    // The 2-dimensional playing surface.
    private boolean[][] board;

    // The status of the game.
    private String      status;

    /**
     * FlipItModel no-argument constructor.
     * Create a default one-by-one board.
     */
    public FlipItModel() {
        this( DEFAULT_ROWS, DEFAULT_COLUMNS );
    }

    /**
     * Creates a FlipMeModel with rows-by-columns dimensions.
     *
     * @param int initial number of rows
     * @param int initial number of columns
     */
    public FlipItModel( int rows, int columns ) {
        super();

        this.setDimensions( rows, columns );

        // let's start the game...
        this.start();
    }

    /**
     * Flips the up, down, left, and right neighbors of board[row][column].
     * Safely ignores those cases where the cell at board[row][column]
     * does not have an immediate neighbor.
     * For example, the cell at board[0][0] does not have an up neighbor,
     * and does not have a left neighbor.
     * @param int row
     * @param int column
     */
    private void flipImmediateNeighborsOf( int row, int column ) {
        final int rightColumn = column + 1;
        final int downRow = row + 1;
        final int leftColumn = column - 1;
        final int upRow = row - 1;

        // up
        if ( upRow >= 0 )
            this.toggleValueAt( upRow, column );

        // down
        if ( downRow < this.getRows() )
            this.toggleValueAt( downRow, column );

        // left
        if ( leftColumn >= 0 )
            this.toggleValueAt( row, leftColumn );

        // right
        if ( rightColumn < this.getColumns() )
            this.toggleValueAt( row, rightColumn );
    }

    /**
     * Flips the value of the cell at board[row][column].
     * Also, the immediate up, down, left, and rights
     * neighbors must also be flipped.
	 * For example, if the value of the cell at board[row][column] is true,
     * then it will be flipped to false.
     * If the value is false, then the flipped value will be true.
     * @param int row
     * @param int column
     */
    public void flipValueAt( int row, int column ) {
        this.toggleValueAt( row, column );
        this.flipImmediateNeighborsOf( row, column );
        this.setStatus( "flipped " + (row + 1) + ", " + (column + 1) );
        this.updateObservers();
    }

    /**
     * Gets the number of columns for this game.
     * @return the integer number of columns
     */
    public int getColumns() {
        return ( board[0].length );
    }

    /**
     * Returns the number of tiles computed as: rows x columns
     * @return int the number of tiles
     */
    public int getNumberOfCells() {
        return this.getRows() * this.getColumns();
    }

    /**
     * Returns the number of tiles that have been flipped.
     * @return int the number of flipped cells
     */
    public int getNumberOfFlippedCells() {
        final int cols = this.getColumns();
        final int rows = this.getRows();

        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if ( this.getValueAt(r, c) )
                    count++;
            }
        }

        return count;
    }

    /**
     * Calculate the percent of completion.
     * @return int the percentage of completion
     */
    public int getProgress() {
        return ( (this.getNumberOfFlippedCells() * 100) / this.getNumberOfCells() );
    }

    /**
     * Gets the number of rows for this game.
     * @return the integer number of rows
     */
    public int getRows() {
        return ( board.length );
    }

    /**
     * Gets the status.
     * @return String the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the value of the cell at board[row][column].
     * @param int row
     * @param int column
     * @return boolean true if the cell at board[row][column]
     * is true; false otherwise.
     */
    public boolean getValueAt( int row, int column ) {
        return ( board[row][column] );
    }

    /**
     * Answers whether or not all of the cells of board
     * have been flipped to true.
     * @return boolean true if all the cells of board
     * have been flipped; false otherwise.
     */
    private boolean isFlipped() {
        final int cols = this.getRows();
        final int rows = this.getRows();

        // Visit each cell of board.
        // If any cell is false, return false.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if ( this.getValueAt(i, j) == false )
                    return false;
            }
        }

        // All of the cells have been flipped to true.
        return true;
    }

    /**
     * Answers whether or not the game is lost.
     * @return boolean false as a FlipMe! game is
     * never lost. Rather, the user quits in frustration.
     */
    @Override
    public boolean isLost() {
        return false;
    }

    /**
     * Answers whether or not the game is won.
     * @return boolean true if all the cells of baord
     * have been flipped; false otherwise.
     */
    @Override
    public boolean isWon() {
        // The game is won if all of the cells have been flipped.
        return this.isFlipped();
    }

    /**
     * Sets all of the cells of board to false.
     */
    @Override
    public void reset() {
        final int cols = this.getColumns();
        final int rows = this.getRows();

        // Visit each cell and set its value to false.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if ( this.getValueAt(i, j) == true ) {
                    this.toggleValueAt( i, j );
                }
            }
        }

        this.setStatus( this.toString() );
        this.updateObservers();
    }

    /**
     * Sets the number of rows and columns
     * and resets the game.
     * NOTE: no error checking is done on the parameters!
     * @param int number of rows
     * @param int number of columns
     */
    public void setDimensions( int rows, int columns ) {
        board = new boolean[rows][columns];

        this.reset();
    }

    /**
     * Sets the status.
     * @param status the new status
     */
    private void setStatus( String status ) {
        this.status = status;
    }

    /**
     * Starts the FlipIt! game.
     */
    @Override
    public void start() {
        this.reset();
    }

    /**
     * Toggles the value of the cell at board[row][column].
     * For example, if the value of the cell at board[row][column]
     * is true, then toggle its value to false.
     * If the value is false, then toggle its value to true.
     * @param int row
     * @param int column
     */
    private void toggleValueAt( int row, int column ) {
        if ( this.getValueAt(row, column) == true )
            board[row][column] = false;
        else
            board[row][column] = true;
    }

    /**
     * Returns a String that represents the value of this object.
     * @return a string representation of the receiver
     */
    @Override
    public String toString() {
        return ("FlipIt with dimensions " +
                this.getRows() +
                " by " +
                this.getColumns());
    }

    /**
     * The model has changed!
     * Broadcast changed state to all registered observers.
     */
    private void updateObservers() {
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Starts the application.
     * @param args an array of command-line arguments
     *
     * Demonstrates this model is independent of any view.
     */
    public static void main( String[] args ) {
        FlipItModel model = new FlipItModel();

        System.out.println( model );
        System.out.println( "Correct response: FlipItModel with dimensions 1 by 1" );
        System.out.println( "Have I won? " + model.isWon() );
        System.out.println( "Correct response: false" );
        System.out.println( "Flipping cell at row 0, column 0" );
        model.flipValueAt( 0, 0 );
        System.out.println( "Have I won? " + model.isWon() );
        System.out.println( "Correct response: true" );
        System.out.println();

        model = new FlipItModel( 1, 2 );
        System.out.println( model );
        System.out.println( "Correct response: FlipMeModel with dimensions 1 by 2" );
        System.out.println( "Flipping cell at row 0, column 0" );
        model.flipValueAt( 0, 0 );
        System.out.println( "Have I won? " + model.isWon() );
        System.out.println( "Correct response: true" );
        System.out.println();

        model = new FlipItModel( 1, 3 );
        System.out.println( model );
        System.out.println( "Correct response: FlipMeModel with dimensions 1 by 3" );
        System.out.println( "Flipping cell at row 0, column 0" );
        model.flipValueAt( 0, 0 );
        System.out.println( "Have I won? " + model.isWon() );
        System.out.println( "Correct response: false" );
        System.out.println( "Flipping cell at row 0, column 0 (again)" );
        model.flipValueAt( 0, 0 );
        System.out.println( "Flipping cell at row 0, column 1" );
        model.flipValueAt( 0, 1 );
        System.out.println( "Have I won? " + model.isWon() );
        System.out.println( "Correct response: true" );
        System.out.println();

        model = new FlipItModel( 3, 3 );
        System.out.println( model );
        System.out.println( "Correct response: FlipItModel with dimensions 3 by 3" );
        for( int i = 0; i < model.getRows(); i++ ) {
            for( int j = 0; j < model.getColumns(); j++ ) {
                System.out.println( "Flipping cell at row " + i + ", column " + j );
                model.flipValueAt( i, j );
            }
        }
        System.out.println( "There should be no run-time error messages." );
        System.out.println();
    }
}