package a7;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TicTacToeGame {
	public static void main(String[] args) {
		JFrame mainFrame = new JFrame();
		mainFrame.setTitle("Tic Tac Toe");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		mainFrame.setContentPane(topPanel);

		topPanel.add(new TicTacToeWidget(), BorderLayout.CENTER);

		mainFrame.pack();
		mainFrame.setVisible(true);
	}
}
