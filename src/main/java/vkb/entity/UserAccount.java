package vkb.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "user_account", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount implements Serializable {
    private static final long serialVersionUID = -5478596133621588561L;

    @Id
    @Column(name = "id")
    String id;
    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;
    @Column(name = "email")
    String email;
    @Column(name = "phone")
    String phone;

    @Column(name = "registered_date")
    Date registeredDate;
    @Column(name = "description")
    String description;

    @Column(name = "subscription")
    String subscription;

    @Column(name = "gender")
    String gender;

    @Column(name = "location")
    String location;

}
