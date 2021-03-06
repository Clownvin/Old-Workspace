/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ctp.irc;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ctp.packets.IRCPacketHandler;
import com.dew.packets.Packet;
import com.dew.util.BinaryOperations;

/**
 *
 * @author calvin
 */
public class IRCClient extends javax.swing.JFrame {
   
    private final IRCPacketHandler packetHandler = new IRCPacketHandler();
    private final Thread packetGrabber = new Thread(new Runnable() {

	@Override
	public void run() {
		boolean streamAlive = true;
		while (streamAlive) {
			while (!connected) {
				try {
					socket = new Socket("localhost", 2224);
					setOutputStream(socket.getOutputStream());
					ois = socket.getInputStream();
					connected = true;
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
				}
			}
			try {
				byte[] buffer = new byte[4];
				for (int i = 0; i < 4; i++) {
					buffer[i] = (byte) ois.read();
				}
				int length = BinaryOperations.bytesToInteger(buffer);
				if (length > 0) {
					System.out.println("Packet Recieved");
					buffer = new byte[length];
					int amount = ois.read(buffer);
					Packet newPacket = (Packet) Packet.buildPacket(buffer, amount);
					packetHandler.handlePacket(newPacket);
				} else {
					if (ois.available() > 0) {
						System.out.println("Trying something different");
						buffer = new byte[ois.available()];
						int amount = ois.read(buffer);
						Packet newPacket = (Packet) Packet.buildPacket(buffer, amount);
						packetHandler.handlePacket(newPacket);
					}
					System.out.println("Buffer length = "+length);
					System.out.println("Available = "+ois.available());
				}
			} catch (IOException e) {
				connected = false;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
				streamAlive = false;
			}
		}
	}

});
    /**
     * Creates new form Frame
     */
    public IRCClient() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        mainPanels = new javax.swing.JTabbedPane();
        pane1 = new javax.swing.JPanel();
        pane2 = new javax.swing.JPanel();
        currentChannel = new javax.swing.JLabel();
        channelLabel = new javax.swing.JLabel();
        channelPermissions = new javax.swing.JLabel();
        permissionsLabel = new javax.swing.JLabel();
        channelMembers = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout pane1Layout = new javax.swing.GroupLayout(pane1);
        pane1.setLayout(pane1Layout);
        pane1Layout.setHorizontalGroup(
            pane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 807, Short.MAX_VALUE)
        );
        pane1Layout.setVerticalGroup(
            pane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 392, Short.MAX_VALUE)
        );

        mainPanels.addTab("tab2", pane1);

        currentChannel.setText("Current Channel:");

        channelLabel.setText("None");

        channelPermissions.setText("Channel Permissions:");

        permissionsLabel.setText("None");

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        channelMembers.setViewportView(jList1);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jTextField1.setText("jTextField1");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pane2Layout = new javax.swing.GroupLayout(pane2);
        pane2.setLayout(pane2Layout);
        pane2Layout.setHorizontalGroup(
            pane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pane2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(channelMembers, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pane2Layout.createSequentialGroup()
                        .addGroup(pane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(channelPermissions)
                            .addComponent(currentChannel))
                        .addGap(18, 18, 18)
                        .addGroup(pane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(channelLabel)
                            .addComponent(permissionsLabel))))
                .addGap(24, 24, 24)
                .addGroup(pane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
                    .addComponent(jTextField1))
                .addContainerGap())
        );
        pane2Layout.setVerticalGroup(
            pane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pane2Layout.createSequentialGroup()
                .addGroup(pane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pane2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pane2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(currentChannel)
                            .addComponent(channelLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(channelPermissions)
                            .addComponent(permissionsLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(channelMembers, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)))
                .addContainerGap())
        );

        mainPanels.addTab("tab1", pane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanels)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanels)
        );

        pack();
    }// </editor-fold>                        

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IRCClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IRCClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IRCClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IRCClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IRCClient().setVisible(true);
                try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                  //  UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(IRCClient.class.getName()).log(Level.SEVERE, null, ex);
        }
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JLabel channelLabel;
    private javax.swing.JScrollPane channelMembers;
    private javax.swing.JLabel channelPermissions;
    private javax.swing.JLabel currentChannel;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTabbedPane mainPanels;
    private javax.swing.JPanel pane1;
    private javax.swing.JPanel pane2;
    private javax.swing.JLabel permissionsLabel;
    // End of variables declaration                   
}
