/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author gamma
 */
public class DogDTO extends GenericDTO{
    String message;
    String status;

    public DogDTO(String message, String status) {
        this.message = message;
        this.status = status;
    }
    
    public String getId() {
        return message;
    }

    public void setId(String id) {
        this.message = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
