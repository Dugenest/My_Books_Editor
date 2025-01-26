package com.afci.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.afci.data.Book;
import com.afci.data.BookRepository;
import com.afci.exception.NotFoundException;
import com.afci.exception.DuplicateException;
import com.afci.exception.InvalidDataException;

@Service
public class BookService {

	private final BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	// Obtenir tous les livres
	public List<Book> getAllBooks() {
		return (List<Book>) bookRepository.findAll();
	}

	// Vérifie si un livre existe par ID
	public boolean existsById(Long id) {
		return bookRepository.existsById((long) id);
	}

	// Obtenir un livre par son ID
	public Book getBookById(Long id) {
		return bookRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Book not found with id: " + id));
	}

	// Ajout d'un livre
	public Book addBook(Book book) {
		// Vérification de l'ID du livre
		if (book.getIdBook() != 0 && bookRepository.existsById((long) book.getIdBook())) {
			throw new DuplicateException("Book with ID " + book.getIdBook() + " already exists.");
		}

		// Vérification des données invalides
		validateBookData(book);

		// Sauvegarde du livre
		return bookRepository.save(book);
	}

	// Méthode de validation centralisée
	private void validateBookData(Book book) {
		if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
			throw new InvalidDataException("Book title cannot be null or empty.");
		}

		if (book.getAuthor() == null || book.getAuthor().getAuthorId() == null) {
			throw new InvalidDataException("Author ID is required.");
		}

		if (book.getCategories() == null || book.getCategories().isEmpty()) {
			throw new InvalidDataException("At least one category is required.");
		}
	}

	// Mettre à jour un livre existant
	public Book updateBook(Long id, Book bookDetails) {
		Book existingBook = getBookById(id);

		// Vérification des données invalides pour la mise à jour
		if (bookDetails.getTitle() == null || bookDetails.getTitle().isEmpty()) {
			throw new InvalidDataException("Book title cannot be null or empty.");
		}

		// Mettre à jour les champs
		existingBook.setTitle(bookDetails.getTitle());
		existingBook.setDateBook(bookDetails.getDateBook());

		return bookRepository.save(existingBook);
	}

	// Supprimer un livre
	public void deleteBook(Long id) {
		Book existingBook = getBookById(id);
		bookRepository.delete(existingBook);
	}
}
