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
public class DadDTO extends GenericDTO{
    String id;
    String joke;
    
    public DadDTO(String id, String joke) {
        this.id = id;
        this.joke = joke;
        System.out.println("FROM DADDTO " + joke);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }
    
    
}
