import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ScrollPaneLayout;

public class Threads_3 extends JFrame{

	private static final long serialVersionUID = 1L;

	private Thread hilo;
	private int counter;
	private Thread hiloVerde;
	private Thread hiloAmarillo;
	private Thread hiloRojo;
	private boolean paused = false;
	private boolean running = true;
	
	public Threads_3() {
		setTitle("Semáforo");
		setSize(375,600);
		setLocationRelativeTo(null);
		setResizable(false);
		
		//Panel principal
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		
		//panelSemaforo
		JPanel panelSemaforo = new JPanel(new GridLayout(3,1,0,10));
		
		JLabel labelRojo = new JLabel("");
		labelRojo.setBackground(Color.BLACK);
		labelRojo.setForeground(Color.RED);
		labelRojo.setHorizontalAlignment(JLabel.CENTER);
		labelRojo.setFont(new Font("Arial", Font.PLAIN, 100));
		labelRojo.setOpaque(true);
		
		
		JLabel labelAmarillo = new JLabel("");
		labelAmarillo.setBackground(Color.BLACK);
		labelAmarillo.setForeground(Color.YELLOW);
		labelAmarillo.setHorizontalAlignment(JLabel.CENTER);
		labelAmarillo.setFont(new Font("Arial", Font.PLAIN, 100));
		labelAmarillo.setOpaque(true);
		
		JLabel labelVerde = new JLabel("");
		labelVerde.setBackground(Color.BLACK);
		labelVerde.setForeground(Color.GREEN);
		labelVerde.setHorizontalAlignment(JLabel.CENTER);
		labelVerde.setFont(new Font("Arial", Font.PLAIN, 100));
		labelVerde.setOpaque(true);
		
		panelSemaforo.add(labelRojo);
		panelSemaforo.add(labelAmarillo);
		panelSemaforo.add(labelVerde);
		
		panelPrincipal.add(panelSemaforo, BorderLayout.CENTER);
		
		//panelButtons
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
		panelButtons.setPreferredSize(new Dimension(0,50));
		
		JButton botonIniciar = new JButton("Iniciar");
		botonIniciar.setEnabled(true);
		

		JButton botonPausar = new JButton("Pausar");
		
		botonIniciar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				botonPausar.setText("Pausar");
				running= true;
				
				hilo = new Thread(new Runnable() {
					
					@Override
					public void run() {
						while(running) {
						try {
							hiloVerde = new Thread(new Runnable() {
								@Override
								public void run() {
									botonIniciar.setEnabled(false);
									counter = 11;
									labelVerde.setText(String.valueOf(counter));
									labelAmarillo.setText("");
									labelRojo.setText("");
									while(running && counter > 1) {
										synchronized (Threads_3.this) {
											while (paused) {
												try {
													Threads_3.this.wait();
												} catch (InterruptedException e) {
													e.printStackTrace();
												}
											}
										}
										try {
											counter -= 1;
											labelVerde.setText(String.valueOf(counter));
											Thread.sleep(1000);
										
										
										} catch (InterruptedException e) {
											break;
										}
										
											
									}
									
								}
								
							});
							hiloVerde.start();
							if (!running) {
								break;
							}
							hiloVerde.join();
							if (!running) {
								break;
							}
							hiloAmarillo = new Thread(new Runnable() {
								@Override
								public void run() {
									labelVerde.setText("");
									counter = 4;
									labelAmarillo.setText(String.valueOf(counter));
									while (running && counter > 1) {
										synchronized (Threads_3.this) {
											while (paused) {
												try {
													Threads_3.this.wait();
												} catch (InterruptedException e) {
													e.printStackTrace();
												}
											}
										}
										try {
											counter -= 1;
											labelAmarillo.setText(String.valueOf(counter));
											Thread.sleep(1000);
											
										} catch (InterruptedException e) {
											break;
										}
										
									}
									
								}
							});
							hiloAmarillo.start();
							if (!running) {
								break;
							}
							hiloAmarillo.join();
							if (!running) {
								break;
							}
							hiloRojo = new Thread(new Runnable() {
								
								@Override
								public void run() {
									labelAmarillo.setText("");
									counter = 11;
									labelRojo.setText(String.valueOf(counter));
									while(running && counter > 1) {
										synchronized (Threads_3.this) {
											while (paused) {
												try {
													Threads_3.this.wait();
												} catch (InterruptedException e) {
													e.printStackTrace();
												}
											}
										}
										try {
											counter -= 1;
											labelRojo.setText(String.valueOf(counter));
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											break;
										}
										
									}
									
								
								}
							});
							hiloRojo.start();
							if (!running) {
								break;
							}
							hiloRojo.join();
							if (!running) {
								break;
							}
						} catch (InterruptedException e) {
							break;
						}
						}
					}
				});
				hilo.start();
			}
		});
		
		
		botonPausar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (botonPausar.getText().equals("Pausar")) {
					paused = true;
					botonPausar.setText("Reanudar");
				}
				else {
					synchronized (Threads_3.this) {
						Threads_3.this.notifyAll();
						paused = false;
						botonPausar.setText("Pausar");
					}
					
				}
			}
		});
		JButton botonDetener = new JButton("Detener");
		botonDetener.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 1. Desactivar el bucle principal
		        running = false;

		        // 2. Despausar cualquier hilo en wait()
		        synchronized (Threads_3.this) {
		            paused = false;
		            Threads_3.this.notifyAll();
		        }

		        // 3. Interrumpir los hilos secundarios si están corriendo
		        if (hiloVerde != null) hiloVerde.interrupt();
		        if (hiloAmarillo != null) hiloAmarillo.interrupt();
		        if (hiloRojo != null) hiloRojo.interrupt();

		        // 4. Esperar a que el hilo principal termine
		        if (hilo != null) {
		            try {
		                hilo.join();
		            } catch (InterruptedException ex) {
		                ex.printStackTrace();
		            }
		        }

		        // 5. Reset visual y botones
		        labelVerde.setText("");
		        labelAmarillo.setText("");
		        labelRojo.setText("");
		        botonIniciar.setEnabled(true);
		        botonPausar.setText("Pausar");
			}
		});
		
		panelButtons.add(botonIniciar);
		panelButtons.add(botonPausar);
		panelButtons.add(botonDetener);
		

		
		
		panelPrincipal.add(panelButtons, BorderLayout.SOUTH);
		
		
		
		add(panelPrincipal);
	}
	


	public static void main(String[] args) {
		Threads_3 frame = new Threads_3();
		frame.setVisible(true);
	}
}
