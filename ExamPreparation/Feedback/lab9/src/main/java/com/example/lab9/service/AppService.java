package com.example.lab9.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lab9.DTO.FeedbackAddingRequest;
import com.example.lab9.DTO.FeedbackAddingResponse;
import com.example.lab9.model.BlockedWords;
import com.example.lab9.model.Customer;
import com.example.lab9.model.Feedback;
import com.example.lab9.repo.BlockedWordsRepository;
import com.example.lab9.repo.CustomerRepository;
import com.example.lab9.repo.FeedbackRepository;

@Service
public class AppService {
    private final CustomerRepository customerRepository;
    private final FeedbackRepository feedbackRepository;
    private final BlockedWordsRepository blockedWordsRepository;

    // runtime injection
    public AppService(CustomerRepository customerRepository, FeedbackRepository feedbackRepository,
            BlockedWordsRepository blockedWordsRepository) {
        this.customerRepository = customerRepository;
        this.feedbackRepository = feedbackRepository;
        this.blockedWordsRepository = blockedWordsRepository;
    }

    public Boolean authenticate(String name, String challenge) {
        Customer user = customerRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return user.getEmail().equals(challenge);
    }

    public List<Feedback> getFeedback() {
        return feedbackRepository.findAll();
    }

    private boolean checkIfMessageNaughty(String message) {
        List<BlockedWords> blockedWords = blockedWordsRepository.findAll();
        int naughtyCnt = 0;

        for (BlockedWords blockedWord : blockedWords) {
            Pattern pattern = Pattern.compile(blockedWord.getPattern());
            Matcher matcher = pattern.matcher(message);

            while (matcher.find()) {
                naughtyCnt++;
                if (naughtyCnt > 3)
                    break;
            }
        }

        return naughtyCnt > 3;
    }

    private String getHighlightedMessage(String message) {
        List<BlockedWords> blockedWords = blockedWordsRepository.findAll();

        for (BlockedWords blockedWord : blockedWords) {
            Pattern pattern = Pattern.compile(blockedWord.getPattern(), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(message);

            StringBuilder sb = new StringBuilder();
            while (matcher.find()) {
                String match = matcher.group();
                matcher.appendReplacement(sb,
                        "<span class=\"highlight\">" + Matcher.quoteReplacement(match) + "</span>");
            }
            matcher.appendTail(sb);
            message = sb.toString();
        }

        return message;
    }

    public FeedbackAddingResponse addFeedback(FeedbackAddingRequest request) {
        FeedbackAddingResponse response = new FeedbackAddingResponse();

        if (checkIfMessageNaughty(request.getMessage())) {
            response.isNaughty = true;
            response.highlightedMessage = getHighlightedMessage(request.getMessage());
        } else {
            response.isNaughty = false;

            Customer customer = customerRepository.findByName(request.getCustomerName())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            Feedback feedback = new Feedback();
            feedback.setCustomer(customer);
            feedback.setMessage(request.getMessage());
            feedback.setTimestamp(LocalDateTime.now());

            feedbackRepository.save(feedback);
        }

        return response;
    }

    public String cleanMessage(String message) {
        List<BlockedWords> blockedWords = blockedWordsRepository.findAll();

        for (BlockedWords blockedWord : blockedWords) {
            Pattern pattern = Pattern.compile(blockedWord.getPattern());
            Matcher matcher = pattern.matcher(message);

            while (matcher.find()) {
                String match = matcher.group();
                message = message.replace(match, "");
            }
        }

        return message;
    }
}
