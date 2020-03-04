package model.move;

import model.board.IBoardModel;

public interface IBoardLambada {
	
	public abstract boolean apply(int i, IBoardModel iboardmodel, Object obj, int j, int k, int l);
	public abstract void noApply(int i, IBoardModel iboardmodel, Object obj);
}
