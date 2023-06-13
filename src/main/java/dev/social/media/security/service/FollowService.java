package dev.social.media.security.service;

import dev.social.media.security.model.*;
import dev.social.media.security.repository.FollowRepository;
import dev.social.media.security.repository.PostRepository;
import dev.social.media.security.repository.UserFollowRepository;
import dev.social.media.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.yarn.webapp.hamlet2.Hamlet;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service

@AllArgsConstructor
@Slf4j
public class FollowService {


    /*@Id
    private ObjectId id;
    @DBRef
    private AppUser follower;

    private LocalDateTime createdAt;*/


    @Autowired
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    private final UserFollowRepository userFollowRepository;

    public Follow createFollow(AppUser userEmail, AppUser targetUserEmail) {

        AppUser user = userRepository.findByEmailIgnoreCase(userEmail.getEmail());
        AppUser targetUser= userRepository.findByEmailIgnoreCase(targetUserEmail.getEmail());



        if (user == null && targetUser== null) {
            throw  new UsernameNotFoundException("User not found with that email");

        }

        // retrieve the followers exisiting follows

        Optional<List<Follow>> existingFollow = followRepository.findByFollower(user);

        if (existingFollow.isEmpty()) {
            throw new NullPointerException("The user has no followers in the list");

        }

        boolean followExist = existingFollow.get().stream()
                .anyMatch(follow -> follow.getFollower().equals(targetUser));

        if (followExist) {
            // the relationship already exist in this case

            throw new IllegalStateException("the follower already exist");
        }




        LocalDateTime dateTime = LocalDateTime.now();

        UserFollow userFollow = new UserFollow();

        userFollow.setFollower(user);

        userFollow.setFollowedUser(targetUser);

        userFollow.setCreatedAt(dateTime);

        userFollowRepository.save(userFollow);

       return followRepository.insert(new Follow(new ObjectId(),user,dateTime));




    }


    // this method handles getting all followers
    // for a specific user
    public List<AppUser> getAllFollowers(AppUser user) {
        Optional<List<UserFollow>> appFollow = userFollowRepository.findByFollower(user);

        if (appFollow.isEmpty()) {

            throw new NullPointerException("No followers found");

        }

        List<UserFollow> followList = appFollow.get();
        List<AppUser> followers = new ArrayList<>();

        for (UserFollow follow: followList) {
        followers.add(follow.getFollower());
        }

        return followers;
    }

    // this method handles gettling all followings from a specific user

    public List<AppUser> findAllFollowings(AppUser user) {
        Optional<List<UserFollow>> followListOption = userFollowRepository.findByFollower(user);

        if (followListOption.isEmpty()) {
            throw new NullPointerException("No followings for a user");
        }

        List<UserFollow> followList = followListOption.get();
        List<AppUser> followings = new ArrayList<>();

        for (UserFollow follow : followList) {
            followings.add(follow.getFollowedUser());
        }


        return followings;
    }





    public Optional<Follow> getFollowById(ObjectId followId) {
        return followRepository.findById(followId);

    }


    public void removeFollow(AppUser follower, AppUser targetUser) {
        Optional<List<Follow>> followListOptional = followRepository.findByFollower(follower);

        if (followListOptional.isPresent()) {
            List<Follow> followList = followListOptional.get();



            Optional<Follow> followOptional = followList.stream()
                    .filter(follow -> follow.getFollower().equals(targetUser))
                    .findFirst();

            if (followOptional.isPresent()) {
                Follow follow = followOptional.get();
                followRepository.delete(follow);
            } else {
                throw new NoSuchElementException("Follow relationship not found");
            }
        } else {
            throw new NoSuchElementException("No follows found for the follower");
        }

        // Remove follow from UserFollow collection
        Optional<List<UserFollow>> userFollowListOptional = userFollowRepository.findByFollower(follower);

        if (userFollowListOptional.isPresent()) {
            List<UserFollow> userFollowList = userFollowListOptional.get();

            Optional<UserFollow> userFollowOptional = userFollowList.stream()
                    .filter(userFollow -> userFollow.getFollowedUser().equals(targetUser))
                    .findFirst();

            if (userFollowOptional.isPresent()) {
                UserFollow userFollow = userFollowOptional.get();
                userFollowRepository.delete(userFollow);
            } else {
                throw new NoSuchElementException("UserFollow relationship not found");
            }
        } else {
            throw new NoSuchElementException("No UserFollows found for the follower");
        }
    }




    public Optional<Follow> findById(ObjectId followId) {
        return followRepository.findById(followId);
    }
    public Optional<UserFollow> findUserFollowById(ObjectId followId) {
        return userFollowRepository.findById(followId);
    }

    public boolean isFollowed(AppUser follower, AppUser targetUser) {
        Optional<List<Follow>> followListOptional = followRepository.findByFollower(follower);

        if (followListOptional.isPresent()) {
            List<Follow> followList = followListOptional.get();
            return followList.stream()
                    .anyMatch(follow -> follow.getFollower().equals(targetUser));
        }

        return false;
    }


}
