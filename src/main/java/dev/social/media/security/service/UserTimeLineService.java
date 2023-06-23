package dev.social.media.security.service;

import dev.social.media.security.model.*;
import dev.social.media.security.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserTimeLineService {


    @Autowired
    private final UserTimeLineRepository userTimeLineRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private final CommentRepository commentRepository;




    public void allSharedContentFromUser(AppUser user) {

        // return everything from user shared
        userTimeLineRepository.findByUser(user);

    }


    public void ShareCommentOnTimeLine(ObjectId commentId, AppUser appUser) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if (commentOptional.isEmpty()) {
            throw new NullPointerException("Post with that id not found");
        }

        // this user is the one who made the post
        Comment comment = commentOptional.get();

        AppUser postUser = comment.getUser();

        //check if the post belongs to the user
        // basically shar your own post
        if (comment.getUser().equals(appUser)) {
            shareComment(comment,postUser);

            //share your followers post
        }
        // sharw whatever post u like as long as the user isnt private
        else if (!appUser.isPrivate()) {
            shareComment(comment,appUser);

        }
        LocalDateTime dateTime = LocalDateTime.now();

        // the post and the person who shared it is saved to the timeline database
        userTimeLineRepository.insert(new UserTimeLine(new ObjectId(),comment.getContent(),dateTime,null,appUser));



    }

    public Comment shareComment(Comment comment, AppUser appUser) {

        AppUser user = userRepository.findByEmailIgnoreCase(appUser.getEmail());




        if (user == null) {
            throw  new UsernameNotFoundException("User not found with that email");

        }
        LocalDateTime dateTime = LocalDateTime.now();


        Comment sharedComment = new Comment();

        sharedComment.setContent(comment.getContent());
        sharedComment.setCreatedAt(dateTime);
        sharedComment.setUser(user);

        return commentRepository.save(comment);



    }



    public void SharePostOnTimeLine(ObjectId postId, AppUser appUser) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()) {
            throw new NullPointerException("Post with that id not found");
        }

        // this user is the one who made the post
        Post post = postOptional.get();

        AppUser postUser = post.getUser();

        //check if the post belongs to the user
        // basically shar your own post
        if (post.getUser().equals(appUser)) {
            sharePost(post,postUser);

            //share your followers post
        }
        // sharw whatever post u like as long as the user isnt private
        else if (!appUser.isPrivate()) {
            sharePost(post,appUser);

        }

        LocalDateTime dateTime = LocalDateTime.now();

        // the post and the person who shared it is saved to the timeline database
        userTimeLineRepository.insert(new UserTimeLine(new ObjectId(),post.getContent(),dateTime,null,appUser));


    }

    public Post sharePost(Post post, AppUser appUser) {

        AppUser user = userRepository.findByEmailIgnoreCase(appUser.getEmail());




        if (user == null) {
            throw  new UsernameNotFoundException("User not found with that email");

        }
        LocalDateTime dateTime = LocalDateTime.now();


        Post sharedPost = new Post();

        sharedPost.setContent(post.getContent());
        sharedPost.setCreatedAt(dateTime);
        sharedPost.setUser(user);

        return postRepository.save(sharedPost);



    }

}
