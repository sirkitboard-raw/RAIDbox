/**
 * Created by Aditya on 10/18/2014.
 */
import com.dropbox.core.*;
import java.io.*;
import java.util.Locale;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException, DbxException {
		// Get your app key and secret from the Dropbox developers website.
		final String APP_KEY = "3rsh9nltgm81v71";
		final String APP_SECRET = "rc9selnewcb5kzu";
		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
		User[] users = new User[2];
		DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
				Locale.getDefault().toString());
		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
		DbxClient client;
		// Have the user sign in and authorize your app.
		for(int i=0;i<2;i++) {
			String name;
			System.out.print("Enter user name : ");
			Scanner input = new Scanner(System.in);
			name = input.nextLine();
			String authorizeUrl = webAuth.start();
			System.out.println("1. Go to: " + authorizeUrl);
			System.out.println("2. Click \"Allow\" (you might have to log in first)");
			System.out.println("3. Copy the authorization code.");
			String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
			// This will fail if the user enters an invalid authorization code.
			DbxAuthFinish authFinish = webAuth.finish(code);
			String accessToken = authFinish.accessToken;
			users[i] = new User(name, accessToken);
			client = new DbxClient(config, accessToken);

			System.out.println("Linked account: " + client.getAccountInfo().displayName);
		}
		File file = new File("users.dat");
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		for(int i=0;i<2;i++) {
			oos.writeObject(users[i]);
		}
	}
}