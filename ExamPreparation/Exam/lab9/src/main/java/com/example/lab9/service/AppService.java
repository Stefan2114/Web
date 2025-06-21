package com.example.lab9.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.lab9.DTO.UpdatedTaskDTO;
import com.example.lab9.Enum.StatusType;
import com.example.lab9.model.Task;
import com.example.lab9.model.TaskLog;
import com.example.lab9.model.User;
import com.example.lab9.repo.TaskLogRepository;
import com.example.lab9.repo.TaskRepository;
import com.example.lab9.repo.UserRepository;

@Service
public class AppService {
    private final UserRepository userRepository;
    private final TaskLogRepository taskLogRepository;
    private final TaskRepository taskRepository;
    private String username = new String();

    // runtime injection
    public AppService(UserRepository userRepository, TaskLogRepository taskLogRepository,
            TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskLogRepository = taskLogRepository;
        this.taskRepository = taskRepository;
    }

    public boolean login(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        if (user.isPresent()) {
            return true;
        }
        return false;
    }

    public List<Task> getTasks() {
        return this.taskRepository.findAll();
    }

    public void updateTaskStatus(UpdatedTaskDTO taskDTO) {
        Task task = taskRepository.findById(taskDTO.id()).get();

        User user = userRepository.findByUsername(taskDTO.username()).get();
        TaskLog taskLog = new TaskLog();
        taskLog.setNewStatus(taskDTO.newStatus().toString());
        taskLog.setOldStatus(task.getStatus().toString());
        taskLog.setTask(task);
        taskLog.setUser(user);
        taskLog.setTimestamp(LocalDateTime.now());
        taskLogRepository.save(taskLog);
        System.out.println(taskDTO);
        task.setStatus(taskDTO.newStatus());
        taskRepository.save(task);
        this.username = new String(taskDTO.username());
    }

    public String getLastUser() {
        return this.username;
    }
    // return user.getEmail().equals(challenge);
    // }

    // public List<Feedback> getFeedback() {
    // return feedbackRepository.findAll();
    // }

    // private boolean checkIfMessageNaughty(String message) {
    // List<BlockedWords> blockedWords = blockedWordsRepository.findAll();
    // int naughtyCnt = 0;

    // for (BlockedWords blockedWord : blockedWords) {
    // Pattern pattern = Pattern.compile(blockedWord.getPattern());
    // Matcher matcher = pattern.matcher(message);

    // while (matcher.find()) {
    // naughtyCnt++;
    // if (naughtyCnt > 3)
    // break;
    // }
    // }

    // return naughtyCnt > 3;
    // }

    // private String getHighlightedMessage(String message) {
    // List<BlockedWords> blockedWords = blockedWordsRepository.findAll();

    // for (BlockedWords blockedWord : blockedWords) {
    // Pattern pattern = Pattern.compile(blockedWord.getPattern(),
    // Pattern.CASE_INSENSITIVE);
    // Matcher matcher = pattern.matcher(message);

    // StringBuilder sb = new StringBuilder();
    // while (matcher.find()) {
    // String match = matcher.group();
    // matcher.appendReplacement(sb,
    // "<span class=\"highlight\">" + Matcher.quoteReplacement(match) + "</span>");
    // }
    // matcher.appendTail(sb);
    // message = sb.toString();
    // }

    // return message;
    // }

    // public FeedbackAddingResponse addFeedback(FeedbackAddingRequest request) {
    // FeedbackAddingResponse response = new FeedbackAddingResponse();

    // if (checkIfMessageNaughty(request.getMessage())) {
    // response.isNaughty = true;
    // response.highlightedMessage = getHighlightedMessage(request.getMessage());
    // } else {
    // response.isNaughty = false;

    // Customer customer = customerRepository.findByName(request.getCustomerName())
    // .orElseThrow(() -> new RuntimeException("Customer not found"));

    // Feedback feedback = new Feedback();
    // feedback.setCustomer(customer);
    // feedback.setMessage(request.getMessage());
    // feedback.setTimestamp(LocalDateTime.now());

    // feedbackRepository.save(feedback);
    // }

    // return response;
    // }

    // public String cleanMessage(String message) {
    // List<BlockedWords> blockedWords = blockedWordsRepository.findAll();

    // for (BlockedWords blockedWord : blockedWords) {
    // Pattern pattern = Pattern.compile(blockedWord.getPattern());
    // Matcher matcher = pattern.matcher(message);

    // while (matcher.find()) {
    // String match = matcher.group();
    // message = message.replace(match, "");
    // }
    // }

    // return message;
    // }
}
