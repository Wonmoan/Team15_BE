package kakao.rebit.book.dto;

public record BookResponse(
        Long id,
        String isbn,
        String title,
        String description,
        String author,
        String publisher,
        String cover,
        String pubDate,
        String link
) {

}
