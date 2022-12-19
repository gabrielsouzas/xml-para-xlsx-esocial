package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import bean.Funcionario;
import bean.Rubricas;
import dao.FuncionarioDao;
import dao.RubricasDao;

import java.awt.Font;

public class TelaLerXML extends JDialog {
	
	// Genereted Serial Version
	private static final long serialVersionUID = 4369637007898292460L;
	
	public static ArrayList<String> rubricas = new ArrayList<String>();
	private ArrayList<String> funcionarios = new ArrayList<String>();
	private String caminho_excel = "";
	private String caminho_xml = ""; 
	private String nome_arquivo = "RELATORIOESOCIAL";
	private JTextField txtCaminhoArquivoEXCEL;
	private JTextField txtCaminhoArquivoXML;
	
	private int contleitxml = 0;
	
	// Novo
	private String nome_funcionario = "";
	private ArrayList<String> array_nome_funcionario;
	private File[] files;
	//private ArrayList<String> rubricas_array;
	private List<Rubricas> rubricas_obj;
	private ArrayList<String> array_codigos_rubricas;
	
	// Botões
	private JButton btnExportarParaExcelComVariosArquivos;
	private JButton btnExportarParaExcelComUmArquivo;
	private JButton btnGerarRelatrioAnalticoUmArquivo;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
					//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
					//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					//UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					
					TelaLerXML dialog = new TelaLerXML();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public TelaLerXML() {
		setTitle("Ler XML eSocial");
		setBounds(100, 100, 869, 312);
		getContentPane().setLayout(null);
		
		setLocationRelativeTo(null);
		
		getContentPane().setBackground(Color.white);
		
		JLabel lblCaminhoDoArquivo = new JLabel("Caminho para Salvar o Arquivo EXCEL:");
		lblCaminhoDoArquivo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCaminhoDoArquivo.setBounds(10, 44, 220, 14);
		getContentPane().add(lblCaminhoDoArquivo);
		
		txtCaminhoArquivoEXCEL = new JTextField();
		txtCaminhoArquivoEXCEL.setBounds(240, 41, 414, 20);
		getContentPane().add(txtCaminhoArquivoEXCEL);
		txtCaminhoArquivoEXCEL.setColumns(10);
		
		JButton btnBuscarArquivo = new JButton("Salvar Arquivo");
		btnBuscarArquivo.setBackground(new Color(176, 196, 222));
		btnBuscarArquivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				escolherLocalParaSalvarArquivoEXCEL();
			}
		});
		btnBuscarArquivo.setBounds(664, 40, 139, 23);
		getContentPane().add(btnBuscarArquivo);
		
		btnExportarParaExcelComUmArquivo = new JButton("<html>Exportar para Excel com um Arquivo por vez");
		btnExportarParaExcelComUmArquivo.setBackground(new Color(192, 192, 192));
		btnExportarParaExcelComUmArquivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					lerXML();
				} catch (ParserConfigurationException ex) {
					ex.printStackTrace();
				} catch (SAXException ex) {
					ex.printStackTrace();
				}
			}
		});
		btnExportarParaExcelComUmArquivo.setBounds(284, 72, 191, 51);
		getContentPane().add(btnExportarParaExcelComUmArquivo);
		
		btnGerarRelatrioAnalticoUmArquivo = new JButton("<html>Gerar Relat\u00F3rio Anal\u00EDtico de um Arquivo");
		btnGerarRelatrioAnalticoUmArquivo.setBackground(new Color(50, 205, 50));
		btnGerarRelatrioAnalticoUmArquivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					gerarRelatorioIndividual();
				} catch (ParserConfigurationException | SAXException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnGerarRelatrioAnalticoUmArquivo.setBounds(528, 72, 191, 51);
		getContentPane().add(btnGerarRelatrioAnalticoUmArquivo);
		
		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnSair.setBounds(741, 206, 102, 51);
		getContentPane().add(btnSair);
		
		JLabel lblCaminhoDoArquivo_1 = new JLabel("Caminho do Arquivo XML:");
		lblCaminhoDoArquivo_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCaminhoDoArquivo_1.setBounds(10, 15, 220, 14);
		getContentPane().add(lblCaminhoDoArquivo_1);
		
		txtCaminhoArquivoXML = new JTextField();
		txtCaminhoArquivoXML.setColumns(10);
		txtCaminhoArquivoXML.setBounds(240, 12, 414, 20);
		getContentPane().add(txtCaminhoArquivoXML);
		
		JButton button = new JButton("Buscar Arquivo");
		button.setBackground(new Color(176, 196, 222));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//buscarArquivoXML();
				buscarArquivoXMLNovo();
			}
		});
		button.setBounds(664, 11, 139, 23);
		getContentPane().add(button);
		
		popularListaRubricas();
		popularNomesCPF();
		
		txtCaminhoArquivoEXCEL.setText(System.getProperty("user.home")+"\\Desktop\\RELATORIOESOCIAL.xlsx");
		txtCaminhoArquivoXML.setText(System.getProperty("user.home")+"\\Desktop\\monitoresocial.xml");
		
		btnExportarParaExcelComVariosArquivos = new JButton("<html>Exportar para Excel com V\u00E1ros Arquivos");
		btnExportarParaExcelComVariosArquivos.setBackground(new Color(30, 144, 255));
		btnExportarParaExcelComVariosArquivos.setForeground(Color.WHITE);
		btnExportarParaExcelComVariosArquivos.setBorder(null);
		btnExportarParaExcelComVariosArquivos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					lerXMLNovo();
				} catch (ParserConfigurationException | SAXException e) {
					e.printStackTrace();
				}
			}
		});
		btnExportarParaExcelComVariosArquivos.setBounds(39, 72, 191, 51);
		getContentPane().add(btnExportarParaExcelComVariosArquivos);
		
		JLabel lblBasicamenteUma = new JLabel("<html>Corrigir erro de formata\u00E7\u00E3o na planilha gerada no Excel:\r\n<p>\r\n<p>Basicamente, \u00E9 uma solu\u00E7\u00E3o de 4 passos:\r\n<p>1\u00BA-  Seleciona a coluna da tabela com problema de formata\u00E7\u00E3o de datas;\r\n<p>2\u00BA - Vai na op\u00E7\u00E3o Dados do menu;\r\n<p>3\u00BA - Seleciona a op\u00E7\u00E3o Texto para Colunas;\r\n<p>4\u00BA - Clica (somente) em concluir. ");
		lblBasicamenteUma.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBasicamenteUma.setBounds(32, 138, 493, 119);
		getContentPane().add(lblBasicamenteUma);
		
		caminho_xml = txtCaminhoArquivoXML.getText();
        caminho_excel = txtCaminhoArquivoEXCEL.getText();
        //nome_arquivo = chooser.getSelectedFile().getName();
		
		/*try {
			lerXML();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}*/
        
        /*try {
			gerarRelatorioIndividual();
		} catch (ParserConfigurationException | SAXException e1) {
			e1.printStackTrace();
		}*/
        

	}
	
	public void escolherLocalParaSalvarArquivoEXCEL(){
		JFileChooser chooser = new JFileChooser(System.getProperty("user.home")+"\\Desktop\\");   //Cria o objeto do tipo Janela JFileChooser    
        chooser.setApproveButtonText("Salvar");
		chooser.setDialogTitle("Salvar o Arquivo em");  //Define o título do JFileChooser   
		chooser.setSelectedFile(new File("RELATORIOESOCIAL"));
        String caminho = "";
        int retorno = chooser.showSaveDialog(null); // showSaveDialog retorna um inteiro , e ele ira determinar que o chooser será para salvar.
        if (retorno==JFileChooser.APPROVE_OPTION){
              caminho = chooser.getSelectedFile().getAbsolutePath();  // o getSelectedFile pega o arquivo e o getAbsolutePath retorna uma string contendo o endereço.
              txtCaminhoArquivoEXCEL.setText(caminho+".xlsx");
              caminho_excel = txtCaminhoArquivoEXCEL.getText();
              nome_arquivo = chooser.getSelectedFile().getName();
        }
	}
	
	public void buscarArquivoXML(){
		JFileChooser chooser = new JFileChooser(System.getProperty("user.home")+"\\Desktop\\");   //Cria o objeto do tipo Janela JFileChooser    
        chooser.setApproveButtonText("Buscar");
		chooser.setDialogTitle("Buscar o Arquivo em");  //Define o título do JFileChooser   
		chooser.setFileFilter(new FileNameExtensionFilter("XML FILES", "xml"));
		
		//chooser.setSelectedFile(new File("RELATORIOESOCIAL"));
        String caminho = "";
        int retorno = chooser.showSaveDialog(null); // showSaveDialog retorna um inteiro , e ele ira determinar que o chooser será para salvar.
        if (retorno==JFileChooser.APPROVE_OPTION){
              caminho = chooser.getSelectedFile().getAbsolutePath();  // o getSelectedFile pega o arquivo e o getAbsolutePath retorna uma string contendo o endereço.
              txtCaminhoArquivoXML.setText(caminho);
              caminho_xml = txtCaminhoArquivoXML.getText();
        }
	}
	
	public void buscarArquivoXMLNovo(){
		JFileChooser chooser = new JFileChooser(System.getProperty("user.home")+"\\Desktop\\");   //Cria o objeto do tipo Janela JFileChooser    
        chooser.setApproveButtonText("Buscar");
		chooser.setDialogTitle("Buscar o Arquivo em");  //Define o título do JFileChooser   
		chooser.setFileFilter(new FileNameExtensionFilter("XML FILES", "xml"));
		// Possibilita a seleção de vários arquivos
	    chooser.setMultiSelectionEnabled(true);
	    
	    int retorno = chooser.showOpenDialog(null);
        if (retorno==JFileChooser.APPROVE_OPTION){
        	caminho_xml = chooser.getSelectedFile().getAbsolutePath();  // o getSelectedFile pega o arquivo e o getAbsolutePath retorna uma string contendo o endereço.
            txtCaminhoArquivoXML.setText(caminho_xml);
            files = chooser.getSelectedFiles();
        }
	    
		//chooser.showOpenDialog(null);
        //files = chooser.getSelectedFiles();
	}
	
	@SuppressWarnings("resource")
	public void lerXML() throws ParserConfigurationException, SAXException{
		try {
			FileInputStream arquivo_excel; // = new FileInputStream(new File(caminho_excel));
			
			//HSSFWorkbook workbook = new HSSFWorkbook(arquivo_excel);
			//HSSFSheet sheetEtiquetas = workbook.getSheetAt(0);
			
			XSSFWorkbook workbook;
			XSSFSheet sheetEtiquetas;
			
			File f = new File(caminho_excel);
			if(f.isFile() && !f.isDirectory()) { 
				arquivo_excel = new FileInputStream(new File(caminho_excel));
				workbook = new XSSFWorkbook(arquivo_excel);
				sheetEtiquetas = workbook.getSheetAt(0);
			} else {
				workbook = new XSSFWorkbook();
				sheetEtiquetas = workbook.createSheet(nome_arquivo);
			}
			
			Row linha = sheetEtiquetas.createRow(0+contleitxml);
			Cell coluna = linha.createCell(0);
			
			File file = new File(caminho_xml); 
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();
            System.out.println("Root Element :" + document.getDocumentElement().getNodeName());
            
            NodeList nListTrab = document.getElementsByTagName("ideTrabalhador");
            Node nNodeTrab = nListTrab.item(0);
            if (nNodeTrab.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNodeTrab;
                System.out.println("Funcionário: " + buscarNomeFuncionario(eElement.getElementsByTagName("cpfTrab").item(0).getTextContent()));
                /*System.out.println("CPF: " + eElement.getElementsByTagName("cpfTrab").item(0).getTextContent());
                System.out.println("NIS: " + eElement.getElementsByTagName("nisTrab").item(0).getTextContent());*/
                
                linha = sheetEtiquetas.createRow(1+contleitxml);
    			coluna = linha.createCell(0);
    			coluna.setCellValue("Funcionário");
    			
    			linha = sheetEtiquetas.createRow(2+contleitxml);
    			coluna = linha.createCell(0);
    			coluna.setCellValue(buscarNomeFuncionario(eElement.getElementsByTagName("cpfTrab").item(0).getTextContent())); // Nome do Funcionário
                
            }
        
            NodeList nList = document.getElementsByTagName("itensRemun");
            //System.out.println("----------------------------");
            int cel = 1;
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    /*System.out.println("Código Rúbrica: " + eElement.getElementsByTagName("codRubr").item(0).getTextContent());
                    System.out.println("Descrição Rúbrica: " + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[1]);
                    System.out.println("Natureza Rúbrica: " + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[2]);
                    System.out.println("INSS: " + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[3]);
                    System.out.println("IRRF: " + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[4]);
                    System.out.println("FGTS: " + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[5]);
                    System.out.println("Contribuição Sindical: " + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[6]);
                    System.out.println("Valor Rúbrica : " + eElement.getElementsByTagName("vrRubr").item(0).getTextContent());*/
                    
                    linha = sheetEtiquetas.getRow(0+contleitxml);
        			coluna = linha.createCell(cel);
        			coluna.setCellValue(buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[1]); // Descrição da Rúbrica
                    
        			linha = sheetEtiquetas.getRow(1+contleitxml);
        			coluna = linha.createCell(cel);
        			coluna.setCellValue(eElement.getElementsByTagName("codRubr").item(0).getTextContent()); // Código da Rúbrica
        			
        			linha = sheetEtiquetas.getRow(2+contleitxml);
        			coluna = linha.createCell(cel);
        			coluna.setCellValue(eElement.getElementsByTagName("vrRubr").item(0).getTextContent().replace(".", ",")); // Valor da Rúbrica
        		    
        			cel++;
                }
            }
            
            FileOutputStream outFile = new FileOutputStream(new File(caminho_excel));
			workbook.write(outFile);
			outFile.close();
            System.out.println("Arquivo Excel criado com sucesso!");
            contleitxml+=5;
            
        }
        catch (FileNotFoundException e) {
	        e.printStackTrace();
	        System.out.println("Arquivo não encontrado!");
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("Erro na edição do arquivo!");
	    }
	}
	
	@SuppressWarnings("resource")
	public void lerXMLNovo() throws ParserConfigurationException, SAXException{
		try {
			//rubricas_array = new ArrayList<String>();
			array_nome_funcionario = new ArrayList<String>();
			rubricas_obj = new ArrayList<Rubricas>();
			
			//FileInputStream arquivo_excel; // = new FileInputStream(new File(caminho_excel));
			
			//HSSFWorkbook workbook = new HSSFWorkbook(arquivo_excel);
			//HSSFSheet sheetEtiquetas = workbook.getSheetAt(0);
			
			/*XSSFWorkbook workbook;
			XSSFSheet sheetEtiquetas;
			
			File f = new File(caminho_excel);
			if(f.isFile() && !f.isDirectory()) { 
				arquivo_excel = new FileInputStream(new File(caminho_excel));
				workbook = new XSSFWorkbook(arquivo_excel);
				sheetEtiquetas = workbook.getSheetAt(0);
			} else {
				workbook = new XSSFWorkbook();
				sheetEtiquetas = workbook.createSheet(nome_arquivo);
			}
			
			Row linha = sheetEtiquetas.createRow(0+contleitxml);
			Cell coluna = linha.createCell(0);*/
			
			for (int i = 0; i < files.length; i++) {
				//File file = new File(caminho_xml); //"C:/Users/Informatica/Desktop/monitoresocial.xml"
	            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            Document document = db.parse(files[i]);
	            document.getDocumentElement().normalize();
	            System.out.println("Root Element :" + document.getDocumentElement().getNodeName());
	            
	            NodeList nListTrab = document.getElementsByTagName("ideTrabalhador");
	            Node nNodeTrab = nListTrab.item(0);
	            if (nNodeTrab.getNodeType() == Node.ELEMENT_NODE) {
	                Element eElement = (Element) nNodeTrab;
	                System.out.println("Funcionário: " + buscarNomeFuncionario(eElement.getElementsByTagName("cpfTrab").item(0).getTextContent()));
	                /*System.out.println("CPF: " + eElement.getElementsByTagName("cpfTrab").item(0).getTextContent());
	                System.out.println("NIS: " + eElement.getElementsByTagName("nisTrab").item(0).getTextContent());*/
	                
	                nome_funcionario = buscarNomeFuncionario(eElement.getElementsByTagName("cpfTrab").item(0).getTextContent());
	                array_nome_funcionario.add(nome_funcionario);
	                /*linha = sheetEtiquetas.createRow(1+contleitxml);
	    			coluna = linha.createCell(0);
	    			coluna.setCellValue("Funcionário");
	    			
	    			linha = sheetEtiquetas.createRow(2+contleitxml);
	    			coluna = linha.createCell(0);
	    			coluna.setCellValue(buscarNomeFuncionario(eElement.getElementsByTagName("cpfTrab").item(0).getTextContent())); // Nome do Funcionário
	                */
	            }
	        
	            NodeList nList = document.getElementsByTagName("itensRemun");
	            //int cel = 1;
	            for (int temp = 0; temp < nList.getLength(); temp++) {
	                Node nNode = nList.item(temp);
	                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	                    Element eElement = (Element) nNode;
	                    
	                    /*rubricas_array.add(eElement.getElementsByTagName("codRubr").item(0).getTextContent() + ";"
	                    		+ buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[1] + ";"
	                    		+ eElement.getElementsByTagName("vrRubr").item(0).getTextContent().replace(".", ",") + ";"
	                    		+ nome_funcionario);*/
	                    
	                    rubricas_obj.add(new Rubricas(eElement.getElementsByTagName("codRubr").item(0).getTextContent(),
	                    		buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[1],
	                    		eElement.getElementsByTagName("vrRubr").item(0).getTextContent().replace(".", ","),
	                    		nome_funcionario));
	                    
	                    /*linha = sheetEtiquetas.getRow(0+contleitxml);
	        			coluna = linha.createCell(cel);
	        			coluna.setCellValue(buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[1]); // Descrição da Rúbrica
	                    
	        			linha = sheetEtiquetas.getRow(1+contleitxml);
	        			coluna = linha.createCell(cel);
	        			coluna.setCellValue(eElement.getElementsByTagName("codRubr").item(0).getTextContent()); // Código da Rúbrica
	        			
	        			linha = sheetEtiquetas.getRow(2+contleitxml);
	        			coluna = linha.createCell(cel);
	        			coluna.setCellValue(eElement.getElementsByTagName("vrRubr").item(0).getTextContent().replace(".", ",")); // Valor da Rúbrica
	        		    
	        			cel++;*/
	                }
	            }
			}
            
            /*FileOutputStream outFile = new FileOutputStream(new File(caminho_excel));
			workbook.write(outFile);
			outFile.close();
            System.out.println("Arquivo Excel criado com sucesso!");
            contleitxml+=5;*/
			
			//Collections.sort(rubricas_array);
			
			Map<String, List<Rubricas>> map = groupBy(rubricas_obj, Rubricas::getCodigo);
			
			array_codigos_rubricas = new ArrayList<String>();
			
			map.entrySet().forEach(entry->{
				//System.out.println(entry.getKey() /*+ " = " + entry.getValue()*/);  
				array_codigos_rubricas.add(entry.getKey());
			}); 
			
			FileInputStream arquivo_excel;
			
			
			XSSFWorkbook workbook;
			XSSFSheet sheetEtiquetas;
			
			// Testa se o arquivo Excel já existe, se não, cria um
			File f = new File(caminho_excel);
			if(f.isFile() && !f.isDirectory()) { 
				arquivo_excel = new FileInputStream(new File(caminho_excel));
				workbook = new XSSFWorkbook(arquivo_excel);
				sheetEtiquetas = workbook.getSheetAt(0);
			} else {
				workbook = new XSSFWorkbook();
				sheetEtiquetas = workbook.createSheet(nome_arquivo);
			}
			
			Row linha = sheetEtiquetas.createRow(0);
			Cell coluna = linha.createCell(0);
			
			// Insere os nomes dos funcionários na planilha Excel
			if (array_nome_funcionario != null) {
				linha = sheetEtiquetas.createRow(1);
				coluna = linha.createCell(0);
				coluna.setCellValue("Funcionário");
				for (int i = 0; i < array_nome_funcionario.size(); i++) {
					linha = sheetEtiquetas.createRow(i+2);
					coluna = linha.createCell(0);
					coluna.setCellValue(array_nome_funcionario.get(i)); // Nome do Funcionário
				} 
			}
			
			// Insere as Descrições e os Códigos das Rúbricas na planilha Excel
			for (int i = 0; i < array_codigos_rubricas.size(); i++) {
				linha = sheetEtiquetas.getRow(1);
    			coluna = linha.createCell(i+1);
    			coluna.setCellValue(array_codigos_rubricas.get(i)); // Código da Rúbrica
    			
    			linha = sheetEtiquetas.getRow(0);
    			coluna = linha.createCell(i+1);
    			coluna.setCellValue(pesquisarDescricaoPorCodigoRubrica(array_codigos_rubricas.get(i))); // Descrição da Rúbrica
			}
			
			for (int lin = 0; lin < array_nome_funcionario.size(); lin++) {
				for (int col = 0; col < array_codigos_rubricas.size(); col++) {
					linha = sheetEtiquetas.getRow(1);
	    			coluna = linha.getCell(col+1);
	    			String codigo_excel = coluna.getStringCellValue();
	    			
	    			linha = sheetEtiquetas.getRow(lin+2);
	    			coluna = linha.getCell(0);
	    			String funcionario_excel = coluna.getStringCellValue();
	    			System.out.println(pesquisarValorPorCodigoFuncionarioRubrica(codigo_excel, funcionario_excel));
	    			linha = sheetEtiquetas.getRow(lin+2);
	    			coluna = linha.createCell(col+1);
	    			coluna.setCellValue(pesquisarValorPorCodigoFuncionarioRubrica(codigo_excel, funcionario_excel)); // Valor da Rúbrica
	    			
				}
			}
			
			FileOutputStream outFile = new FileOutputStream(new File(caminho_excel));
			workbook.write(outFile);
			outFile.close();
            System.out.println("Arquivo Excel criado com sucesso!");
            contleitxml+=5;
			
        }
        catch (FileNotFoundException e) {
	        e.printStackTrace();
	        System.out.println("Arquivo não encontrado!");
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("Erro na edição do arquivo!");
	    }
	}
	
	public String pesquisarDescricaoPorCodigoRubrica(String codigo_rubrica) {
	    for (int i = 0; i < rubricas_obj.size(); i++) {
			if (codigo_rubrica.equals(rubricas_obj.get(i).getCodigo())) {
				return rubricas_obj.get(i).getDescricao();
			}
		}
	    return "Não encontrada";
	}
	
	public String pesquisarValorPorCodigoFuncionarioRubrica(String codigo_rubrica, String funcionario_rubrica) {
	    for (int i = 0; i < rubricas_obj.size(); i++) {
			if (codigo_rubrica.equals(rubricas_obj.get(i).getCodigo()) && 
					funcionario_rubrica.equals(rubricas_obj.get(i).getFuncionario())) {
				return rubricas_obj.get(i).getValor();
			}
		}
	    return "0";
	}
	
	public static <E, K> Map<K, List<E>> groupBy(List<E> list, Function<E, K> keyFunction) {
	    return Optional.ofNullable(list)
	            .orElseGet(ArrayList::new)
	            .stream()
	            .collect(Collectors.groupingBy(keyFunction));
	}
	
	@SuppressWarnings("resource")
	public void gerarRelatorioIndividual() throws ParserConfigurationException, SAXException{
		try {
			FileInputStream arquivo_excel; // = new FileInputStream(new File(caminho_excel));
			
			//HSSFWorkbook workbook;
			//HSSFSheet sheetEtiquetas;
			
			XSSFWorkbook workbook;
			XSSFSheet sheetEtiquetas;
			
			File f = new File(caminho_excel);
			if(f.isFile() && !f.isDirectory()) { 
				arquivo_excel = new FileInputStream(new File(caminho_excel));
				workbook = new XSSFWorkbook(arquivo_excel);
				//workbook = new HSSFWorkbook(arquivo_excel);
				sheetEtiquetas = workbook.getSheetAt(0);
			} else {
				workbook = new XSSFWorkbook();
				//workbook = new HSSFWorkbook();
				//sheetEtiquetas = workbook.createSheet(nome_arquivo);
				sheetEtiquetas = workbook.createSheet(nome_arquivo);
			}
			
			Row linha = sheetEtiquetas.createRow(0);
			Cell coluna = linha.createCell(0);
			
			//Row linha = sheetEtiquetas.getRow(0);
			//Cell coluna = linha.getCell(0);
			
			File file = new File(caminho_xml);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();
            System.out.println("Root Element :" + document.getDocumentElement().getNodeName());
            
            NodeList nListTrab = document.getElementsByTagName("ideTrabalhador");
            Node nNodeTrab = nListTrab.item(0);
            if (nNodeTrab != null) {
            	if (nNodeTrab.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNodeTrab;
                    System.out.println("Funcionário: " + buscarNomeFuncionario(eElement.getElementsByTagName("cpfTrab").item(0).getTextContent()));
                    /*System.out.println("CPF: " + eElement.getElementsByTagName("cpfTrab").item(0).getTextContent());
                    System.out.println("NIS: " + eElement.getElementsByTagName("nisTrab").item(0).getTextContent());*/
                    
                    linha = sheetEtiquetas.createRow(1);
        			coluna = linha.createCell(0);
        			coluna.setCellValue("Funcionário:");
        			
        			linha = sheetEtiquetas.getRow(1);
        			coluna = linha.createCell(1);
        			coluna.setCellValue(buscarNomeFuncionario(eElement.getElementsByTagName("cpfTrab").item(0).getTextContent())); // Nome do Funcionário
        		
        			linha = sheetEtiquetas.createRow(2);
        			coluna = linha.createCell(0);
        			coluna.setCellValue("CPF:");
        			
        			linha = sheetEtiquetas.getRow(2);
        			coluna = linha.createCell(1);
        			coluna.setCellValue(eElement.getElementsByTagName("cpfTrab").item(0).getTextContent()); // CPF do Funcionário
        			
        			linha = sheetEtiquetas.createRow(3);
        			coluna = linha.createCell(0);
        			coluna.setCellValue("NIS:");
        			
        			linha = sheetEtiquetas.getRow(3);
        			coluna = linha.createCell(1);
        			if (eElement.getElementsByTagName("nisTrab").item(0) != null) {
        				coluna.setCellValue(eElement.getElementsByTagName("nisTrab").item(0).getTextContent()); // NIS do Funcionário
					} else {
						coluna.setCellValue("NIS Não encontrado");
					}
        			
        			linha = sheetEtiquetas.createRow(4);
        			coluna = linha.createCell(0);
        			coluna.setCellValue("");  // Cria uma linha em branco
        			
        			linha = sheetEtiquetas.createRow(5);
        			coluna = linha.createCell(0);
        			coluna.setCellValue("Código Rúbrica");
        			
        			linha = sheetEtiquetas.getRow(5);
        			coluna = linha.createCell(1);
        			coluna.setCellValue("Descrição Rúbrica");
        			
        			linha = sheetEtiquetas.getRow(5);
        			coluna = linha.createCell(2);
        			coluna.setCellValue("Natureza Rúbrica");
        			
        			linha = sheetEtiquetas.getRow(5);
        			coluna = linha.createCell(3);
        			coluna.setCellValue("INSS");
        			
        			linha = sheetEtiquetas.getRow(5);
        			coluna = linha.createCell(4);
        			coluna.setCellValue("IRRF");
        			
        			linha = sheetEtiquetas.getRow(5);
        			coluna = linha.createCell(5);
        			coluna.setCellValue("FGTS");
        			
        			linha = sheetEtiquetas.getRow(5);
        			coluna = linha.createCell(6);
        			coluna.setCellValue("Contribuição Sindical");
        			
        			linha = sheetEtiquetas.getRow(5);
        			coluna = linha.createCell(7);
        			coluna.setCellValue("Valor Rúbrica");
        			
                }
			} else {
				JOptionPane.showMessageDialog(null, "Erro. TAG não encontrada: ideTrabalhador");
			}
        
            NodeList nList = document.getElementsByTagName("itensRemun");
            //System.out.println("----------------------------");
            int lin = 6;
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode != null) {
                	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        /*System.out.println("Código Rúbrica: " + eElement.getElementsByTagName("codRubr").item(0).getTextContent());
                        System.out.println("Descrição Rúbrica: " + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[1]);
                        System.out.println("Natureza Rúbrica: " + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[2]);
                        System.out.println("INSS: " + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[3]);
                        System.out.println("IRRF: " + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[4]);
                        System.out.println("FGTS: " + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[5]);
                        System.out.println("Contribuição Sindical: " + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[6]);
                        System.out.println("Valor Rúbrica : " + eElement.getElementsByTagName("vrRubr").item(0).getTextContent());*/
                        
                        linha = sheetEtiquetas.createRow(lin);
                        coluna = linha.createCell(0);
                        coluna.setCellValue(eElement.getElementsByTagName("codRubr").item(0).getTextContent()); // Código da Rúbrica

                        linha = sheetEtiquetas.getRow(lin);
            			coluna = linha.createCell(1);
            			coluna.setCellValue(buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[1]); // Descrição da Rúbrica

                        linha = sheetEtiquetas.getRow(lin);
            			coluna = linha.createCell(2);
            			coluna.setCellValue(buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[2]); // Natureza da Rúbrica

                        linha = sheetEtiquetas.getRow(lin);
            			coluna = linha.createCell(3);
            			coluna.setCellValue(buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[3]); // INSS

                        linha = sheetEtiquetas.getRow(lin);
            			coluna = linha.createCell(4);
            			coluna.setCellValue(buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[4]); // IRRF

                        linha = sheetEtiquetas.getRow(lin);
            			coluna = linha.createCell(5);
            			coluna.setCellValue(buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[5]); // FGTS

                        linha = sheetEtiquetas.getRow(lin);
            			coluna = linha.createCell(6);
            			coluna.setCellValue(buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[6]); // Contribuição Sindical
                        
            			linha = sheetEtiquetas.getRow(lin);
            			coluna = linha.createCell(7);
            			coluna.setCellValue(eElement.getElementsByTagName("vrRubr").item(0).getTextContent().replace(".", ",")); // Valor da Rúbrica
            		    
            			lin++;
                    }
				} else {
					JOptionPane.showMessageDialog(null, "Erro. TAG não encontrada: itensRemun");
				}
            }
            
            FileOutputStream outFile = new FileOutputStream(new File(caminho_excel));
			workbook.write(outFile);
			outFile.close();
            System.out.println("Arquivo Excel criado com sucesso!");
            
        }
        catch (FileNotFoundException e) {
	        e.printStackTrace();
	        System.out.println("Arquivo não encontrado!");
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("Erro na edição do arquivo!");
	    }
	}
	
	/*public void gerarRelatorioIndividual() throws ParserConfigurationException, SAXException{
		try {
			
			ArrayList<String> param_nome = new ArrayList<String>();
			ArrayList<String> param_conteudo = new ArrayList<String>();
			
			//ArrayList<String> param_nome = new ArrayList<String>();
			ArrayList<String> detail = new ArrayList<String>();
			
			File file = new File(caminho_xml); //"C:/Users/Informatica/Desktop/monitoresocial.xml"
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();
            System.out.println("Root Element :" + document.getDocumentElement().getNodeName());
            
            NodeList nListTrab = document.getElementsByTagName("ideTrabalhador");
            Node nNodeTrab = nListTrab.item(0);
            if (nNodeTrab.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNodeTrab;
                param_nome.add("funcionario");
                param_conteudo.add(buscarNomeFuncionario(eElement.getElementsByTagName("cpfTrab").item(0).getTextContent()));
                param_nome.add("cpf");
                param_conteudo.add(eElement.getElementsByTagName("cpfTrab").item(0).getTextContent());
                param_nome.add("nis");
                param_conteudo.add(eElement.getElementsByTagName("nisTrab").item(0).getTextContent());
            }
        
        
            NodeList nList = document.getElementsByTagName("itensRemun");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    //param_nome.add("codigo_rubrica");
                    detail.add(eElement.getElementsByTagName("codRubr").item(0).getTextContent());
                    //param_nome.add("descricao_rubrica");
                    detail.add(buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[1]);
                    //param_nome.add("natureza_rubrica");
                    detail.add(buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[2]);
                    //param_nome.add("inss");
                    detail.add(buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[3]);
                    //param_nome.add("irrf");
                    detail.add(buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[4]);
                    //param_nome.add("fgts");
                    detail.add(buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[5]);
                    //param_nome.add("contribuicao_sindical");
                    detail.add(buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[6]);
                    //param_nome.add("valor_rubrica" );
                    detail.add(eElement.getElementsByTagName("vrRubr").item(0).getTextContent());
                    
                    detail.add(eElement.getElementsByTagName("codRubr").item(0).getTextContent() + " " 
                    		 + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[1] + " "
                    		 + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[2] + " "
                             + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[3] + " "
                             + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[4] + " "
                             + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[5] + " "
                             + buscarRubrica(eElement.getElementsByTagName("codRubr").item(0).getTextContent())[6] + " "
                             + eElement.getElementsByTagName("vrRubr").item(0).getTextContent());
                    
                }
            }
       
            InputStream caminho = getClass().getResourceAsStream("/relatorio/RelatorioeSocial.jasper");
            try {
				gerarRelatorioComVariosParametrosArrayList(param_nome, param_conteudo, detail, caminho);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (JRException e) {
				e.printStackTrace();
			}
		
		}
        catch(IOException e) {
        	e.printStackTrace();
            System.out.println(e);
        } 
	}*/
	
	public String[] buscarRubrica(String codigo_rubrica){ 
	    for (int j = 0; j < rubricas.size(); j++) {
			String[] sep = rubricas.get(j).split(";");
	    	if (sep[0].equals(codigo_rubrica)) {
				return sep;
			}
		}
	    String[] retorno_branco = {"-", "-", "-", "-", "-", "-", "-"};
		return retorno_branco;
	}
	
	public String buscarNomeFuncionario(String cpf){
		if (cpf.length() > 0) {
			FuncionarioDao funcionarioDao = new FuncionarioDao();
			Funcionario funcionario = funcionarioDao.selectByOneCpf(cpf);
			
			if (funcionario != null) {
				return funcionario.getNome().toString();
			} else {
				return "Funcionário não encontrado";
			}
		} else {
			return "CPF nulo";
		}
	}
	
	/* Versão com array de string
	public String buscarNomeFuncionario(String cpf){ 
	    for (int j = 0; j < funcionarios.size(); j++) {
			String[] sep = funcionarios.get(j).split(";");
	    	if (sep[0].equals(cpf)) {
				return sep[1];
			}
		}
		return "Funcionário não encontrado";
	}//*/
	
	/*public void gerarRelatorioComVariosParametrosArrayList( ArrayList<String> nomeparametro, ArrayList<String> valorparametro, ArrayList<String> detail,
					InputStream caminho) throws JRException, ClassNotFoundException {
		
		Map<String, Object> parametros = new HashMap<String, Object>();

		for (int i = 0; i < nomeparametro.size(); i++) {
			if (nomeparametro.get(i) != null && valorparametro.get(i) != null) {
				parametros.put(nomeparametro.get(i), valorparametro.get(i));
			}
		}
				
		JasperPrint impressao = JasperFillManager.fillReport(caminho, parametros, new JRBeanCollectionDataSource(detail));
		JasperViewer viewer = new JasperViewer( impressao , false );
		
		JDialog viewerj = new JDialog(new javax.swing.JFrame(),"Relatório eSocial", true);
		Dimension dimensao = Toolkit.getDefaultToolkit().getScreenSize(); 
		viewerj.setSize(dimensao);
		viewerj.setLocationRelativeTo(null);
		viewerj.getContentPane().add(viewer.getContentPane());
		
		viewerj.setVisible(true);
	}*/
	
	public void popularNomesCPF(){
		// Ordem: CPF; Nome
		funcionarios.add("045.589.785-88;GABRIEL SOUZA");
		
	}	
	
	// Ralatório de Rúbricas Datasul - FP0021
	// Parâmetros 
	//      >> Marca -> ExportaCSV
	//                  Imprime Complemento eSocial
	public void popularListaRubricas(){
		//RubricasDao rubricasDao = new RubricasDao();
		//rubricasDao.popularListaRubricas();
		
		// Ordem: Código Datasul; Descrição; Código E-Social; Incidencias
		rubricas.add("100-001;Hrs Normais Diurnas;1000;11;11;11;11");
		rubricas.add("100-002;Valor devido ao Prestador;3501;0;0;0;0");
		rubricas.add("100-003;Hrs Normais Noturnas;1000;11;11;11;11");
		rubricas.add("100-004;Hrs Abonadas Diurnas;1099;11;11;11;11");
		rubricas.add("100-005;Hrs Abonadas Noturnas;1099;11;11;11;11");
	}
}
