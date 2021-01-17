/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package errorhandling;

/**
 *
 * @author gamma
 */
public class LoginInvalidException extends Exception {
    public LoginInvalidException(String reason) {
        super("Invalid username or password, " + reason);
    }
}
