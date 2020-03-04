package model.board;

import model.move.IBoardStatusVisitor;

//Referenced classes of package model.board:
//ATerminalState

public class DrawState extends ATerminalState{

	public static DrawState Singleton = new DrawState();

	private DrawState(){
	}

	@Override
	public Object execute(IBoardStatusVisitor iboardstatusvisitor, Object obj, IBoardModel iboardmodel){
		return iboardstatusvisitor.drawCase(iboardmodel, obj);
	}

}
