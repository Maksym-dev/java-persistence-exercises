package com.bobocode.dao;

import com.bobocode.model.Photo;
import com.bobocode.model.PhotoComment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

/**
 * Please note that you should not use auto-commit mode for your implementation.
 */
public class PhotoDaoImpl implements PhotoDao {
    private EntityManagerFactory entityManagerFactory;

    public PhotoDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Photo photo) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(photo);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public Photo findById(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Photo photo = entityManager.find(Photo.class, id);
        entityManager.getTransaction().commit();
        entityManager.close();
        return photo;
    }

    @Override
    public List<Photo> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Photo> query = criteriaBuilder.createQuery(Photo.class);
        Root<Photo> fromPhotos = query.from(Photo.class);
        query.select(fromPhotos);
        List<Photo> photos = entityManager.createQuery(query).getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return photos;
    }

    @Override
    public void remove(Photo photo) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Photo managed = entityManager.merge(photo);
        entityManager.remove(managed);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void addComment(long photoId, String comment) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            Photo photoReference = entityManager.getReference(Photo.class, photoId);
            PhotoComment photoComment = new PhotoComment(comment, photoReference);
            entityManager.persist(photoComment);
            entityManager.getTransaction().commit();
        }
    }
}
