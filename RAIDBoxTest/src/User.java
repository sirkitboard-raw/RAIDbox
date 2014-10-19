import java.io.Serializable;

/**
 * Created by Aditya on 10/18/2014.
 */
public class User  implements Serializable{
	private String name;
	private String accessToken;

	public User(String name, String accessToken){
		this.name = name;
		this.accessToken = accessToken;
	}

	public String getName() {
		return name;
	}

	public String getAccessToken() {
		return accessToken;
	}
}
