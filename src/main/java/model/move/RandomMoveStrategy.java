package model.move;

import java.util.Random;

import gameIO.IGameModel;
import model.utility.Point;


public class RandomMoveStrategy implements INextMoveStrategy {

	@Override
	public Point getNextMove(IGameModel game, int player) {
		Random rand = new Random();
		int wBound = game.getBoardModel().getDimension().getWidth();
		int hBound = game.getBoardModel().getDimension().getHeight();
		Point p = new Point(rand.nextInt(wBound), rand.nextInt(hBound));
		return p;
	}

	
}
