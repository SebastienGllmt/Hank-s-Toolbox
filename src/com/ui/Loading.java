package com.ui;

import java.awt.BorderLayout;
import java.io.File;

import com.LoonylandConstant;

import javax.swing.*;

@SuppressWarnings("serial")
public class Loading extends JFrame {
	
	public Loading(String title, int launch) {
		final JFrame frame = new JFrame("Loading...");
		frame.setSize(256, 88);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		ImageIcon loadWaitImage = new ImageIcon(LoonylandConstant.TOOLDIR + "HT_loading.png"); //Loading picture
		JLabel loading = new JLabel(loadWaitImage);
		loading.setSize(256,64);
		
	    getContentPane().add(loading);
	    frame.setContentPane(getContentPane());
	    
	    frame.setVisible(true);
		
		final int Case = launch;
		
		Runnable runnable = new Runnable() {
	        public void run() {
	        	
	            SwingUtilities.invokeLater(new Runnable() {
	                public void run() {
	                	 if(Case == 1){
	     					Monster launch = new Monster(LoonylandConstant.MONSTERNAME);
	     					launch.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     					launch.setSize(LoonylandConstant.WINDOWSIZE[0], LoonylandConstant.WINDOWSIZE[1]);
	     					launch.setResizable(false);
	     					launch.setVisible(true);
	     					frame.setLocationRelativeTo(null);
	     				}
	     				if(Case == 2){
	     					Profile launch = new Profile(LoonylandConstant.PROFILENAME);
	     					launch.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     					launch.setSize(950, 620);
	     					launch.setResizable(false);
	     					launch.setVisible(true);
	     					frame.setLocationRelativeTo(null);
	     				}
	     				frame.dispose();
	                }
	            });

	        }
	    };
	    new Thread(runnable).start();
	}
}