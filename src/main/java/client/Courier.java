package client;

public class Courier {
    private String login;
    private String password;
    private String firstName;

    public Courier(String login, String password, String firstname) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

      public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
