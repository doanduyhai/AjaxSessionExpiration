package doan.ajaxsessionexpiration.demo.domain;

public class User
{

	private String login;

	private String firstname;

	private String lastname;

	public User(String login, String firstname, String lastname) {
		super();
		this.login = login;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	public String getLogin()
	{
		return login;
	}

	public void setLogin(String login)
	{
		this.login = login;
	}

	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	public String getLastname()
	{
		return lastname;
	}

	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

}
