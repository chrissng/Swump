package core.solver;

import java.util.ArrayList;


import JaCoP.constraints.Alldistinct;
import JaCoP.core.Store;
import JaCoP.core.Variable;
import JaCoP.search.DepthFirstSearch;
import JaCoP.search.IndomainMin;
import JaCoP.search.IndomainRandom;
import JaCoP.search.Search;
import JaCoP.search.SelectChoicePoint;
import JaCoP.search.SimpleSelect;
import JaCoP.search.SmallestDomain;


/** @author Kangwei
 *
 */
public class Solver {
    
    public Variable[][] elements;
    private ArrayList<Variable> vars = new ArrayList<Variable>();
    private Store store = new Store();
    public Search label;
    
    public void model(int[][] puzzle) {
        int numRow = 3;
        int numCol = 3;

        store = new Store();
        vars = new ArrayList<Variable>();
        
        elements = new Variable[9][9];

        /** Creating variables and assigning constraints to them */
        for (int i = 0; i < numRow * numCol; i++)
            for (int j = 0; j < numRow * numCol; j++)
                if (puzzle[i][j] == 0) {
                    elements[i][j] = new Variable(store, "CELL(" + i +", " +j+ ")", 1, 9);
                    vars.add(elements[i][j]);
                }
                else
                    elements[i][j] = new Variable(store, "CELL(" + i +", " +j+ ")" + i + j,
                            puzzle[i][j], puzzle[i][j]);
        
        /** Creating and imposing constraints to boxes */
        for (int row = 0; row < numRow; row++)
            for (int col = 0; col < numCol; col++) {

                ArrayList<Variable> block = new ArrayList<Variable>();
                for (int k = 0; k < numCol; k++)
                    for (int m = 0; m < numRow; m++)
                        block.add(elements[row * numCol + k][col * numRow + m]);

                store.impose(new Alldistinct(block));

            }

        /** Creating a imposing constraints to rows */
        for (int row = 0; row < numRow * numCol; row++)
            store.impose(new Alldistinct(elements[row]));

        /** Creating and imposing constraints to columns */
        for (int row = 0; row < numRow * numCol; row++) {
            Variable[] column = new Variable[numRow * numCol];
            for (int col = 0; col < numRow * numCol; col++)
                column[col] = elements[col][row];

            store.impose(new Alldistinct(column));
        }
    }

    
    public static void printMatrix(Variable[][] matrix, int rows, int cols) {

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j].value() + " ");
            }
            System.out.println();
        }

    }
    
    /** Search method that searches for a single solution */
    public boolean searchSmallestDomain() {
        
        SelectChoicePoint select = new SimpleSelect(vars.toArray(new Variable[1]), new SmallestDomain(),
                new IndomainMin());

        label = new DepthFirstSearch();
        label.setPrintInfo(false);
        boolean result = label.labeling(store, select);
        
        return result;
        
    }   
    
    /** Search method that searches for all possible solutions */
    public boolean searchAll() {
        
        SelectChoicePoint select = new SimpleSelect(vars.toArray(new Variable[1]), new SmallestDomain(),
                new IndomainRandom());

        label = new DepthFirstSearch();
        label.setPrintInfo(false);
        label.getSolutionListener().searchAll(true);

        boolean result = label.labeling(store, select);
        return result;
        
    }   
    
    public static void print2DArray(int[][] base){
        for(int i=0; i<base.length; i++){
            for(int j=0; j<base[i].length; j++){
                System.out.print(base[i][j]+" ");
            }
            System.out.println();
        }
    }
 
}
