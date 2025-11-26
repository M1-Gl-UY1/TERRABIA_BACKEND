package com.m1sigl.terrabia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m1sigl.terrabia.models.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    // Trouver les conversations d'un user (via la relation ManyToMany)
    List<Conversation> findByParticipants_IdUser(Long idUser);
}
