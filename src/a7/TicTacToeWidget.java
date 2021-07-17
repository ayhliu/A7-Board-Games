package a7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TicTacToeWidget extends JPanel implements ActionListener, SpotListener {

	private static final Color DEFAULT_BACKGROUND_LIGHT = new Color(0.8f, 0.8f, 0.8f);

	private enum Player {
		WHITE, BLACK
	};

	private JSpotBoard board;
	private JLabel message;
	private boolean gameWon;
	private Player nextPlayer;
	private int moves;

	public TicTacToeWidget() {
		board = new JSpotBoard(3, 3);
		message = new JLabel();
		for (Spot s : board) {
			s.setBackground(DEFAULT_BACKGROUND_LIGHT);
		}

		setLayout(new BorderLayout());
		add(board, BorderLayout.CENTER);

		JPanel resetMessagePanel = new JPanel();
		resetMessagePanel.setLayout(new BorderLayout());

		JButton resetButton = new JButton("Restart");
		resetButton.addActionListener(this);
		resetMessagePanel.add(resetButton, BorderLayout.EAST);
		resetMessagePanel.add(message, BorderLayout.CENTER);

		add(resetMessagePanel, BorderLayout.SOUTH);
		board.addSpotListener(this);

		resetGame();
	}

	private void resetGame() {
		for (Spot s : board) {
			s.clearSpot();
		}

		gameWon = false;
		nextPlayer = Player.WHITE;
		moves = 0;
		message.setText("Welcome to Tic Tac Toe. White to play.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		resetGame();
	}

	@Override
	public void spotClicked(Spot spot) {
		if (gameWon || !spot.isEmpty()) {
			return;
		}

		String playerName = null;
		String nextPlayerName = null;
		Color playerColor = null;

		if (nextPlayer == Player.WHITE) {
			playerColor = Color.WHITE;
			playerName = "White";
			nextPlayerName = "Black";
			nextPlayer = Player.BLACK;
		} else {
			playerColor = Color.BLACK;
			playerName = "Black";
			nextPlayerName = "White";
			nextPlayer = Player.WHITE;
		}

		spot.setSpotColor(playerColor);
		spot.setSpot();
		moves++;
		spot.unhighlightSpot();

		gameWon = checkWin(spot.getSpotX(), spot.getSpotY());

		if (gameWon) {
			message.setText(playerName + " wins!");
			return;
		}
		if (moves == 9) {
			message.setText("Draw game.");
			return;
		}
		message.setText(nextPlayerName + " to play.");
	}

	private boolean checkWin(int x, int y) {

		// Check Row
		for (int i = 0; i < 3; i++) {
			if (board.getSpotAt(i, y).isEmpty()) {
				break;
			}
			if (!board.getSpotAt(i, y).getSpotColor().equals(board.getSpotAt(x, y).getSpotColor())) {
				break;
			}
			if (i == 2) {
				return true;
			}
		}

		// Check Column
		for (int i = 0; i < 3; i++) {
			if (board.getSpotAt(x, i).isEmpty()) {
				break;
			}
			if (!board.getSpotAt(x, i).getSpotColor().equals(board.getSpotAt(x, y).getSpotColor())) {
				break;
			}
			if (i == 2) {
				return true;
			}
		}

		// Check Diagonal
		if (x == y) {
			for (int i = 0; i < 3; i++) {
				if (board.getSpotAt(i, i).isEmpty()) {
					break;
				}
				if (!board.getSpotAt(i, i).getSpotColor().equals(board.getSpotAt(x, y).getSpotColor())) {
					break;
				}
				if (i == 2) {
					return true;
				}
			}
		}

		// Check AntiDiagonal
		if (x + y == 2) {
			for (int i = 0; i < 3; i++) {
				if (board.getSpotAt(i, 2 - i).isEmpty()) {
					break;
				}
				if (!board.getSpotAt(i, 2 - i).getSpotColor().equals(board.getSpotAt(x, y).getSpotColor())) {
					break;
				}
				if (i == 2) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void spotEntered(Spot spot) {
		if (gameWon || !spot.isEmpty()) {
			return;
		}
		spot.highlightSpot();
	}

	@Override
	public void spotExited(Spot spot) {
		spot.unhighlightSpot();
	}

}
