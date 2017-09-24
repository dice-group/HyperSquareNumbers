package org.dice.hsn.d2;

import java.util.Arrays;
import java.util.BitSet;

/**
 * An empirical approach to find a solution for printing all possible 2x2 pixel
 * sized black/white fields in a smaller area. encoding of 2x2 pixel fields:
 * left upper corner 8 (2^3) bit, left lower corner 4 (2^2) bit, right upper
 * corner 2 (2^1) bit and right lower corner 1 (2^0).
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 *
 */
public class BruteForce {

    protected static final int LEFT_UPPER_CORNER = 8;
    protected static final int LEFT_LOWER_CORNER = 4;
    protected static final int RIGHT_UPPER_CORNER = 2;
    protected static final int RIGHT_LOWER_CORNER = 1;

    public static void main(String[] args) {
        BruteForce searcher = new BruteForce();
        searcher.searchSolution(new int[4][4]);
    }
    
    private int solutionsCount = 0;

    /**
     * Searches recursively all solutions that could fill the given rectangle
     * using the 16 2x2 fields via brute force.
     * 
     * @param rectangle The rectangle that should be filled with the numbers
     */
    public void searchSolution(int rectangle[][]) {
        BitSet usedNumbers = new BitSet(16);
        recursiveSearch(rectangle, usedNumbers, 0);
        System.out.println("Found " + solutionsCount + " solutions");
    }

    /**
     * The recursive search.
     * 
     * @param rectangle rectangle that should be filled
     * @param usedNumbers numbers already added to the rectangle for the current solution
     * @param nextFreeCell the cell that should be filled by the current method call
     */
    private void recursiveSearch(int[][] rectangle, BitSet usedNumbers, int nextFreeCell) {
        // If we have found a solution
        if (nextFreeCell == 16) {
            System.out.print("Solution found:");
            for (int i = 0; i < rectangle.length; ++i) {
                System.out.print(Arrays.toString(rectangle[i]));
            }
            System.out.println();
            ++solutionsCount;
            return;
        }
        int row = nextFreeCell / rectangle[0].length;
        int column = nextFreeCell % rectangle[0].length;
        for (int i = 0; i < 16; ++i) {
            if (!usedNumbers.get(i)) {
                // If the number fits
                if (((row > 0) ? checkTopBottomOverlap(rectangle[row - 1][column], i) : true)
                        && ((column > 0) ? checkLeftRightOverlap(rectangle[row][column - 1], i) : true)) {
                    rectangle[row][column] = i;
                    usedNumbers.set(i);
                    recursiveSearch(rectangle, usedNumbers, nextFreeCell + 1);
                    usedNumbers.clear(i);
                }
            }
        }
    }

    public boolean checkTopBottomOverlap(int top, int bottom) {
        return (((top & LEFT_LOWER_CORNER) > 0) == ((bottom & LEFT_UPPER_CORNER) > 0))
                && (((top & RIGHT_LOWER_CORNER) > 0) == ((bottom & RIGHT_UPPER_CORNER) > 0));
    }

    public boolean checkLeftRightOverlap(int left, int right) {
        return (((left & RIGHT_UPPER_CORNER) > 0) == ((right & LEFT_UPPER_CORNER) > 0))
                && (((left & RIGHT_LOWER_CORNER) > 0) == ((right & LEFT_LOWER_CORNER) > 0));
    }
}
