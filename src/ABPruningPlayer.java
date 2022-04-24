/**
 * Minimax Player
 * 
 * @author EricLingyiWang
 *
 */
public class ABPruningPlayer implements Player
{
    private int id;
    private int opponent_id;
    private int cols;
    private static java.util.Random rand = new java.util.Random();

    @Override
    public String name() {
        return "Loki";
    }

    @Override
    public void init(int id, int msecPerMove, int rows, int cols) {
    	this.id = id;
    	opponent_id = 3-id;
    	this.cols=cols;
    }
    
    @Override
    public void calcMove(Connect4Board board, int oppMoveCol, Arbitrator arb) 
        throws TimeUpException {

        if (board.isFull()) {
            throw new Error ("Complaint: The board is full!");
        }
        
       int[] scores = new int [cols];
       int random= rand.nextInt(board.numCols());
       int maxCol=random;
              
       int maxDepth=1;
       int alpha = -1000;
       int beta = 1000;
       while(!arb.isTimeUp() && maxDepth <= board.numEmptyCells()) {
        for (int i=0;i<cols;i++) {
        	if (board.isValidMove(i)) {
	        	board.move(i, id);
	        	scores[i] = ABpruning(board, maxDepth-1, alpha, beta, false, arb);
	        	board.unmove(i, id);
        	}
        	else scores[i]=-1000;
        }
       
        for (int i=0; i<scores.length; i++) {
        	if(scores[maxCol]<scores[i]) {
        		maxCol = i;
        	}
        }
        maxDepth++;
        arb.setMove(maxCol);
       }
    }
    
    public int ABpruning(Connect4Board board, int depth, int alpha, int beta, boolean isMaximizing, Arbitrator arb){
    	//depth=0 or terminal node
    	if (depth ==0 || board.isFull() ||arb.isTimeUp()) {
    		return CalcScore(board,id) - CalcScore(board,opponent_id);
    	}
    	
    	if (isMaximizing==true) {
    		int bestScore=-1000;
    		for (int i=0;i<cols;i++) {
            	if (board.isValidMove(i)) {
            		bestScore = Math.max(bestScore, ABpruning(board, depth-1, alpha, beta, false, arb));
            		alpha = Math.max(alpha, bestScore); 
            		if (alpha >= beta) break;
            	}
    		}
    		return bestScore;
    	}
    	else {
    		int bestScore=1000;
    		for (int i=0;i<cols;i++) {
            	if (board.isValidMove(i)) {
            		bestScore = Math.min(bestScore, ABpruning(board, depth -1, alpha, beta, true, arb));
            		beta = Math.min(beta, bestScore); 
            		if (alpha >= beta) break;
            	}
    		}
    		return bestScore;
    	}
    }
    
    //gives results of hypothetical score
    public int CalcScore(Connect4Board board, int id) {
    	int score=0;
    	
    	for(int r = 0; r<board.numRows(); r++) {
    		for (int c = 0; c<=board.numCols()-4; c++) {
    			if (board.get(r, c + 0) != id) continue;
    			if (board.get(r, c + 1) != id) continue;
    			if (board.get(r, c + 2) != id) continue;
    			if (board.get(r, c + 3) != id) continue;
    			score++;
    		}
    	}
    	
    	for (int c = 0; c<=board.numCols()-4; c++) {
    		for(int r = 0; r<=board.numRows()-4; r++) {
    			if (board.get(r + 0, c + 0) != id) continue;
    			if (board.get(r + 1, c + 1) != id) continue;
    			if (board.get(r + 2, c + 2) != id) continue;
    			if (board.get(r + 3, c + 3) != id) continue;
    			score++;
    		}
    	}
    	
    	for (int c = 0; c<board.numCols(); c++) {
    		for(int r = 0; r<=board.numRows()-4; r++) {
    			if (board.get(r + 0, c) != id) continue;
    			if (board.get(r + 1, c) != id) continue;
    			if (board.get(r + 2, c) != id) continue;
    			if (board.get(r + 3, c) != id) continue;
    			score++;
    		}
    	}
    	
    	for (int c = 0; c<=board.numCols()-4; c++) {
    		for(int r = board.numRows()-1; r>=4-1; r--) {
    			if (board.get(r - 0, c + 0) != id) continue;
    			if (board.get(r - 1, c + 1) != id) continue;
    			if (board.get(r - 2, c + 2) != id) continue;
    			if (board.get(r - 3, c + 3) != id) continue;
    			score++;
    		}
    	}
    	return score;
    }
}
