package org.example.services;

import org.example.models.Comment;
import org.example.repositories.CommentRepository;
import org.example.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentService {
    private CommentRepository commentRepository;

    private ItemRepository itemRepository;

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Autowired
    public void setItemRepository(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Comment> findByItem(int item){
        return commentRepository.findByItem(itemRepository.findById(item).orElse(null));
    }
}
