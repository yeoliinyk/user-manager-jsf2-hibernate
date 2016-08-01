package com.yevgenoliinykov.usermanager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
@Table(name = "PHONE_NUMBER")
public class PhoneNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_number_id", unique = true, nullable = false)
    private Long phoneNumberId;

    @Column(name = "number", nullable = false)
    @Field(analyzer = @Analyzer(definition = "entry") )
    private String number;

    @ContainedIn
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public PhoneNumber() {
	super();
    }

    public PhoneNumber(User user) {
	this.user = user;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Long getPhoneNumberId() {
	return phoneNumberId;
    }

    public void setPhoneNumberId(Long phoneNumberId) {
	this.phoneNumberId = phoneNumberId;
    }

    public String getNumber() {
	return number;
    }

    public void setNumber(String number) {
	this.number = number;
    }
}
