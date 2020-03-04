package model.board;

import model.move.IBoardLambada;
import model.move.IBoardStatusVisitor;

public interface IBoardState {

	public abstract void map(int i, IBoardLambada iboardlambda, Object obj, IBoardModel iboardmodel);
	public abstract Object execute(IBoardStatusVisitor iboardstatusvisitor, Object obj, IBoardModel iboardmodel);
}
