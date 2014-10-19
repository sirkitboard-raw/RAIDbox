import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Locale;

/**
 * Created by Aditya on 10/18/2014.
 */
public class DropboxWriteFile {
	public static void main(String[] agrs) {
		FileChooser fileChooser;
		fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("E:/Dropbox/"));

		fileChooser.setTitle("Open File");
		final String APP_KEY = "3rsh9nltgm81v71";
		final String APP_SECRET = "rc9selnewcb5kzu";
		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
		User[] users = new User[2];
		DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
				Locale.getDefault().toString());
		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
		DbxClient client;

		try {
			File file = new File("users.dat");
			FileInputStream fos = new FileInputStream(file);
			ObjectInputStream oos = new ObjectInputStream(fos);
			for (int i = 0; i < 2; i++) {
				users[i] = (User) oos.readObject();
			}

			File fileToOpen = fileChooser.showOpenDialog(new Stage());
			String fileName = fileToOpen.getPath();
			if (fileToOpen != null) {
				// LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
				// BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
				// THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
				byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				FileInputStream fis = new FileInputStream(fileToOpen);
				BufferedInputStream bis = new BufferedInputStream(fis);

				// HERE IT IS, THE ONLY READY REQUEST WE NEED
				bis.read(bytes);
				bis.close();

				// NOW WE NEED TO LOAD THE DATA FROM THE BYTE ARRAY
				DataInputStream dis = new DataInputStream(bais);

				// NOTE THAT WE NEED TO LOAD THE DATA IN THE SAME
				// ORDER AND FORMAT AS WE SAVED IT
				// FIRST READ THE GRID DIMENSIONS
				int initGridColumns = dis.readInt();
				int initGridRows = dis.readInt();
				int[][] newGrid = new int[initGridColumns][initGridRows];

				// AND NOW ALL THE CELL VALUES
				for (int i = 0; i < initGridColumns; i++) {
					for (int j = 0; j < initGridRows; j++) {
						newGrid[i][j] = dis.readInt();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
