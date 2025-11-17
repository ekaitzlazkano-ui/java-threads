import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Threads2 extends JFrame{

	private static final long serialVersionUID = 1L;
	private JButton Iniciar = new JButton("Iniciar");
	private JButton Parar = new JButton("Parar");
	private JProgressBar progressBar = new JProgressBar();
	private Thread hiloIniciar;
	
	public Threads2() {
		//principal
		setTitle("Threads2");
		setSize(900,600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//panels
		JPanel panelPrincipal = new JPanel(new GridLayout(2,1));
		JPanel panelBotones = new JPanel(new GridLayout(1,2));
		//buttons
		
		Iniciar.setEnabled(true);
		
		
		Parar.setEnabled(false);
		Iniciar.addActionListener((e) -> {
			hiloIniciar = new Thread( new Runnable() {
				
				@Override
				public void run() {
						IniciarMetodo();
			}
			
		});
			hiloIniciar.start();
			
		});
		Parar.addActionListener((e2) -> {
			new Thread(() -> {
				PararMetodo();
			}).start();
		});
		panelBotones.add(Iniciar);
		panelBotones.add(Parar);
		panelPrincipal.add(panelBotones);
		//JProgressBar
		
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.red);
		panelPrincipal.add(progressBar);
		
		
		add(panelPrincipal);
	}
	
	
	private void IniciarMetodo() {
		Parar.setEnabled(true);
		Iniciar.setEnabled(false);
		progressBar.setValue(0);
		while(progressBar.getValue() < 100) {
			try {
				Thread.sleep(200);
				if (Thread.currentThread().isInterrupted()) {
					break;
				}
				progressBar.setValue(progressBar.getValue()+1);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	private void PararMetodo() {
		Parar.setEnabled(false);
		Iniciar.setEnabled(true);
		hiloIniciar.interrupt();
		
		
	}
	
	public static void main(String[] args) {
		Threads2 frame = new Threads2();
		frame.setVisible(true);
	}
}
