package a7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.List;
import java.util.ArrayList;

public class ConnectFourWidget extends JPanel implements ActionListener, SpotListener {

	private static final Color DEFAULT_BACKGROUND_LIGHT = new Color(0.8f, 0.8f, 0.8f);
	private static final Color DEFAULT_BACKGROUND_DARK = new Color(0.5f, 0.5f, 0.5f);

	private enum Player {
		RED, BLACK
	};

	private JSpotBoard board;
	private JLabel message;
	private boolean gameWon;
	private Player nextPlayer;
	private int moves;

	public ConnectFourWidget() {
		board = new JSpotBoard(7, 6);
		message = new JLabel();
		for (Spot s : board) {
			s.setBackground(s.getSpotX() % 2 == 0 ? DEFAULT_BACKGROUND_LIGHT : DEFAULT_BACKGROUND_DARK);
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
			s.unhighlightSpot();
		}

		gameWon = false;
		nextPlayer = Player.RED;
		moves = 0;
		message.setText("Welcome to Connect Four. Red to play.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		resetGame();
	}

	@Override
	public void spotClicked(Spot spot) {
		if (gameWon || !board.getSpotAt(spot.getSpotX(), 0).isEmpty()) {
			return;
		}

		String playerName = null;
		String nextPlayerName = null;
		Color playerColor = null;

		if (nextPlayer == Player.RED) {
			playerColor = Color.RED;
			playerName = "Red";
			nextPlayerName = "Black";
			nextPlayer = Player.BLACK;
		} else {
			playerColor = Color.BLACK;
			playerName = "Black";
			nextPlayerName = "Red";
			nextPlayer = Player.RED;
		}

		for (int i = 5; i >= 0; i--) {
			if (board.getSpotAt(spot.getSpotX(), i).isEmpty()) {
				board.getSpotAt(spot.getSpotX(), i).setSpotColor(playerColor);
				board.getSpotAt(spot.getSpotX(), i).setSpot();
				board.getSpotAt(spot.getSpotX(), i).unhighlightSpot();
				gameWon = checkWin(spot.getSpotX(), i);
				moves++;
				break;
			}
		}

		if (gameWon) {
			message.setText(playerName + " wins!");
			return;
		}
		if (moves == 42) {
			message.setText("Draw game.");
			return;
		}
		message.setText(nextPlayerName + " to play.");
	}

	private boolean checkWin(int x, int y) {

		// Check Column
		if (y <= 2) {
			for (int i = y + 1; i <= y + 3; i++) {
				if (!board.getSpotAt(x, i).getSpotColor().equals(board.getSpotAt(x, y).getSpotColor())) {
					break;
				}
				if (i == y + 3) {
					spotExited(board.getSpotAt(x, y));
					for (int j = y; j <= y + 3; j++) {
						board.getSpotAt(x, j).highlightSpot();
					}
					return true;
				}
			}
		}

		// Check Row
		List<Spot> validRowSpot = new ArrayList<Spot>();
		for (int i = x; i <= 6; i++) {
			if (board.getSpotAt(i, y).isEmpty()) {
				break;
			}
			if (!board.getSpotAt(i, y).getSpotColor().equals(board.getSpotAt(x, y).getSpotColor())) {
				break;
			}
			validRowSpot.add(board.getSpotAt(i, y));
		}

		for (int i = x - 1; i >= 0 && validRowSpot.size() != 4; i--) {
			if (board.getSpotAt(i, y).isEmpty()) {
				break;
			}
			if (!board.getSpotAt(i, y).getSpotColor().equals(board.getSpotAt(x, y).getSpotColor())) {
				break;
			}
			validRowSpot.add(board.getSpotAt(i, y));
		}

		if (validRowSpot.size() == 4) {
			spotExited(board.getSpotAt(x, y));
			for (Spot s : validRowSpot) {
				s.highlightSpot();
			}
			return true;
		}

		// Check Diagonal
		validRowSpot = new ArrayList<Spot>();
		for (int i = 0; x + i <= 6 && y + i <= 5; i++) {
			if (board.getSpotAt(x + i, y + i).isEmpty()) {
				break;
			}
			if (!board.getSpotAt(x + i, y + i).getSpotColor().equals(board.getSpotAt(x, y).getSpotColor())) {
				break;
			}
			validRowSpot.add(board.getSpotAt(x + i, y + i));
		}

		for (int i = -1; x + i >= 0 && y + i >= 0 && validRowSpot.size() != 4; i--) {
			if (board.getSpotAt(x + i, y + i).isEmpty()) {
				break;
			}
			if (!board.getSpotAt(x + i, y + i).getSpotColor().equals(board.getSpotAt(x, y).getSpotColor())) {
				break;
			}
			validRowSpot.add(board.getSpotAt(x + i, y + i));
		}

		if (validRowSpot.size() == 4) {
			spotExited(board.getSpotAt(x, y));
			for (Spot s : validRowSpot) {
				s.highlightSpot();
			}
			return true;
		}

		// Check AntiDiagonal
		validRowSpot = new ArrayList<Spot>();
		for (int i = 0; x + i <= 6 && y - i >= 0; i++) {
			if (board.getSpotAt(x + i, y - i).isEmpty()) {
				break;
			}
			if (!board.getSpotAt(x + i, y - i).getSpotColor().equals(board.getSpotAt(x, y).getSpotColor())) {
				break;
			}
			validRowSpot.add(board.getSpotAt(x + i, y - i));
		}

		for (int i = -1; x + i >= 0 && y - i <= 5 && validRowSpot.size() != 4; i--) {
			if (board.getSpotAt(x + i, y - i).isEmpty()) {
				break;
			}
			if (!board.getSpotAt(x + i, y - i).getSpotColor().equals(board.getSpotAt(x, y).getSpotColor())) {
				break;
			}
			validRowSpot.add(board.getSpotAt(x + i, y - i));
		}

		if (validRowSpot.size() == 4) {
			spotExited(board.getSpotAt(x, y));
			for (Spot s : validRowSpot) {
				s.highlightSpot();
			}
			return true;
		}

		return false;
	}

	@Override
	public void spotEntered(Spot spot) {
		if (gameWon) {
			return;
		}
		for (int i = 0; i < 6; i++) {
			if (board.getSpotAt(spot.getSpotX(), i).isEmpty()) {
				board.getSpotAt(spot.getSpotX(), i).highlightSpot();
			}
		}
	}

	@Override
	public void spotExited(Spot spot) {
		if (gameWon) {
			return;
		}
		for (int i = 0; i < 6; i++) {
			board.getSpotAt(spot.getSpotX(), i).unhighlightSpot();
		}
	}

}
