import com.dropbox.core.*;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Locale;

/**
 * Created by Aditya on 10/18/2014.
 */
public class DropboxWriteFile extends Application{
	public void start(Stage stage) {
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
		try {
			File file = new File("users.dat");
			FileInputStream fos = new FileInputStream(file);
			ObjectInputStream oos = new ObjectInputStream(fos);
			for (int i = 0; i < 2; i++) {
				users[i] = (User) oos.readObject();
			}

			File fileToOpen = fileChooser.showOpenDialog(new Stage());
			String fileName = fileToOpen.getPath();
			byte[] bytes = new byte[0];
			if (fileToOpen != null) {
				// LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
				// BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
				// THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
				bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
				ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
				FileInputStream fis = new FileInputStream(fileToOpen);
				BufferedInputStream bis = new BufferedInputStream(fis);

				// HERE IT IS, THE ONLY READY REQUEST WE NEED
				bis.read(bytes);
				bis.close();
			}
			int temp = bytes.length/2;
			byte[] half1 = new byte[temp];
			byte[] half2 = new byte[bytes.length - temp];
			File file1 = new File(fileToOpen.getName() +"1.raid");
			File file2 = new File(fileToOpen.getName() +"2.raid");
			for(int i=0;i<temp;i++) {
				half1[i] = bytes[i];
			}
			for(int i=temp,j=0;i<bytes.length;i++,j++) {
				half1[j] = bytes[i];
			}
			FileOutputStream fis = new FileOutputStream(file1);
			fis.write(half1);
			fis = new FileOutputStream(file2);
			fis.write(half2);

			DbxClient client = new DbxClient(config, users[0].getAccessToken());
			File inputFile = new File(fileToOpen.getName() +"1.raid");
			FileInputStream inputStream = new FileInputStream(inputFile);
			try {
				DbxEntry.File uploadedFile = client.uploadFile("/"+file1.getName(),
						DbxWriteMode.add(), inputFile.length(), inputStream);
				System.out.println("Uploaded: " + uploadedFile.toString());
			} finally {
				inputStream.close();
			}

			client = new DbxClient(config, users[1].getAccessToken());
			inputFile = new File(fileToOpen.getName() +"2.raid");
			inputStream = new FileInputStream(inputFile);
			try {
				DbxEntry.File uploadedFile = client.uploadFile("/"+file2.getName(),
						DbxWriteMode.add(), inputFile.length(), inputStream);
				System.out.println("Uploaded: " + uploadedFile.toString());
			} finally {
				inputStream.close();
			}


		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
