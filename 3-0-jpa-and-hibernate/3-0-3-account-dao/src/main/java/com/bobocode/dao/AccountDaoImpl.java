package com.bobocode.dao;

import com.bobocode.exception.AccountDaoException;
import com.bobocode.model.Account;
import com.bobocode.util.ExerciseNotCompletedException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class AccountDaoImpl implements AccountDao {
    private EntityManagerFactory emf;

    public AccountDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void save(Account account) {
        try (EntityManager entityManager = emf.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(account);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new AccountDaoException("Error during save account", e);
        }
    }

    @Override
    public Account findById(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        Account account = entityManager.find(Account.class, id);
        entityManager.getTransaction().commit();
        entityManager.close();
        return account;
    }

    @Override
    public Account findByEmail(String email) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
        Root<Account> fromAccounts = query.from(Account.class);
        query.select(fromAccounts).where(criteriaBuilder.equal(fromAccounts.get("email"), email));
        List<Account> resultList = entityManager.createQuery(query).getResultList();
        Account account = resultList.getFirst();
        entityManager.getTransaction().commit();
        entityManager.close();
        return account;
    }

    @Override
    public List<Account> findAll() {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
        Root<Account> fromAccounts = query.from(Account.class);
        query.select(fromAccounts);
        List<Account> accounts = entityManager.createQuery(query).getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return accounts;
    }

    @Override
    public void update(Account account) {
        try (EntityManager entityManager = emf.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.merge(account);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new AccountDaoException("Error during update account", e);
        }
    }

    @Override
    public void remove(Account account) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        Account managed = entityManager.merge(account);
        entityManager.remove(managed);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}

