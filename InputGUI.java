package xlsxReadin.xlsxReader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class InputGUI {
	private int skip;
	private int sheet;
	private String cas;
	private String out;
	private File activeFile;
	private JTextField fileField;
	
	public InputGUI() {
		initUI();
	}
	
	public int getSkip() {
		return this.skip;
	}
	
	public int getSheet() {
		return this.sheet;
	}
	
	public String getCas() {
		return this.cas;
	}
	
	public String getOut() {
		return this.out;
	}
	
	public File getFile() {
		return this.activeFile;
	}
	
	private void initUI() {
		JTextField skipField = new JTextField("1", 10);
		JTextField sheetField = new JTextField("1", 10);
		JTextField casField = new JTextField("B", 10);
		JTextField outField = new JTextField("C", 10);
		fileField = new JTextField("", 10);
		
		JLabel skipLabel = new JLabel("Number of rows to skip:");
		JLabel sheetLabel = new JLabel("Sheet number:");
		JLabel casLabel = new JLabel("CAS column:");
		JLabel outLabel = new JLabel("Output column");
		JLabel fileLabel = new JLabel("File");
		
		JButton quitBtn = new JButton("Cancel");
		quitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		}); 
		
		
		JButton fileBtn = new JButton("File");
		fileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				File dir = new File(System.getProperty("user.dir"));
				JFileChooser chooser = new JFileChooser(dir);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel spreadsheets", "xlsx");
				chooser.setFileFilter(filter);
				int result = chooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					activeFile = chooser.getSelectedFile();
					String fileName = activeFile.getName();
					fileField.setText(fileName);
				}

			}
		});
		
		
		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		panel.setLayout(layout);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(skipLabel)
						.addComponent(casLabel)
						.addComponent(fileLabel))
				.addGroup(layout.createParallelGroup()
						.addComponent(skipField)
						.addComponent(casField)
						.addComponent(fileField))
				.addGroup(layout.createParallelGroup()
						.addComponent(sheetLabel)
						.addComponent(outLabel)
						.addComponent(fileBtn))
				.addGroup(layout.createParallelGroup()
						.addComponent(sheetField)
						.addComponent(outField))
				);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(skipLabel)
						.addComponent(skipField)
						.addComponent(sheetLabel)
						.addComponent(sheetField))
				.addGroup(layout.createParallelGroup()
						.addComponent(casLabel)
						.addComponent(casField)
						.addComponent(outLabel)
						.addComponent(outField))
				.addGroup(layout.createParallelGroup()
						.addComponent(fileLabel)
						.addComponent(fileField)
						.addComponent(fileBtn))
				);
		
		
		int result = JOptionPane.showConfirmDialog(null, panel, "text", JOptionPane.OK_OPTION );
		if(result == JOptionPane.OK_OPTION && this.activeFile != null) {
			this.skip = Integer.parseInt(skipField.getText());
			this.sheet = Integer.parseInt(sheetField.getText());
			this.cas = casField.getText();
			this.out = outField.getText();
		}
		else if (result == JOptionPane.OK_OPTION && this.activeFile == null) {
			JOptionPane.showMessageDialog(null, "No file selected. Exiting.");
		}
		else if (result == JOptionPane.NO_OPTION) {
			System.exit(0);
		}
		
	}


}
