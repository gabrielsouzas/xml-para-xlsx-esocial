package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class TelaPesquisarFuncionario extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTable table;

	public static void main(String[] args) {
		try {
			TelaPesquisarFuncionario dialog = new TelaPesquisarFuncionario();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TelaPesquisarFuncionario() {
		setBounds(100, 100, 519, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblCpfnome = new JLabel("CPF/Nome:");
		lblCpfnome.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCpfnome.setBounds(10, 11, 99, 14);
		contentPanel.add(lblCpfnome);
		
		textField = new JTextField();
		textField.setBounds(119, 8, 231, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.setBounds(360, 7, 89, 23);
		contentPanel.add(btnPesquisar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 483, 150);
		contentPanel.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel.setBounds(10, 197, 483, 53);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JButton btnInserir = new JButton("Inserir");
		btnInserir.setBounds(10, 11, 89, 31);
		panel.add(btnInserir);
		
		JButton btnAlterar = new JButton("Alterar");
		btnAlterar.setBounds(109, 11, 89, 31);
		panel.add(btnAlterar);
		
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.setBounds(208, 11, 89, 31);
		panel.add(btnExcluir);
		
		JButton btnSair = new JButton("Sair");
		btnSair.setBounds(384, 11, 89, 31);
		panel.add(btnSair);
	}
}
