package com.azael.taskapp.persistence.repositories;

import org.springframework.stereotype.Repository;

import com.azael.taskapp.persistence.entities.Status;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
}