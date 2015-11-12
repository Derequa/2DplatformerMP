package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import events.*;
import time.TimeLine;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ClientManager extends JFrame implements ActionListener {

	private static final long serialVersionUID = 7251259630714833216L;
	private static Client myClient;
	
	JPanel panel = new JPanel();
	JPanel panel_1 = new JPanel();
	JPanel panel_2 = new JPanel();
	JPanel panel_3 = new JPanel();
	
	JButton btnStartRecording = new JButton("Start Recording");
	JButton btnStopRecording = new JButton("Stop Recording");
	JButton btnPlay = new JButton("Play");
	JButton btnPause = new JButton("Pause");
	JButton btnRestart = new JButton("Restart");
	JButton btnSpeed = new JButton("1/2x Speed");
	JButton btnxSpeed = new JButton("2x Speed");
	JButton btnNormalSpeed = new JButton("Normal Speed");
	
	JLabel lblPlaybackSpeed = new JLabel("Playback Speed:");

	public ClientManager(Client c) {
		myClient = c;
		setupGUI();
	}

	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(btnStartRecording)){
			if(!myClient.recordingReplay){
				ReplayEvent event = new ReplayEvent(myClient.globalTime.getTime(), EventManager.HIGHEST);
				try {
					myClient.inputLock.acquire();
					myClient.eventManager.raiseReplayEvent(event);
					myClient.inputLock.release();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		else if(e.getSource().equals(btnStopRecording)){
			if(myClient.recordingReplay){
				StopReplayEvent event = new StopReplayEvent(myClient.globalTime.getTime(), EventManager.HIGHEST);
				try {
					myClient.inputLock.acquire();
					myClient.eventManager.raiseReplayEvent(event);
					myClient.inputLock.release();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		else if(e.getSource().equals(btnPlay)){
			if((!myClient.recordingReplay) && (Client.replayViewer != null) && !Client.replayViewer.playing){
				PlayPauseReplayEvent event = new PlayPauseReplayEvent(myClient.globalTime.getTime(), EventManager.HIGHEST, true);
				try {
					myClient.inputLock.acquire();
					myClient.eventManager.raiseReplayEvent(event);
					myClient.inputLock.release();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		else if(e.getSource().equals(btnPause)){
			if((!myClient.recordingReplay) && (Client.replayViewer != null) && Client.replayViewer.playing){
				PlayPauseReplayEvent event = new PlayPauseReplayEvent(myClient.globalTime.getTime(), EventManager.HIGHEST, false);
				try {
					myClient.inputLock.acquire();
					myClient.eventManager.raiseReplayEvent(event);
					myClient.inputLock.release();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		else if(e.getSource().equals(btnRestart)){
			if((!myClient.recordingReplay) && (Client.replayViewer != null)){
				RestartReplayEvent event = new RestartReplayEvent(myClient.globalTime.getTime(), EventManager.HIGHEST);
				try {
					myClient.inputLock.acquire();
					myClient.eventManager.raiseReplayEvent(event);
					myClient.inputLock.release();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		else if(e.getSource().equals(btnSpeed)){
			if((!myClient.recordingReplay) && (Client.replayViewer != null) && Client.replayViewer.playing){
				ReplaySpeedChangeEvent event = new ReplaySpeedChangeEvent(myClient.globalTime.getTime(), EventManager.HIGHEST, TimeLine.HALF);
				try {
					myClient.inputLock.acquire();
					myClient.eventManager.raiseReplayEvent(event);
					myClient.inputLock.release();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		else if(e.getSource().equals(btnxSpeed)){
			if((!myClient.recordingReplay) && (Client.replayViewer != null) && Client.replayViewer.playing){
				ReplaySpeedChangeEvent event = new ReplaySpeedChangeEvent(myClient.globalTime.getTime(), EventManager.HIGHEST, TimeLine.DOUBLE);
				try {
					myClient.inputLock.acquire();
					myClient.eventManager.raiseReplayEvent(event);
					myClient.inputLock.release();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		else if(e.getSource().equals(btnNormalSpeed)){
			if((!myClient.recordingReplay) && (Client.replayViewer != null) && Client.replayViewer.playing){
				ReplaySpeedChangeEvent event = new ReplaySpeedChangeEvent(myClient.globalTime.getTime(), EventManager.HIGHEST, TimeLine.DEFAULT);
				try {
					myClient.inputLock.acquire();
					myClient.eventManager.raiseReplayEvent(event);
					myClient.inputLock.release();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void setButtons(){
		if(myClient.recordingReplay){
			btnStartRecording.setEnabled(false);
			btnStopRecording.setEnabled(true);
			btnPlay.setEnabled(false);
			btnPause.setEnabled(false);
			btnRestart.setEnabled(false);
			btnSpeed.setEnabled(false);
			btnxSpeed.setEnabled(false);
			btnNormalSpeed.setEnabled(false);
		}
		else if(Client.replayViewer == null){
			btnStartRecording.setEnabled(true);
			btnStopRecording.setEnabled(false);
			btnPlay.setEnabled(false);
			btnPause.setEnabled(false);
			btnRestart.setEnabled(false);
			btnSpeed.setEnabled(false);
			btnxSpeed.setEnabled(false);
			btnNormalSpeed.setEnabled(false);
		}
		else{
			btnStartRecording.setEnabled(true);
			btnStopRecording.setEnabled(false);
			if(Client.replayViewer.playing){
				btnPlay.setEnabled(false);
				btnPause.setEnabled(true);
			}
			else{
				btnPlay.setEnabled(true);
				btnPause.setEnabled(false);
			}
			btnRestart.setEnabled(true);
			btnSpeed.setEnabled(true);
			btnxSpeed.setEnabled(true);
			btnNormalSpeed.setEnabled(true);
		}
	}
	
	private void setupGUI(){
		setSize(420, 150);
		setTitle("Replay Manager");
		Container contentPane = this.getContentPane();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 387, 151);
		contentPane = new JPanel();
		((JComponent) contentPane).setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(btnStartRecording);
		panel.add(btnStopRecording);
		contentPane.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		panel_1.add(btnPlay);
		panel_1.add(btnPause);
		panel_1.add(btnRestart);
		contentPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel_2.add(lblPlaybackSpeed);
		panel_2.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));
		panel_3.add(btnSpeed);
		panel_3.add(btnxSpeed);
		panel_3.add(btnNormalSpeed);

		btnStartRecording.addActionListener(this);
		btnStopRecording.addActionListener(this);
		btnPlay.addActionListener(this);
		btnPause.addActionListener(this);
		btnRestart.addActionListener(this);
		btnxSpeed.addActionListener(this);
		btnNormalSpeed.addActionListener(this);
		btnSpeed.addActionListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setButtons();
		setVisible(true);
	}

}
