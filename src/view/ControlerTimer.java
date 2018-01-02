package view;

import dao.MobsDAO;
import javafx.animation.AnimationTimer;

public class ControlerTimer extends AnimationTimer {

	@Override
	public void handle(long arg0) {
		refreshGameObjects();
		//System.out.println("refresh");
	}
	private void refreshGameObjects() {
		MobsDAO.getInstance().refresh();
	}
}
