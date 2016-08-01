package com.yevgenoliinykov.usermanager.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

@AnalyzerDef(name = "entry", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class) , filters = {
	@TokenFilterDef(factory = StandardFilterFactory.class), @TokenFilterDef(factory = LowerCaseFilterFactory.class),
	@TokenFilterDef(factory = NGramFilterFactory.class, params = { @Parameter(name = "minGramSize", value = "3"),
		@Parameter(name = "maxGramSize", value = "5") }) })
@Entity
@Indexed
@Table(name = "USER")
@NamedQueries({ @NamedQuery(name = "User.findUserByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
	@NamedQuery(name = "User.findUserIdByLogin", query = "SELECT u.userId FROM User u WHERE u.login = :login"),
	@NamedQuery(name = "User.findUserById", query = "SELECT u FROM User u WHERE u.userId = :userId"),
	@NamedQuery(name = "User.checkUserLogin", query = "SELECT u.userId FROM User u WHERE u.email = :email AND u.password = :password") })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "login", nullable = false)
    @Field(analyzer = @Analyzer(definition = "entry") )
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "sex")
    private String sex;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Role> roles;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "user")
    @IndexedEmbedded
    private List<PhoneNumber> phoneNumbers;

    public User() {
	super();
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getSex() {
	return sex;
    }

    public void setSex(String sex) {
	this.sex = sex;
    }

    public Set<Role> getRoles() {
	return roles;
    }

    public void setRoles(Set<Role> roles) {
	this.roles = roles;
    }

    public List<PhoneNumber> getPhoneNumbers() {
	return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
	this.phoneNumbers = phoneNumbers;
    }
}
