import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The MainUI serves the main window that holds all of the user interface.
 * 
 * @author z5115782
 */
public class MainUI extends JFrame {
	private static final long serialVersionUID = -5285564050945629510L;
	private int width = 900;
	private int height = 600;
	private GameUI game;

	public MainUI() {
		super();
		this.initUI();
	}

	private void initUI() {
		this.setTitle("Warehouse Game");
		/*
		 * The window has a set size for now. We might need to implement
		 * resizability later.
		 */
		this.setSize(this.width, this.height);

		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		Container contPanel = this.getContentPane();
		contPanel.setLayout(new CardLayout());

		JPanel menuCont = new JPanel(new GridBagLayout());
		menuCont.add(new MenuUI(this));

		contPanel.add(menuCont, "menu");

		this.game = new GameUI(this);
		contPanel.add(this.game, "game");

		this.changeInterface(PanelName.GAME_MENU);
	}

	public void changeInterface(PanelName pN) {
		Container contPanel = this.getContentPane();
		CardLayout c = (CardLayout) (contPanel.getLayout());

		switch (pN) {
		case MAIN_MENU:
			c.show(contPanel, "menu");
			break;
		case GAME_MENU:
			c.show(contPanel, "game");
			break;
		}
	}

	public enum PanelName {
		MAIN_MENU, GAME_MENU
	}
}