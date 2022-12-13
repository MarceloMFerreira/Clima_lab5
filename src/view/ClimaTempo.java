package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.thoughtworks.xstream.XStream;

import model.Cidade;
import model.Cidades;
import model.City;
import model.Previsao;
import model.service.WeatherForecastService;

public class ClimaTempo {

	private JFrame frame;
	private JTextField txtCodigo;
	private JTextField txtLocalizacao;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClimaTempo window = new ClimaTempo();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public ClimaTempo() {
		initialize();
	}

	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 922, 533);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(475, 100, 400, 350);
		frame.getContentPane().add(scrollPane_1);
		
		JTextArea txtTempo = new JTextArea();
		txtTempo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTempo.setBackground(new Color(224, 255, 255));
		txtTempo.setForeground(new Color(0, 0, 0));
		txtTempo.setEditable(false);
		scrollPane_1.setViewportView(txtTempo);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 100, 392, 350);
		frame.getContentPane().add(scrollPane);
		
		JTextArea txtCidade = new JTextArea();
		txtCidade.setBackground(new Color(224, 255, 255));
		txtCidade.setForeground(Color.BLACK);
		scrollPane.setViewportView(txtCidade);
		
		JLabel lblCdigo = new JLabel("Código:");
		lblCdigo.setToolTipText("Defina o");
		lblCdigo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCdigo.setBounds(475, 40, 66, 17);
		frame.getContentPane().add(lblCdigo);
		
		txtCodigo = new JTextField();
		txtCodigo.setToolTipText("Código");
		txtCodigo.setColumns(8);
		txtCodigo.setBounds(540, 40, 226, 19);
		frame.getContentPane().add(txtCodigo);
		
		JButton btnClima = new JButton("Buscar");
		btnClima.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtTempo.setText(null);
				try {
					int local = Integer.parseInt(txtCodigo.getText());
					
					String previsao = WeatherForecastService.sevenDaysWeatherForecast(local);
					
					XStream xstream = new XStream();
					
					// Ajuste de segurança do XStream
					Class<?>[] classes = new Class[] {Cidade.class, Previsao.class};
					xstream.allowTypes(classes);
					
					xstream.alias("cidade", Cidade.class);
					xstream.alias("previsao", Previsao.class);
					
					xstream.addImplicitCollection(Cidade.class, "previsoes");
					
					Cidade c = (Cidade) xstream.fromXML(previsao);
					
					txtTempo.append("Previsão para: ");
					txtTempo.append(c.getNome());
					txtTempo.append(c.getUf());
					txtTempo.append("\n");
					
					for (Previsao p : c.getPrevisoes()) {
						txtTempo.append("\nDia: " + p.getDia());
						txtTempo.append("\nMinima: " + p.getMinima());
						txtTempo.append("\nMaxima: " + p.getMaxima());
						txtTempo.append("\nTempo: ");
						String t = p.getTempo();
						txtTempo.append(decricao(t));
						txtTempo.append("\n");
					}
					
				} catch (IOException e1) {
					System.out.println("Erro ao consultar");
					e1.printStackTrace();
				}
			}	
		});
		
		btnClima.setBounds(780, 40, 107, 21);
		frame.getContentPane().add(btnClima);
		
		JLabel lblLocalizao = new JLabel("Localização:");
		lblLocalizao.setToolTipText("Defina o");
		lblLocalizao.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLocalizao.setBounds(10, 40, 89, 17);
		frame.getContentPane().add(lblLocalizao);
		
		txtLocalizacao = new JTextField();
		txtLocalizacao.setToolTipText("");
		txtLocalizacao.setColumns(10);
		txtLocalizacao.setBounds(104, 40, 203, 19);
		frame.getContentPane().add(txtLocalizacao);
		
		JButton btnLocalizacao = new JButton("Buscar");
		btnLocalizacao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtCidade.setText(null);
				
				try {
					
					String city = txtLocalizacao.getText();
					String cidades = WeatherForecastService.citysearch(city);
					
					XStream xstream = new XStream();
					
					// Ajuste de segurança do XStream
					Class<?>[] classes = new Class[] {Cidades.class, City.class};
					xstream.allowTypes(classes);
					
					xstream.alias("cidades", Cidades.class);
					xstream.alias("cidade", City.class);
					
					xstream.addImplicitCollection(Cidades.class, "cidade");
					
					Cidades c = (Cidades) xstream.fromXML(cidades);
					
					for (City p : c.getCidade()) {
						txtCidade.append("\nNome: " + p.getNome());
						txtCidade.append("\nUf: " + p.getUf());
						txtCidade.append("\nId: " + p.getId());
						txtCidade.append("\n");
					}
					
					
				} catch (IOException e1) {
					System.out.println("Erro ao consultar API de clima.");
					e1.printStackTrace();
				}
				
				
			}
		});
		btnLocalizacao.setBounds(320, 40, 107, 21);
		frame.getContentPane().add(btnLocalizacao);
		
		JLabel lblNewLabel_1_1 = new JLabel("Localização");
		lblNewLabel_1_1.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblNewLabel_1_1.setBounds(150, 70, 158, 36);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Clima");
		lblNewLabel_1_1_1.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblNewLabel_1_1_1.setBounds(640, 70, 158, 36);
		frame.getContentPane().add(lblNewLabel_1_1_1);
	}
	
	   private String decricao(String t) {
		   
		   switch (t) {
		   case"ci": t ="Chuvas Isoladas";break;
		   case"c": t ="Chuva";break;
		   case"in": t ="Instável";break;
		   case"pp": t ="Poss. de Pancadas de Chuva";break;
		   case"cm": t ="Chuva pela Manhã";break;
		   case"cn": t ="Chuva a Noite";break;
		   case"pt": t ="Pancadas de Chuva a Tarde";break;
		   case"pm": t ="Pancadas de Chuva pela Manhã";break;
		   case"np": t ="Nublado e Pancadas de Chuva";break;
		   case"pc": t ="Pancadas de Chuva";break;
		   case"pn": t ="Parcialmente Nublado";break;
		   case"cv": t ="Chuvisco";break;
		   case"ch": t ="Chuvoso";break;
		   case"t": t ="Tempestade";break;
		   case"ps": t ="Predomínio de Sol";break;
		   case"e": t ="Encoberto";break;
		   case"n": t ="Nublado";break;
		   case"cl": t ="Céu Claro";break;
		   case"nv": t ="Nevoeiro";break;
		   case"g": t ="Geada";break;
		   case"ne": t ="Neve";break;
		   case"nd": t ="Não Definido";break;
		   case"pnt": t ="Pancadas de Chuva a Noite";break;
		   case"psc": t ="Possibilidade de Chuva";break;
		   case"pcm": t ="Possibilidade de Chuva pela Manhã";break;
		   case"pct": t ="Possibilidade de Chuva a Tarde";break;
		   case"pcn": t ="Possibilidade de Chuva a Noite";break;
		   case"npt": t ="Nublado com Pancadas a Tarde";break;
		   case"npn": t ="Nublado com Pancadas a Noite";break;
		   case"ncn": t ="Nublado com Poss. de Chuva a Noite";break;
		   case"nct": t ="Nublado com Poss. de Chuva a Tarde";break;
		   case"ncm": t ="Nubl. c/ Poss. de Chuva pela Manhã";break;
		   case"npm": t ="Nublado com Pancadas pela Manhã";break;
		   case"npp": t ="Nublado com Possibilidade de Chuva";break;
		   case"vn": t ="Variação de Nebulosidade";break;
		   case"ct": t ="Chuva a Tarde";break;
		   case"ppn": t ="Poss. de Panc. de Chuva a Noite";break;
		   case"ppt": t ="Poss. de Panc. de Chuva a Tarde";break;
		   case"ppm": t ="Poss. de Panc. de Chuva pela Manhã";break;

		default:
			t = "Erro ao localizar";
		}
		   
	return t;
	}
}
