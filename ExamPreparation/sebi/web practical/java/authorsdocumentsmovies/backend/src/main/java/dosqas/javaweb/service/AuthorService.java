package dosqas.javaweb.service;

import dosqas.javaweb.dto.AuthoredWorkDTO;
import dosqas.javaweb.model.Author;
import dosqas.javaweb.model.Document;
import dosqas.javaweb.model.Movie;
import dosqas.javaweb.repository.AuthorRepository;
import dosqas.javaweb.repository.DocumentRepository;
import dosqas.javaweb.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final DocumentRepository documentRepository;
    private final MovieRepository movieRepository;

    // runtime injection
    public AuthorService(AuthorRepository authorRepository, DocumentRepository documentRepository, MovieRepository movieRepository) {
        this.authorRepository = authorRepository;
        this.documentRepository = documentRepository;
        this.movieRepository = movieRepository;
    }

    private List<Integer> deserializeList(String listOfIds) {
        if (listOfIds == null || listOfIds.isEmpty()) {
            return List.of();
        }
        String[] idStrings = listOfIds.split(",");
        List<Integer> ids = new ArrayList<>();
        for (String idStr : idStrings) {
            ids.add(Integer.parseInt(idStr.trim()));
        }
        return ids;
    }

    private String serializeList(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Integer id : ids) {
            builder.append(id);
            builder.append(",");
        }

        return builder.toString();
    }

    public Boolean checkAuthor(String authorName, Integer authoredId) {
        Author author = authorRepository.findByName(authorName)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        List<Integer> deserializedIdList = deserializeList(author.getDocumentList() + "," + author.getMovieList());

        return deserializedIdList.contains(authoredId);
    }

    public Boolean addDocument(String authorName, String documentName, String contents) {
        Document document = new Document();
        document.setName(documentName);
        document.setContents(contents);

        Document savedDocument = documentRepository.save(document);

        Author author = authorRepository.findByName(authorName)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        author.setDocumentList(author.getDocumentList() + "," + savedDocument.getId());
        authorRepository.save(author);

        return true;
    }

    public List<AuthoredWorkDTO> getInterleavedDocumentsAndMovies(String authorName) {
        Author author = authorRepository.findByName(authorName)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        List<Integer> documentIdList = deserializeList(author.getDocumentList());
        List<Integer> movieIdList = deserializeList(author.getMovieList());

        List<Document> documentList = documentRepository.findAllById(documentIdList);
        List<Movie> movieList = movieRepository.findAllById(movieIdList);

        List<AuthoredWorkDTO> interleavedDocuments = new ArrayList<>();
        int index = 0;

        while (documentList.size() > index && movieList.size() > index) {
            Document document = documentList.get(index);
            Movie movie = movieList.get(index);
            AuthoredWorkDTO documentDTO = new AuthoredWorkDTO();

            documentDTO.setId(document.getId());
            documentDTO.setName(document.getName());
            documentDTO.setContents(document.getContents());

            AuthoredWorkDTO movieDTO = new AuthoredWorkDTO();
            movieDTO.setId(movie.getId());
            movieDTO.setTitle(movie.getTitle());
            movieDTO.setDuration(movie.getDuration());
            movieDTO.setIsMovie(true);

            index++;

            interleavedDocuments.add(documentDTO);
            interleavedDocuments.add(movieDTO);
        }

        while (documentList.size() > index) {
            Document document = documentList.get(index);
            AuthoredWorkDTO documentDTO = new AuthoredWorkDTO();
            documentDTO.setId(document.getId());
            documentDTO.setName(document.getName());
            documentDTO.setContents(document.getContents());

            index++;

            interleavedDocuments.add(documentDTO);
        }

        while (movieList.size() > index) {
            Movie movie = movieList.get(index);
            AuthoredWorkDTO movieDTO = new AuthoredWorkDTO();
            movieDTO.setId(movie.getId());
            movieDTO.setTitle(movie.getTitle());
            movieDTO.setDuration(movie.getDuration());
            movieDTO.setIsMovie(true);

            index++;

            interleavedDocuments.add(movieDTO);
        }

        return interleavedDocuments;
    }

    public Document getDocumentWithMostAuthors() {
        Integer maxCount = 0, maxDocumentId = 0;
        List<Author> authorList = authorRepository.findAll();
        Hashtable<Integer, Integer> documentCountMap = new Hashtable<>();

        for (Author author : authorList) {
            List<Integer> documentIdList = deserializeList(author.getDocumentList());
            for (Integer documentId : documentIdList) {
                if (!documentCountMap.containsKey(documentId)) {
                    documentCountMap.put(documentId, 0);
                }
                Integer newCount = documentCountMap.get(documentId) + 1;
                documentCountMap.put(documentId, newCount);
                if (newCount > maxCount) {
                    maxCount = newCount;
                    maxDocumentId = documentId;
                }
            }
        }

        return documentRepository.findById(maxDocumentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    public Boolean deleteMovie(String authorName, Integer movieId) {
        Author author = authorRepository.findByName(authorName)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        List<Integer> movieIdList = deserializeList(author.getMovieList());
        movieIdList.remove(movieId);
        author.setMovieList(serializeList(movieIdList));

        authorRepository.save(author);

        movieRepository.deleteById(movieId);

        return true;
    }
}