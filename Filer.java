import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Filer {
	final static String INDIREC = "InFiles\\";
	final static String OUTDIREC = "OutFiles\\";
	private String activeFilePath;
	private File f;

	public String getActive() {
		return activeFilePath;
	}

	public String getActiveExtension(int len) {
		return activeFilePath.substring(activeFilePath.length() - len);
	}

	public void setActive(String newPath) {
		activeFilePath = newPath;
		f = new File(activeFilePath);
	}

	// converts an image to a byte array
	byte[] createImgByte() {
		BufferedImage ni;
		try {
			ni = ImageIO.read(new File(activeFilePath));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(ni, "jpg", baos);
			ImCrypter.setUpdate("Converted to Bytes");

			return baos.toByteArray();

		} catch (IOException e) {
			ImCrypter.setUpdate("Image to byte conversion error. Please ensure image is of JPG format.");
		}
		return null;
	}

	// generates file from encrypted bytes
	boolean createEnFile(byte[] imgdata) {
		if (imgdata == null) {
			ImCrypter.setUpdate("Canceled");
			return false;
		}
		try (FileOutputStream stream = new FileOutputStream(
				OUTDIREC + f.getName().substring(0, f.getName().length() - 4) + ".ic")) {
			stream.write(imgdata);
			return true;
		} catch (Exception ex) {
			ImCrypter.setUpdate("Output folder (Def: OutFile) not found");
			return false;
		}
	}

	// converts a byte array into an image
	boolean createImg(byte[] imgdata) {
		if (imgdata == null) {
			ImCrypter.setUpdate("Canceled");
			return false;
		} else {
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(imgdata);
				BufferedImage outimg = ImageIO.read(bais);
				ImageIO.write(outimg, "jpg", new File(OUTDIREC + f.getName().substring(0, f.getName().length() - 3) + ".jpg"));
				return true;
			} catch (IOException iox) {
				ImCrypter.setUpdate("Output folder (Def: OutFile) not found");
				return false;
			}
		}
	}

	// converts a file to a byte array
	byte[] readFileToByteArray() {
		File file = new File(activeFilePath);
		FileInputStream fis = null;
		byte[] bArray = new byte[(int) file.length()];
		
		try {
			fis = new FileInputStream(file);
			fis.read(bArray);
			fis.close();
		} catch (IOException ioExp) {
			ioExp.printStackTrace();
		}
		return bArray;
	}
}
