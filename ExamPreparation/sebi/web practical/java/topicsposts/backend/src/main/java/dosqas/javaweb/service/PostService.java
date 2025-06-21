package dosqas.javaweb.service;

import dosqas.javaweb.dto.PostDataDTO;
import dosqas.javaweb.dto.PostNotification;
import dosqas.javaweb.model.Post;
import dosqas.javaweb.model.Topic;
import dosqas.javaweb.repository.PostRepository;
import dosqas.javaweb.repository.TopicRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // runtime injection
    public PostService(PostRepository postRepository, TopicRepository topicRepository,
                          SimpMessagingTemplate messagingTemplate) {
        this.postRepository = postRepository;
        this.topicRepository = topicRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public List<PostDataDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostDataDTO> postDataDTOs = new ArrayList<>();

        for (Post post : posts) {
            PostDataDTO postDataDTO = new PostDataDTO();
            postDataDTO.setId(post.getId());
            postDataDTO.setUser(post.getUser());
            postDataDTO.setDate(post.getDate());
            postDataDTO.setText(post.getText());
            postDataDTO.setTopicName(post.getTopic().getTopicName());

            postDataDTOs.add(postDataDTO);
        }

        return postDataDTOs;
    }

    public Post addPost (String user, String topicName, String text) {
        Topic topic = topicRepository.findByTopicName(topicName)
                .orElseGet(() -> {
                    Topic newTopic = new Topic();
                    newTopic.setTopicName(topicName);
                    return topicRepository.save(newTopic);
                });

        Post post = new Post();
        post.setUser(user);
        post.setTopic(topic);
        post.setText(text);
        post.setDate(LocalDateTime.now());

        Post savedPost = postRepository.save(post);

        // Send WebSocket notification to all connected clients
        PostNotification notification = new PostNotification(
                savedPost.getId(),
                savedPost.getUser(),
                savedPost.getTopic().getTopicName(),
                savedPost.getText(),
                savedPost.getDate()
        );

        // auto converts objects to json
        // designed for websocket communication
        messagingTemplate.convertAndSend("/topic/posts", notification);

        return savedPost;
    }

    public Post modifyPost (Integer id, String user, String topicName, String text) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Topic topic = topicRepository.findByTopicName(topicName)
                .orElseGet(() -> {
                    Topic newTopic = new Topic();
                    newTopic.setTopicName(topicName);
                    return topicRepository.save(newTopic);
                });

        post.setUser(user);
        post.setTopic(topic);
        post.setText(text);
        post.setDate(LocalDateTime.now());

        postRepository.save(post);

        return post;
    }
}