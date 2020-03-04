package gameIO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;
import javax.swing.border.Border;
import model.modelToView.ICommand;
import model.modelToView.IRejectCommand;
import model.modelToView.IViewManager;
import model.utility.Dimension;
import model.viewToModel.IModelManager;
import model.viewToModel.ITurnManager;
import model.viewToModel.IViewRequestor;

/**
 * View for TicTacToeGame
 */
public final class TicTacToeView extends JFrame implements IView {
	
	JButton startButton, resetButton;
	JButton [][]buttons;
	JRadioButton []playerButtons;
	JLabel messageLabel;
	
	IModelManager modelManager;
	IViewRequestor viewRequestor;
	List<Object> players;
	Object []playerSelections;
	
	JPanel board, third, playerSelectionPanel;
	Font font;
	AtomicBoolean finished; 
	String winner;
	
	final int SQUARE_SIZE = 170, EXTRA_SPACE = 100;

	
	public TicTacToeView(int nRows, int nCols, IModelManager modelManager) {
		
		this.finished = new AtomicBoolean(false);
		this.modelManager = modelManager;
		font = new Font("monospaced", Font.PLAIN, 40);
		
		
		// status message
		messageLabel = new JLabel("TicTacToe");
		messageLabel.setFont(font);
		
		font = new Font("monospaced", Font.PLAIN, 20);
		
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.add(messageLabel, BorderLayout.CENTER);
		
		// buttons
		startButton = new JButton("Start");
		startButton.setFont(font);
		
		resetButton = new JButton("Reset");
		resetButton.setFont(font);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(startButton);
		buttonPanel.add(resetButton);
		
		// Create the buttons for player selection
		playerSelectionPanel = new JPanel();
		playerSelectionPanel.setLayout(new GridLayout(1, 2));
		
		// create options panel
		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
		
		bottom.add(playerSelectionPanel);
		bottom.add(buttonPanel);
		
		
		// create and draw game board panel
		board = new JPanel(); 
		drawBoard(nRows, nCols);
		
				
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
		this.setTitle("Tic Tac Toe"); 
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(top, BorderLayout.NORTH); 
		this.getContentPane().add(board, BorderLayout.CENTER); 
		this.getContentPane().add(bottom, BorderLayout.SOUTH); 
		this.setSize(nCols*SQUARE_SIZE + EXTRA_SPACE, nRows*SQUARE_SIZE + EXTRA_SPACE);
		board.setSize(nCols*SQUARE_SIZE, nRows*SQUARE_SIZE);
		this.setLocation(900,300); 
				
		Thread shows = new Thread() { 
			public void run() { 
				setVisible(true);
			}
		};
		EventQueue.invokeLater(shows);

		// action listeners
		
		// enable all of the board buttons and disable this button
		// then, give the players to the model (starts game)
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				for(int i = 0; i < buttons.length; i++) {
					for(int j = 0; j < buttons[0].length; j++) {
						buttons[i][j].setEnabled(true);
					}
				}
				startButton.setEnabled(false);
				
				(new Thread() {
					@Override
					public void run() {
						IModelManager manager = getModelManager();
						if(manager != null) {
							manager.setPlayers(playerSelections[0], playerSelections[1]);
						}
					}
				}).start();
			}
		});
		
		// reset model and view
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getModelManager().reset();
				getViewManager().reset();
			}
		});
		
	}
	
	/**
	 * Draw the grid according to the number of rows and columns
	 */
	void drawBoard(int nRows, int nCols) {
		board.removeAll();
		board.setLayout(new GridLayout(nRows, nCols)); 
		Font f = new Font("monospaced", Font.PLAIN,128);
		
		buttons = new JButton[nRows][nCols];
		for(int i = 0; i < nRows; i++) {
			for(int j = 0; j < nCols; j++) {
				buttons[i][j] = new JButton("");
				buttons[i][j].setFont(f);
				buttons[i][j].setActionCommand(i + " " + j);
				
				// try to set X or O on pressing a button
				buttons[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String []dimen = e.getActionCommand().split(" ");
						final int i = Integer.parseInt(dimen[0]), j = Integer.parseInt(dimen[1]);
						(new Thread() {
							@Override
							public void run() {
								viewRequestor.setTokenAt(i, j, new IRejectCommand() {
									public void execute() {
										setMessage("Invalid move");
									}

								});
							}
						}).start();
					}
				});
				buttons[i][j].setEnabled(false);
				board.add(buttons[i][j]);
			}
		}
		startButton.setEnabled(true);
		
		this.setSize(nCols*SQUARE_SIZE + EXTRA_SPACE, nRows*SQUARE_SIZE + EXTRA_SPACE);
		board.setSize(nCols*SQUARE_SIZE, nRows*SQUARE_SIZE);
		board.updateUI();
	}
	
	void initPlayerButtons() {
		int numPlayers = 2;
		playerSelectionPanel.setLayout(new GridLayout(1, numPlayers));
		JPanel []playerPanels = new JPanel[numPlayers];
		if(players != null && players.size() > 0) {
			playerSelections = new Object[numPlayers];
			for(int player = 0; player < numPlayers; player++) {
				playerPanels[player] = new JPanel();
				playerPanels[player].setLayout(new BoxLayout(playerPanels[player], BoxLayout.Y_AXIS));
				playerSelectionPanel.add(playerPanels[player]);
				
				JLabel heading = new JLabel("Player " + player);
				heading.setFont(font);
				playerPanels[player].add(heading);
				
				ButtonGroup g = new ButtonGroup(); 
				JRadioButton []options = new JRadioButton[players.size()];
				for(int i = 0; i < players.size(); i++) {
					options[i] = new JRadioButton();
					options[i].setText(players.get(i).toString());
					options[i].setFont(font);
					playerPanels[player].add(options[i]);
					g.add(options[i]);
					options[i].setActionCommand(player + " " + i);
					options[i].addActionListener(new ActionListener() { 
					
						@Override
						public void actionPerformed(ActionEvent e) { 
							String []command = ((JRadioButton) e.getSource()).getActionCommand().split(" ");
							int player = Integer.parseInt(command[0]), selection = Integer.parseInt(command[1]);
							playerSelections[player] = players.get(selection);
						}
					});
				}
				options[player].setSelected(true);
				playerSelections[player] = players.get(player);
			}
			startButton.setEnabled(true);
			playerSelectionPanel.updateUI();
		}
        
		
		
	}
	
	public void setMessage(String s) {
		messageLabel.setText(s);
	}

	IModelManager getModelManager() {
		return modelManager;
	}
	
	public ICommand getCommand() {
		return new ICommand() {
			public void setTokenAt(int row, int col, int player) {
				String c = player == 0 ? "X" : "O";
				buttons[row][col].setText(c);
				
				finished.set(true);
			}

			public void clearTokenAt(int row, int col) {
				buttons[row][col].setText("");
			}

			public void setMessage(String s) {
				messageLabel.setText(s);
			}
		
		};
	}

	public void setiViewRequestor(IViewRequestor iViewRequestor) {
		viewRequestor = iViewRequestor;
	}

	public void setPlayers(List<Object> players) {
		this.players = players;
		initPlayerButtons();
	}

	public void setDimension(Dimension size) {
		drawBoard(size.getHeight(), size.getWidth());
	}
	
	
	// interface to tell this view that it's a human's turn
	public ITurnManager getTurnManager() {
		
		return new ITurnManager() {
			public void takeTurn(IViewRequestor requestor) {
				setMessage("Human player's turn");
				setiViewRequestor(requestor);
				finished.set(false);
				while(!finished.get())
					try {
						Thread.sleep(50);
					} catch(Exception e) {}
			}
		};
		
	}
	
	// interface to manage this view
	public IViewManager getViewManager() {
		
		return new IViewManager() {
			public void draw() {
				setMessage("Tie");
				disableButtons();
			}

			public void win(int player) {
				String message = (player == 0) ? "X wins" : "O wins";
				setMessage(message);
				disableButtons();
			}

			public void reset() {
				drawBoard(buttons.length, buttons[0].length);
				setMessage("TicTacToe");
			}
			
		};
	}
	
	void disableButtons() {
		for(int i = 0; i < buttons.length; i++) {
			for(int j = 0; j < buttons[i].length; j++) {
				buttons[i][j].setEnabled(false);
			}
		}
	}
	
}
