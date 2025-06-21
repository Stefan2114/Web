package dosqas.javaweb.service;

import dosqas.javaweb.dto.FeedbackAddingRequest;
import dosqas.javaweb.dto.FeedbackAddingResponse;
import dosqas.javaweb.model.BlockedWords;
import dosqas.javaweb.model.Customer;
import dosqas.javaweb.model.Feedback;
import dosqas.javaweb.repository.BlockedWordsRepository;
import dosqas.javaweb.repository.CustomerRepository;
import dosqas.javaweb.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
