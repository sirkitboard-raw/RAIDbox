import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.Locale;
import java.util.Scanner;

import com.dropbox.core.*;
import javafx.application.Application;


/**
 * Created by PHIEO_o on 10/18/2014.
 */
public class DropboxReadFile extends Application{
        public void start(Stage stage) throws IOException, DbxException {

            FileChooser fileChooser;
            fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("E:/Dropbox/"));

            fileChooser.setTitle("Writing file");
            final String APP_KEY = "3rsh9nltgm81v71";
            final String APP_SECRET = "rc9selnewcb5kzu";
            DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET); // key and secret
            User[] users = new User[2]; //creating two users
            DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
                    Locale.getDefault().toString());
            DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);


            File writeFile = fileChooser.showOpenDialog(new Stage());
            String fileName = writeFile.getPath(); // path we will be saving the file
            FileOutputStream outputStream = new FileOutputStream(fileName);

            String readingFirst = "";
            String readingSecond = "";

            for (int i = 0; i < 2; i++) {
                System.out.print("Enter the first Raid file name : ");
                System.out.print("Enter the second Raid file name : ");

                Scanner input = new Scanner(System.in);
                Scanner input2 = new Scanner(System.in);
                readingFirst = input.nextLine();
                readingSecond = input2.nextLine();
            }
            readingFirst += "1.raid";
            readingSecond += "2.raid";
            DbxClient client = new DbxClient(config, users[0].getAccessToken());
            try {
                DbxEntry.File downloadedFile = client.getFile(readingFirst, null,
                        outputStream);
                System.out.println("Metadata: " + downloadedFile.toString());
            } finally {
                outputStream.close();
            }
            DbxClient client2 = new DbxClient(config, users[1].getAccessToken());
            try {
                DbxEntry.File downloadedFile = client2.getFile(readingSecond, null,
                        outputStream);
                System.out.println("Metadata: " + downloadedFile.toString());
            } finally {
                outputStream.close();
            }

            //reading both files and adding them together
            byte[] bytes = new byte[0];
            if (writeFile != null) {
                // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
                // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
                // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
                bytes = new byte[Long.valueOf(writeFile.length()).intValue()];
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                FileInputStream fis = new FileInputStream(readingFirst);
                BufferedInputStream bis = new BufferedInputStream(fis);
                // HERE IT IS, THE ONLY READY REQUEST WE NEED
                bis.read(bytes);
                bis.close();
            }

            byte[] SecondBytes = new byte[0];
            if (writeFile != null) {

                SecondBytes = new byte[Long.valueOf(writeFile.length()).intValue()];
                ByteArrayInputStream bais = new ByteArrayInputStream(SecondBytes);
                FileInputStream fis = new FileInputStream(readingSecond);
                BufferedInputStream bis = new BufferedInputStream(fis);

                bis.read(SecondBytes);
                bis.close();
            }
            int firstByteLength = bytes.length;
            int secondByteLength = SecondBytes.length;
            int finalLengthForTotalBytes = firstByteLength + secondByteLength;

            byte[] totalBytes = new byte[finalLengthForTotalBytes];

            for (int i = 0; i < firstByteLength; i++) {
                totalBytes[i] = bytes[i];
            }
            for (int i = firstByteLength, j = 0; i < finalLengthForTotalBytes; i++, j++) {
                totalBytes[j] = SecondBytes[i];
            }

            //saving to file

            try {
                if (writeFile != null) {

                    FileOutputStream fos = new FileOutputStream(fileName);
                    fos.write(totalBytes);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void main(String[] args) {
            launch(args);
        }
    }


