/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import dto.Lists.MemberInfoListDTO;
import entities.MemberInfo;
import entities.Player;
import entities.User;

import java.util.List;

public class PlayerDTO extends GenericDTO{
    private Long id;
    private String name;
    private String email;
    private String phone;
    private int age;
    private MemberInfoListDTO memberInfoList;
    
    public PlayerDTO(Long id, String name, String email, String phone, int age, List<MemberInfo> memberInfoList) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        if(memberInfoList != null && memberInfoList.size() > 0) {
            this.memberInfoList = new MemberInfoListDTO(memberInfoList);
        }
    }

    //Without entities
    public PlayerDTO(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.email = player.getEmail();
        this.phone = player.getPhone();
        this.age = player.getAge();
        if(player.getMemberInfoList() != null && player.getMemberInfoList().size() > 0) {
            this.memberInfoList = new MemberInfoListDTO(player.getMemberInfoList());
        }
    }

    //Without lists
    public PlayerDTO(Player player, Boolean list) {
        this.id = player.getId();
        this.name = player.getName();
        this.email = player.getEmail();
        this.phone = player.getPhone();
        this.age = player.getAge();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public MemberInfoListDTO getMemberInfoList() {
        return memberInfoList;
    }

    public void setMemberInfoList(MemberInfoListDTO memberInfoList) {
        this.memberInfoList = memberInfoList;
    }

}
