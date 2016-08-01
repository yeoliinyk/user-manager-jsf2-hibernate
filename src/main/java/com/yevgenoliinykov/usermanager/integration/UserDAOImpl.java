package com.yevgenoliinykov.usermanager.integration;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.jboss.logging.Logger;

import com.yevgenoliinykov.usermanager.model.User;

@Named
@Stateless
public class UserDAOImpl implements UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class);

    @PersistenceContext(unitName = "usermanager")
    private EntityManager em;

    @Override
    public void createUser(User user) {
	user.setPassword(new Sha256Hash(user.getPassword()).toHex());
	em.persist(user);
    }

    @Override
    public void updateUser(User user) {
	em.merge(user);
    }

    @Override
    public void deleteUser(Long id) {
	User target = em.getReference(User.class, id);
	em.remove(target);
    }

    @Override
    public boolean isLoginInDB(String login) {
	try {
	    Long userId = em.createNamedQuery("User.findUserIdByLogin", Long.class).setParameter("login", login)
		    .getSingleResult();
	    LOGGER.info("Database already contains this login: '" + login + "' with user id: '" + userId + "'");
	} catch (NoResultException e) {
	    LOGGER.info("There is no such login in database!");
	    return false;
	}
	return true;
    }

    @Override
    public User findUserWithRolesById(Long id) {
	User user = null;
	try {
	    user = em.createNamedQuery("User.findUserById", User.class).setParameter("userId", id).getSingleResult();
	    LOGGER.info("User with id " + id + " exists");
	} catch (NoResultException e) {
	    LOGGER.info("There is no such user with this id in database!");
	    return user;
	}
	return user;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findUsersByLoginOrTel(String login, String tel) {
	FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
	QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(User.class).get();
	org.apache.lucene.search.Query nameQuery;
	if (!login.equals("") && !tel.equals("")) {
	    LOGGER.info("Searching by login and tel...");
	    nameQuery = qb.bool().should(qb.keyword().onField("login").matching(login).createQuery())
		    .must(qb.keyword().onField("phoneNumbers.number").matching(tel).createQuery()).createQuery();
	} else if (!login.equals("")) {
	    LOGGER.info("Searching by login...");
	    nameQuery = qb.keyword().onField("login").matching(login).createQuery();
	} else {
	    LOGGER.info("Searching by tel...");
	    nameQuery = qb.keyword().onField("phoneNumbers.number").matching(tel).createQuery();
	}
	javax.persistence.Query persistenceQuery = fullTextEntityManager.createFullTextQuery(nameQuery, User.class);
	List<User> results = persistenceQuery.getResultList();
	return results;
    }
}
