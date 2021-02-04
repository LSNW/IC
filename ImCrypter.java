import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.custom.CCombo;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
//import javax.swing.Timer;

import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;

public class ImCrypter {
	private DataBindingContext m_bindingContext;

	protected Shell shlImcrypter;

	private static Crypter cr;
	private static Filer fr;
	private static Keymanager km;
	private static Label updateLabel;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 * @throws Exception
	 */

	public static void main(String[] args) throws Exception {
		new ImCrypter();
		windowInit();
	}

	public ImCrypter() throws Exception {
		cr = new Crypter();
		fr = new Filer();
		km = new Keymanager();
	}

	public static void windowInit() {
		Display display = Display.getDefault();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				try {
					ImCrypter window = new ImCrypter();
					window.open();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Open the window.
	 * 
	 * @throws Exception
	 */
	public void open() throws Exception {
		Display display = Display.getDefault();
		createContents();
		shlImcrypter.open();
		shlImcrypter.layout();
		while (!shlImcrypter.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * 
	 * @throws Exception
	 */
	protected void createContents() throws Exception {
		shlImcrypter = new Shell();
		shlImcrypter.setImage(SWTResourceManager.getImage(ImCrypter.class, "/stock/IC.ico"));
		shlImcrypter.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		shlImcrypter.setSize(645, 379);
		shlImcrypter.setText("ImCrypter");

		Label fileLabel = new Label(shlImcrypter, SWT.NONE);
		fileLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		fileLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		fileLabel.setFont(SWTResourceManager.getFont("Rockwell Condensed", 20, SWT.NORMAL));
		fileLabel.setBounds(251, 112, 281, 39);

		updateLabel = new Label(shlImcrypter, SWT.NONE);
		updateLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				setUpdate("");
			}
		});
		updateLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		updateLabel.setFont(SWTResourceManager.getFont("Rockwell Condensed", 20, SWT.NORMAL));
		updateLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
		updateLabel.setBounds(22, 283, 587, 39);
		
		// ENCRYPT
		// BUTTON///////////////////////////////////////////////////////////////////////////////////
		Button btnPressThis = new Button(shlImcrypter, SWT.BORDER | SWT.CENTER);
		btnPressThis.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					if (cr.getActive() == null) {
						setUpdate("No identifier selected");
					} else if (!(".jpg".equals(fr.getActiveExtension(4)))) {
						setUpdate("Selection is not a .jpg file");
					} else {
						if (fr.createEnFile(cr.encrypt(fr.createImgByte()))) {
							setUpdate("Image Encrypted to File");
						}
					}
				} catch (NullPointerException npe) {
					setUpdate("No file selected");
				}
			}
		});
		btnPressThis.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		btnPressThis.setFont(SWTResourceManager.getFont("Rockwell Condensed", 19, SWT.NORMAL));
		btnPressThis.setBounds(239, 203, 160, 64);
		btnPressThis.setText("Encrypt");

		// DECRYPT
		// BUTTON////////////////////////////////////////////////////////////////////////////////////
		Button btnDecrypt = new Button(shlImcrypter, SWT.BORDER | SWT.CENTER);
		btnDecrypt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					if (cr.getActive() == null) {
						setUpdate("No identifier selected");
					} else if (!(".ic".equals(fr.getActiveExtension(3)))) {
						setUpdate("Selection is not a .ic file");
					} else if (fr.createImg(cr.decrypt(fr.readFileToByteArray()))) {
						setUpdate("File Decrypted and Written to Image");
					}
				} catch (NullPointerException npe) {
					setUpdate("No file selected");
				//} catch (IOException iox) {
					//setUpdate("Filetype decryption error");
				} catch (BadPaddingException bpe) {
					setUpdate("Invalid identifier for decryption");
				} catch (IllegalBlockSizeException ibe) {
					setUpdate("Not an encrypted file");
				} catch (NegativeArraySizeException nas) {
					setUpdate("Nonexistent/Empty file. Try another");
				} catch (Exception e1) {
					System.out.println(e1);
					//setUpdate("There was an error");
				}
			}
		});
		btnDecrypt.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		btnDecrypt.addSelectionListener(new SelectionAdapter() {

		});
		btnDecrypt.setText("Decrypt");
		btnDecrypt.setFont(SWTResourceManager.getFont("Rockwell Condensed", 19, SWT.NORMAL));
		btnDecrypt.setBounds(449, 203, 160, 64);

		// CCOMBO
		// BOX///////////////////////////////////////////////////////////////////////////////////
		CCombo combo = new CCombo(shlImcrypter, SWT.BORDER);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cr.setActive(combo.getItem(combo.getSelectionIndex()));
			}
		});
		combo.setEditable(false);
		combo.setFont(SWTResourceManager.getFont("Rockwell Condensed", 19, SWT.NORMAL));
		combo.setText("Key Identifiers");
		combo.setBounds(22, 216, 198, 42);
		m_bindingContext = initDataBindings();

		// Combo list objects
		ArrayList<String> identifiers = km.checkIdentifiers();
		try {
			if (identifiers.size() == 0) {
				setUpdate("No entries in Identifiers.txt");
			} else {
				for (int i = 0; i < identifiers.size(); i++) {
				combo.add(identifiers.get(i));
				}
			}
		} catch (NullPointerException npe) {
			setUpdate("Error reading Keys file");
		}
		
		// FILE
		// SELECTOR//////////////////////////////////////////////////////////////////////////////////
		Button btnNewButton = new Button(shlImcrypter, SWT.NONE);
		btnNewButton.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				JFileChooser jfc = new JFileChooser(Filer.INDIREC);
				FileNameExtensionFilter jpegFilter = new FileNameExtensionFilter("JPG & IC", "jpg", "ic");
				jfc.setFileFilter(jpegFilter);
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					String fileName = selectedFile.getName();
					if (fileName.length() > 21) {
						fileLabel.setText(fileName.substring(0, 22) + "...");
					} else {
						fileLabel.setText(fileName);
					}
					fr.setActive(selectedFile.getAbsolutePath());
				}
			}
		});
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton.setFont(SWTResourceManager.getFont("Rockwell Condensed", 29, SWT.NORMAL));
		btnNewButton.setBounds(22, 87, 198, 78);
		btnNewButton.setText("Select File");

		/////////////////////////////////////////////////////////////////////////////////////////////
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}

	public static void setUpdate(String update) {
		updateLabel.setText(update);
	}

	protected static String getPass() {
		JFrame passWin = new JFrame();
        String newPass = JOptionPane.showInputDialog(passWin, "Enter password for selected key", null);
        return newPass;
    }
}