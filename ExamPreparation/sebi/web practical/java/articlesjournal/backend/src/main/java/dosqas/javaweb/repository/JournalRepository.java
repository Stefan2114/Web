package dosqas.javaweb.repository;

import dosqas.javaweb.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JournalRepository extends JpaRepository<Journal, Integer> {
    Optional<Journal> findByName(String name);
}

// Spring Data JPA Method Naming Conventions for Auto-Implemented Queries:
//
// 1. Basic Query Methods:
//    - Find by a single property: findBy<PropertyName>
//      Example: findByUser(String user)
//    - Find by multiple properties: findBy<Property1>And<Property2>
//      Example: findByUserAndJournal(String user, Journal journal)
//    - Find by a property with OR: findBy<Property1>Or<Property2>
//      Example: findByUserOrJournal(String user, Journal journal)
//
// 2. Sorting:
//    - Add OrderBy<PropertyName><Desc/Asc> to sort results.
//      Example: findByUserOrderByDateDesc(String user)
//
// 3. Comparison Operators:
//    - Equals: findBy<PropertyName>
//      Example: findById(Integer id)
//    - Less than: findBy<PropertyName>LessThan
//      Example: findByDateLessThan(LocalDateTime date)
//    - Greater than: findBy<PropertyName>GreaterThan
//      Example: findByDateGreaterThan(LocalDateTime date)
//    - Between: findBy<PropertyName>Between
//      Example: findByDateBetween(LocalDateTime start, LocalDateTime end)
//
// 4. Like/Containing:
//    - Like: findBy<PropertyName>Like
//      Example: findBySummaryLike(String pattern)
//    - Containing: findBy<PropertyName>Containing
//      Example: findBySummaryContaining(String keyword)
//
// 5. Null Checks:
//    - Is Null: findBy<PropertyName>IsNull
//      Example: findBySummaryIsNull()
//    - Is Not Null: findBy<PropertyName>IsNotNull
//      Example: findBySummaryIsNotNull()
//
// 6. Boolean Properties:
//    - Use the property name directly for boolean fields.
//      Example: findByPublishedTrue() or findByPublishedFalse()
//
// 7. Count Queries:
//    - Use countBy<PropertyName> to count matching records.
//      Example: countByUser(String user)
//
// 8. Delete Queries:
//    - Use deleteBy<PropertyName> to delete matching records.
//      Example: deleteByUser(String user)
//
// 9. Distinct Results:
//    - Add Distinct to fetch unique results.
//      Example: findDistinctByUser(String user)
//
// 10. Limiting Results:
//     - Use First or Top to limit the number of results.
//       Example: findTop3ByUserOrderByDateDesc(String user)