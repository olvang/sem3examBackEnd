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
public class FullDTO {

    ChuckDTO chuck;
    DadDTO dad;
    DogDTO dog;
    XkcdDTO xkcd;
    IpDTO ip;

    public FullDTO() {
    }

    public ChuckDTO getChuckDTO() {
        return chuck;
    }

    public void setChuckDTO(ChuckDTO chuck) {
        this.chuck = chuck;
    }

    public DadDTO getDadDTO() {
        return dad;
    }

    public void setDadDTO(DadDTO dad) {
        this.dad = dad;
    }

    public DogDTO getDogDTO() {
        return dog;
    }

    public void setDogDTO(DogDTO dog) {
        this.dog = dog;
    }

    public XkcdDTO getXkcdDTO() {
        return xkcd;
    }

    public void setXkcdDTO(XkcdDTO xkcd) {
        this.xkcd = xkcd;
    }

    public IpDTO getIpDTO() {
        return ip;
    }

    public void setIpDTO(IpDTO ip) {
        this.ip = ip;
    }
    
}
