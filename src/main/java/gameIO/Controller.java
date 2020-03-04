package gameIO;

import java.util.List;
import model.GameModel;


public class Controller {
	
	public static void main(String []args) {
		
		int nRows = 3, nCols = 3;
		
		IModelCombo m = new GameModel(nRows, nCols);
		TicTacToeView v = new TicTacToeView(nRows, nCols, m);
		
		
		m.setCommand(v.getCommand());
		m.setViewManager(v.getViewManager(), v.getTurnManager());
		
		List<Object> players = m.getPlayers();
		v.setPlayers(players);		
	}
}
