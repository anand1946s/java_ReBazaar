package model;

public class User {
    private String username;
    private String password;
    private String email;
    private String phone;
    
    public User(String username, String password, String email, String phone) {
        setUsername(username);
        setPassword(password);
        this.email = email;
        setPhone(phone);
        //this.username = username;
        //this.password = password;
        //this.email = email;
        //this.phone = phone;
    }
    
    // Getters and setters
    
    
    public String getUsername() { return username; }
    public void setUsername(String username){
        if(username.length()>=5){
            this.username = username;
        }
        //fill an else statement
    }
    
    public String getPassword() { return password; }
    public void setPassword(String password) {
        if(password != null && password.length()>=8){
            this.password = password;
        }
        // fill an else condition
    }
    public String getPhone() { return phone; }
    public void setPhone(String phone){
        if(phone.length()==10){
            this.phone = phone;
        }
        //fill an else condition
    }
}