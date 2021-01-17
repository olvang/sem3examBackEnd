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
public class UsernameTakenException extends Exception {
    public UsernameTakenException(String username) {
        super("Username " + username + " is already in use");
    }
}
