package dto;

import entities.User;

public class UserCredentialsDTO {
    private String username;
    private String password;

    public UserCredentialsDTO() {
    }

    public UserCredentialsDTO(User user) {
       this.username = user.getUserName();
    }

    public UserCredentialsDTO(String username, String password) {
        if(username != null) {
            this.username = username;
        }else {
            this.username = "";
        }
        if(password != null) {
            this.password = password;
        }else {
            this.password = "";
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
