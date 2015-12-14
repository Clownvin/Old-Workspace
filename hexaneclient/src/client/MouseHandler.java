package client;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

public final class MouseHandler extends Component implements
		MouseMotionListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5289912002535343959L;
	public final Client client;

	public MouseHandler(final Client client) {
		this.client = client;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		client.mouseX = e.getX();
		client.mouseY = e.getY();
		e.consume();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int clickX = e.getX() - 8;
		int clickY = e.getY() - 31;
		System.out.println("(" + clickX + ", " + clickY + ")");
		client.BUTTONS.handleClick(clickX, clickY);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		int new_mx = arg0.getX(), new_my = arg0.getY();
		if (SwingUtilities.isRightMouseButton(arg0)) {
			client.GRAPHICS_HANDLER.viewAngle.x -= new_mx - client.mouseX;
			client.GRAPHICS_HANDLER.viewAngle.y -= new_my - client.mouseY;
		} else {
			// client.GRAPHICS_HANDLER.azimuth -= new_mx - client.mouseX;
			// client.GRAPHICS_HANDLER.elevation += new_my - client.mouseY;
			client.user.playerX -= new_mx - client.mouseX;
			client.user.playerY -= new_my - client.mouseY;
		}
		client.mouseX = new_mx;
		client.mouseY = new_my;
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		int mouseX = arg0.getX() - 8;
		int mouseY = arg0.getY() - 31;
		client.BUTTONS.handleMouseover(mouseX, mouseY);
	}

}
